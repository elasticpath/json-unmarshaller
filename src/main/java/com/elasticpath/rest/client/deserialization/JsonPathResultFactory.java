package com.elasticpath.rest.client.deserialization;

import static com.google.common.collect.FluentIterable.from;
import static com.jayway.jsonpath.JsonPath.using;
import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.internal.spi.json.JacksonJsonProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elasticpath.rest.client.annotations.JsonPath;
import com.elasticpath.rest.client.url.CortexUrlFactory;

@Named
@Singleton
public class JsonPathResultFactory {

	private static final Logger LOG = LoggerFactory.getLogger(JsonPathResultFactory.class);

	@Inject
	private ClassInstantiator classInstantiator;

	@Inject
	private ObjectMapper objectMapper;

	@Inject
	private CortexUrlFactory cortexUrlFactory;

	public <T> T create(Class<T> resultClass,
						String jsonResult) throws IOException {

		Configuration configuration = Configuration.defaultConfiguration().jsonProvider(new JacksonJsonProvider());
		Object jsonObject = objectMapper.readValue(jsonResult, Object.class);

		ReadContext jsonContext = using(configuration).parse(jsonObject);

		try {
			T resultObject = classInstantiator.newInstance(resultClass);

			for (Field field : getInjectableFields(getSuperclassHierarchy(resultClass))) {
				JsonPath annotation = field.getAnnotation(JsonPath.class);
				Class<?> fieldType = field.getType();
				Object read = readField(jsonContext, annotation, fieldType);
				Type genericType = field.getGenericType();

				if (fieldType.isPrimitive()) {
					setField(resultObject, field, read);
				} else if (genericType instanceof ParameterizedType) {
					Class actualTypeArgument = getActualTypeArgument(genericType);

					JavaType typedField = objectMapper.getTypeFactory()
							.constructParametricType(fieldType, actualTypeArgument);
					setField(resultObject, field, objectMapper.convertValue(read, typedField));
				} else {
					setField(resultObject, field, objectMapper.convertValue(read, fieldType));
				}
			}
			return resultObject;
		} catch (IllegalAccessException | InstantiationException e) {
			LOG.error(format(
					"[%s] failed JsonPath parsing for zoom [%s] with error: ",
					resultClass.getName(),
					cortexUrlFactory.create("", resultClass)
			), e);
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Retrieves current class and all superclasses except Object.
	 *
	 * @param resultClass leaf class in the hierarchy to scan
	 * @return an iterable collection of classes
	 */
	private Iterable<Class<?>> getSuperclassHierarchy(Class<?> resultClass) {

		Collection<Class<?>> superclasses = new ArrayList<>();
		Class<?> klass = resultClass;
		while (!(klass.equals(Object.class))) {
			superclasses.add(klass);
			klass = klass.getSuperclass();
		}
		return superclasses;
	}

	/**
	 * Retrieves all fields valid for injection from the given iterable of classes.
	 * <p/>
	 * Fields are valid if they are annotated @JsonPath.
	 *
	 * @param classes any iterable collection of classes
	 * @return an iterable collection of fields
	 */
	private Iterable<Field> getInjectableFields(Iterable<Class<?>> classes) {

		return from(classes)
				.transformAndConcat(new Function<Class<?>, Iterable<? extends Field>>() {
					public Iterable<? extends Field> apply(java.lang.Class<?> input) {
						return asList(input.getDeclaredFields());
					}
				})
				.filter(new Predicate<Field>() {
					public boolean apply(Field input) {
						return input.isAnnotationPresent(JsonPath.class);
					}
				});
	}

	private <T> void setField(T resultObject,
							  Field field,
							  Object value) throws IllegalAccessException, IOException {
		field.setAccessible(true);
		field.set(resultObject, value);
		field.setAccessible(false);
	}

	private Object readField(ReadContext jsonContext,
							 JsonPath annotation,
							 Class<?> fieldType) {
		Object read = null;
		try {
			read = jsonContext.read(annotation.value());
		} catch (PathNotFoundException e) {
			if (Iterable.class.isAssignableFrom(fieldType)) {
				read = new ArrayList();
			} else {
				LOG.trace(e.getMessage(), e);
			}
		}
		return read;
	}

	private Class getActualTypeArgument(Type genericType) {
		return (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
	}
}
