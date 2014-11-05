package com.elasticpath.rest.client.unmarshalling;

import static com.jayway.jsonpath.JsonPath.using;
import static java.lang.String.format;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.internal.spi.json.JacksonJsonProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;

@Named
@Singleton
public class JsonPathResultFactory {

	private static final Logger LOG = LoggerFactory.getLogger(JsonPathResultFactory.class);

	@Inject
	private ClassInstantiator classInstantiator;

	@Inject
	private ObjectMapper objectMapper;

	@Inject
	private JsonAnnotationsModelIntrospector jsonAnnotationsModelIntrospector;

	public <T> T create(Class<T> resultClass,
						String jsonResult) throws IOException {

		Configuration configuration = Configuration.defaultConfiguration().jsonProvider(new JacksonJsonProvider());
		Object jsonObject = objectMapper.readValue(jsonResult, Object.class);

		ReadContext jsonContext = using(configuration).parse(jsonObject);

		try {
			T resultObject = classInstantiator.newInstance(resultClass);

			for (Field field : jsonAnnotationsModelIntrospector.retrieveFieldsWithJsonAnnotations(resultClass)) {
				JsonPath jsonPathAnnotation = field.getAnnotation(JsonPath.class);
				JsonProperty jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
				sanityCheck(resultClass, field, jsonPathAnnotation, jsonPropertyAnnotation);
				if (jsonPathAnnotation != null) {
					performJsonPathUnmarshalling(jsonContext, resultObject, field, jsonPathAnnotation);
				} else {
					performJacksonUnmarshalling(jsonObject, resultObject, field, jsonPropertyAnnotation);
				}
			}
			return resultObject;
		} catch (IllegalAccessException | InstantiationException e) {
			LOG.error(format(
					"[%s] failed JsonPath parsing for with error: ",
					resultClass.getName()
			), e);
			throw new IllegalArgumentException(e);
		}
	}

	private <T> void sanityCheck(final Class<T> resultClass, final Field field, final JsonPath jsonPathAnnotation,
								 final JsonProperty jsonPropertyAnnotation) {
		if (jsonPathAnnotation != null && jsonPropertyAnnotation != null) {
			String errorMessage = format("JsonProperty and JsonPath annotations both detected on field [%s] in class [%s]",
					field.getName(), resultClass.getName());
			LOG.error(errorMessage);
			throw new IllegalStateException(errorMessage);
		}
	}

	private <T> void performJsonPathUnmarshalling(final ReadContext jsonContext, final T resultObject, final Field field, JsonPath jsonPath)
			throws IllegalAccessException, IOException {
		Class<?> fieldType = field.getType();
		Object read = readField(jsonContext, jsonPath, fieldType);
		setField(resultObject, field, fieldType, read);
	}

	// If there's a top level @JsonProperty then simply copy that map entry from the unmarshalled jsonObject,
	// and set it unchanged into the result object.
	@SuppressWarnings("unchecked")
	private <T> void performJacksonUnmarshalling(final Object jsonObject, final T resultObject, final Field field, JsonProperty jsonProperty)
			throws IOException, IllegalAccessException {
		Class<?> fieldType = field.getType();
		String fieldName = jsonProperty.value();
		Map<String, Object> jsonMap = (Map<String, Object>) jsonObject;
		Object read = jsonMap.get(fieldName);
		setField(resultObject, field, fieldType, read);
	}


	private <T> void setField(final T resultObject, final Field field, final Class<?> fieldType, final Object read)
			throws IllegalAccessException, IOException {
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
