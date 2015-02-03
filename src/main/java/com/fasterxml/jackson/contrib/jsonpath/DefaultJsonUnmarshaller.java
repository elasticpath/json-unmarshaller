package com.fasterxml.jackson.contrib.jsonpath;

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

import com.fasterxml.jackson.contrib.jsonpath.util.CandidateField;
import com.fasterxml.jackson.contrib.jsonpath.util.JsonPathUtil;
import com.fasterxml.jackson.contrib.jsonpath.util.ReflectionUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.internal.spi.json.JacksonJsonProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default implementation of {@link JsonUnmarshaller}.
 */
public class DefaultJsonUnmarshaller implements JsonUnmarshaller {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultJsonUnmarshaller.class);
	private ObjectMapper objectMapper;
	private final ReflectionUtil reflectionUtil;
	private final JsonPathUtil jsonPathUtil;

	/**
	 * Default constructor.
	 */
	public DefaultJsonUnmarshaller() {
		this(new ObjectMapper().disable(FAIL_ON_UNKNOWN_PROPERTIES));
	}

	/**
	 * Constructor that allows setting of a pre-configured object mapper.
	 * @param objectMapper the object mapper to use.
	 */
	public DefaultJsonUnmarshaller(final ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.reflectionUtil = new ReflectionUtil();
		this.jsonPathUtil = new JsonPathUtil();
	}

	@Override
	public <T> T unmarshal(final Class<T> resultClass, final String json) throws IOException {

		final JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider(objectMapper);
		final Configuration jwayConfiguration = Configuration.defaultConfiguration().jsonProvider(jacksonJsonProvider);
		final ReadContext jwayReadContext = using(jwayConfiguration).parse(json); //for JSONPath

		try {
			return recursivelyProcessAllFields(resultClass.newInstance(), jwayReadContext, new ArrayList<String>());
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public <T> boolean isSuitableForUnmarshalling(final Class<T> resultClass) {
		return reflectionUtil.shouldConstituentMembersBeUnmarshalled(resultClass);
	}

	@Override
	public void setObjectMapper(final ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/* Unmarshal Json tree to POJOs, taking care of JsonPath and JsonProperty annotations on multiple levels
	 *
	 * @param currentObject the object to process
	 * @param jwayReadContext Jway Json context
	 * @param parentJsonPath for storing Json paths
	 * @return unmarshalled POJO
	 */
	private <T> T recursivelyProcessAllFields(final T currentObject, final ReadContext jwayReadContext, final Collection<String> parentJsonPath) throws IOException {

		final Class<?> resultClass = currentObject.getClass();
		final String resultClassName = resultClass.getName();

		try {
			final Iterable<Field> declaredFields = reflectionUtil.retrieveAllFields(resultClass);

			if (!declaredFields.iterator().hasNext()) {
				return currentObject;
			}
			final String parentJsonPathString = jsonPathUtil.getJsonPath(parentJsonPath);
			final boolean isAbsolutePath = "".equals(parentJsonPathString);

			for (Field field : declaredFields) {

				final CandidateField candidateField = new CandidateField(field, currentObject);

				if (candidateField.isAppropriateForJsonPathUnmarshalling()) {
					final String jsonPathOnField = candidateField.getJsonAnnotationValue(isAbsolutePath);
					performJsonPathUnmarshalling(jwayReadContext, currentObject, field, jsonPathOnField, parentJsonPathString);
				}

				processChildFields(candidateField, currentObject, jwayReadContext, parentJsonPath);
			}
			return currentObject;
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
	private void processChildFields(final CandidateField candidateField, final Object resultObject,
			final ReadContext jwayReadContext, final Collection<String> parentJsonPath)	throws IOException, IllegalAccessException {

		final Field field = candidateField.getField();
		if (reflectionUtil.shouldConstituentMembersBeUnmarshalled(field.getType())) {

			final Object fieldValue = candidateField.getFieldValue(resultObject);
			if (fieldValue == null) {
				return;
			}

			final Collection<String> currentJsonPath = jsonPathUtil.resolveRelativeJsonPaths(candidateField, parentJsonPath);

			//handles arrays/Lists
			if (reflectionUtil.isFieldArrayOrList(field)) {
				unmarshalArrayOrList(fieldValue, jwayReadContext, currentJsonPath);

			} else {
				//handles anything else
				recursivelyProcessAllFields(fieldValue, jwayReadContext, currentJsonPath);
			}
		}
	}

	/*
	 * In case of arrays/lists, correct Json path must be created for accessing each Json node.
	 * The path looks like e.g. $.parent.array_node[0], $.parent.array_node[1] etc
	 *
	 * @param fieldValue used to determine whether a field is a list(iterable) or array
	 */
	private void unmarshalArrayOrList(final Object fieldValue, final ReadContext jwayReadContext, final Collection<String> parentJsonPath)
			throws IOException {

		Object[] fieldValues;

		if (fieldValue instanceof List) {
			fieldValues = ((List) fieldValue).toArray();
		} else {
			fieldValues = (Object[]) fieldValue;
		}

		if (!shouldProcessArrayMembers(fieldValues)) {
			return;
		}

		for (int i = 0; i < fieldValues.length; i++) {
			Object member = fieldValues[i];
			List<String> currentJsonPath = new ArrayList<>(parentJsonPath);
			currentJsonPath.add("[" + i + "]");
			recursivelyProcessAllFields(member, jwayReadContext, currentJsonPath);
		}
	}

	private boolean shouldProcessArrayMembers(final Object[] fieldValueInstanceMembers) {

		return fieldValueInstanceMembers.length > 0
				&& reflectionUtil.shouldConstituentMembersBeUnmarshalled(fieldValueInstanceMembers[0].getClass());
	}


	/*
	 * Unmarshals Json value using Jway ReadContext and Jakson ObjectMapper into proper Java structure
	 */
	private <T> void performJsonPathUnmarshalling(final ReadContext jwayReadContext, final T resultObject, final Field field,
			final String fieldJsonPath, final String parentJsonPath) throws IllegalAccessException, IOException {

		final Class<?> fieldType = field.getType();
		final String currentJsonPath = jsonPathUtil.buildCorrectJsonPath(fieldJsonPath, parentJsonPath);

		final Object unmarshalledValue = readValueFromJsonTree(jwayReadContext, currentJsonPath, fieldType);
		if (unmarshalledValue == null && fieldType.isPrimitive()) {
			return;
		}
		setField(resultObject, field, unmarshalledValue);
	}

	/*
	 * Set Java field using value obtained from readValueFromJsonTree method
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
	private Object readValueFromJsonTree(final ReadContext jwayReadContext, final String jsonPath, final Class<?> fieldType) {

		Object read = null;
		try {
			read = jwayReadContext.read(jsonPath);
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
