package com.elasticpath.rest.json.unmarshalling.impl;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.jayway.jsonpath.JsonPath.using;
import static java.lang.String.format;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.internal.spi.json.JacksonJsonProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elasticpath.rest.json.unmarshalling.JsonUnmarshaller;

/**
 * The default implementation of {@link JsonUnmarshaller}.
 */
public class DefaultJsonUnmarshaller implements JsonUnmarshaller {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultJsonUnmarshaller.class);
	private final ObjectMapper objectMapper;
	private final ReflectionUtil reflectionUtil;
	private final JsonPathUtil jsonPathUtil;

	/**
	 * Default constructor.
	 */
	public DefaultJsonUnmarshaller() {
		this.objectMapper = new ObjectMapper().disable(FAIL_ON_UNKNOWN_PROPERTIES);
		this.reflectionUtil = new ReflectionUtil();
		this.jsonPathUtil = new JsonPathUtil();
	}

	@Override
	public <T> T unmarshall(final Class<T> resultClass, final String json) throws IOException {

		final Configuration configuration = Configuration.defaultConfiguration().jsonProvider(new JacksonJsonProvider());
		final ReadContext jsonContext = using(configuration).parse(json); //for JSONPath

		try {
			return unmarshall(resultClass.newInstance(), jsonContext, new ArrayList<String>());
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/*
	 * Unmarshall Json tree to POJOs, taking care of JsonPath and JsonProperty annotations on multiple levels
	  *
	 * @param resultObject an object currently being processed
	 * @param jsonContext Jway Json context
	 * @param parentJsonPath for storing Json paths
	 * @return unmarshalled POJO
	 */
	private <T> T unmarshall(final T resultObject, final ReadContext jsonContext, final Collection<String> parentJsonPath) throws IOException {

		final Class<?> resultClass = resultObject.getClass();
		final String resultClassName = resultClass.getName();

		try {
			final Iterable<Field> declaredFields = reflectionUtil.retrieveAllFields(resultClass);

			if (declaredFields.iterator().hasNext()) {

				final String parentJsonPathString = jsonPathUtil.getJsonPath(parentJsonPath);
				final boolean isAbsolutePath = "".equals(parentJsonPathString);

				CandidateField candidateField;
				for (Field field : declaredFields) {

					candidateField = new CandidateField(field, resultObject);

					if (candidateField.isAppropriateForJsonPathUnmarshalling()) {
						final String jsonPathOnField = candidateField.getJsonAnnotationValue(isAbsolutePath);
						performJsonPathUnmarshalling(jsonContext, resultObject, field, jsonPathOnField, parentJsonPathString);
					}

					processMultiLevelAnnotations(candidateField, resultObject, jsonContext, parentJsonPath);
				}
			}
			return resultObject;
		} catch (IllegalAccessException e) {
			LOG.error(format("[%s] failed JsonPath parsing for with error: ", resultClassName), e);
			throw new IllegalArgumentException(e);
		}
	}


	/*
	 * Make recursive calls for all fields that contain JsonPath annotations.
	 * Fields annotated with JsonProperty will be automatically set on all levels as long as
	 * field name matches Json node name
	 *
	 */
	private void processMultiLevelAnnotations(final CandidateField candidateField, final Object resultObject,
			final ReadContext jsonContext, final Collection<String> parentJsonPath)	throws IOException, IllegalAccessException {

		final Field field = candidateField.getField();
		if (reflectionUtil.canUnmarshallClass(field.getType())) {

			final Object fieldValue = candidateField.getFieldValue(resultObject);
			if (fieldValue == null) {
				return;
			}

			final Collection<String> currentJsonPath = jsonPathUtil.resolveRelativeJsonPaths(candidateField, parentJsonPath);

			//handles arrays/Lists
			if (reflectionUtil.isFieldArrayOrList(field)) {
				unmarshalArrayOrList(fieldValue, jsonContext, currentJsonPath);

			} else {
				//handles anything else
				unmarshall(fieldValue, jsonContext, currentJsonPath);
			}
		}
	}

	/*
	 * In case of arrays/lists, correct Json path must be created for accessing each Json node.
	  * The path looks like e.g. $.parent.array_node[0], $.parent.array_node[1] etc
	  *
	 * @param fieldValue used to determine whether a field is a list(iterable) or array
	 */
	private void unmarshalArrayOrList(final Object fieldValue, final ReadContext jsonContext, final Collection<String> parentJsonPath)
			throws IOException {

		Object[] fieldValues;

		if (fieldValue instanceof List) {
			fieldValues = ((List) fieldValue).toArray();
		} else {
			fieldValues = (Object[]) fieldValue;
		}

		if (canUnmarshallMemberInstance(fieldValues)) {
			return;
		}

		for (int i = 0; i < fieldValues.length; i++) {
			Object member = fieldValues[i];
			List<String> currentJsonPath = new ArrayList<>(parentJsonPath);
			currentJsonPath.add("[" + i + "]");
			unmarshall(member, jsonContext, currentJsonPath);
		}
	}

	private boolean canUnmarshallMemberInstance(final Object[] fieldValueInstanceMembers) {

		return fieldValueInstanceMembers.length > 0
				&& !reflectionUtil.canUnmarshallClass(fieldValueInstanceMembers[0].getClass());
	}


	/*
	 * Unmarshalls Json value using Jway ReadContext and Jakson ObjectMapper into proper Java structure
	 */
	private <T> void performJsonPathUnmarshalling(final ReadContext jsonContext, final T resultObject, final Field field,
			final String fieldJsonPath, final String parentJsonPath) throws IllegalAccessException, IOException {

		final Class<?> fieldType = field.getType();
		final String currentJsonPath = jsonPathUtil.buildCorrectJsonPath(fieldJsonPath, parentJsonPath);

		final Object unmarshalledValue = unmarshallField(jsonContext, currentJsonPath, fieldType);
		if (unmarshalledValue == null && fieldType.isPrimitive()) {
			return;
		}
		setField(resultObject, field, unmarshalledValue);
	}

	/*
	 * Set Java field using value obtained from unmarshallField method
	 *
	 * @param resultObject target object, field owner
	 * @param field the field to be set with found Json value
	 * @param unmarshalledValue Json value
	 */
	private <T> void setField(final T resultObject, final Field field, final Object unmarshalledValue)
			throws IllegalAccessException, IOException {

		final Class<?> fieldType = field.getType();
		final Type genericType = field.getGenericType();

		if (fieldType.isPrimitive()) {
			reflectionUtil.setField(resultObject, field, unmarshalledValue);

		} else if (genericType instanceof ParameterizedType) {
			final Class<?>[] actualTypeArguments = reflectionUtil.geClassArgumentsFromGeneric(genericType);

			final JavaType typedField;
			if (actualTypeArguments.length == 1) {
				typedField = objectMapper.getTypeFactory().constructParametricType(fieldType, actualTypeArguments[0]);
			} else {
				typedField = objectMapper.getTypeFactory().constructMapLikeType(fieldType, actualTypeArguments[0], actualTypeArguments[1]);
			}

			reflectionUtil.setField(resultObject, field, objectMapper.convertValue(unmarshalledValue, typedField));

		} else {
			reflectionUtil.setField(resultObject, field, objectMapper.convertValue(unmarshalledValue, fieldType));
		}
	}

	/*
	 * Read value from Json tree for given JsonPath
	 */
	private Object unmarshallField(final ReadContext jsonContext, final String jsonPath, final Class<?> fieldType) {

		Object read = null;
		try {
			read = jsonContext.read(jsonPath);
		} catch (PathNotFoundException e) {
			if (Iterable.class.isAssignableFrom(fieldType)) {
				read = new ArrayList();
			} else {
				LOG.trace(e.getMessage(), e);
			}
		}
		return read;
	}
}
