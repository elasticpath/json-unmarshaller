package com.elasticpath.rest.json.unmarshalling.impl;

import static com.elasticpath.rest.json.unmarshalling.impl.FieldUtil.*;
import static com.elasticpath.rest.json.unmarshalling.impl.JsonPathUtil.*;
import static com.jayway.jsonpath.JsonPath.using;
import static java.lang.String.format;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.internal.spi.json.JacksonJsonProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elasticpath.rest.json.unmarshalling.JsonUnmarshaller;
import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;

public class DefaultJsonUnmarshaller implements JsonUnmarshaller {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultJsonUnmarshaller.class);

	private final ClassInstantiator classInstantiator;
	private final ObjectMapper objectMapper;
	private final JsonAnnotationsModelIntrospector jsonAnnotationsModelIntrospector;

	public DefaultJsonUnmarshaller(final ClassInstantiator classInstantiator, final ObjectMapper objectMapper,
								   final JsonAnnotationsModelIntrospector jsonAnnotationsModelIntrospector) {
		this.classInstantiator = classInstantiator;
		this.objectMapper = objectMapper;
		this.jsonAnnotationsModelIntrospector = jsonAnnotationsModelIntrospector;
	}

	@Override
	public <T> T unmarshall(final Class<T> resultClass, final String jsonResult) throws IOException {

		final Configuration configuration = Configuration.defaultConfiguration().jsonProvider(new JacksonJsonProvider());
		final ReadContext jsonContext = using(configuration).parse(jsonResult);//for JSONPath

		try {
			return unmarshall(classInstantiator.newInstance(resultClass), jsonContext, new LinkedList<String>());
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/*
	 * Unmarshall Json tree to POJOs, taking care of JsonPath and JsonProperty annotations on multiple levels
	  *
	 * @param resultObject an object currently being processed
	 * @param jsonContext Jway Json context
	 * @param jsonPathStack Deque for storing Json paths
	 * @param <T>
	 * @return unmarshalled POJO
	 * @throws IOException
	 */
	private <T> T unmarshall(final T resultObject, final ReadContext jsonContext, final Deque<String> jsonPathStack) throws IOException {

		final Class<?> resultClass = resultObject.getClass();
		final String resultClassName = resultClass.getName();

		try {
			final Iterable<Field> declaredFields = jsonAnnotationsModelIntrospector.retrieveAllFields(resultClass);

			if (declaredFields.iterator().hasNext()) {

				final String fullJsonPath = getJsonPath(jsonPathStack);
				final boolean isAbsolutePath = fullJsonPath.equals("");

				for (Field field : declaredFields) {
					JsonProperty jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
					JsonPath jsonPathAnnotation = field.getAnnotation(JsonPath.class);

					sanityCheck(jsonPathAnnotation, jsonPropertyAnnotation,resultClassName, field);

					final String jsonPath = getJsonAnnotationValue(jsonPathAnnotation, jsonPropertyAnnotation, field.getName(),
																			  isAbsolutePath);

					if (shouldPerformJsonPathUnmarshalling(jsonPathAnnotation, jsonPropertyAnnotation, field, getFieldValue(resultObject,field))){
						performJsonPathUnmarshalling(jsonContext, resultObject, field, jsonPath, fullJsonPath);
					}

					processMultiLevelAnnotations(jsonPathAnnotation, jsonPropertyAnnotation, field, getFieldValue(resultObject,field), jsonContext,
												 jsonPathStack);
				}
			}
			return resultObject;
		} catch (IllegalAccessException e) {
			LOG.error(format(
					"[%s] failed JsonPath parsing for with error: ", resultClassName
			), e);
			throw new IllegalArgumentException(e);
		}
	}



	/*
	 * Make recursive calls for all fields that contain JsonPath annotations.
	 * Fields annotated with JsonProperty will be automatically set on all levels as long as
	 * field name matches Json node name
	 *
	 * @param field
	 * @param fieldValue
	 * @param jsonPathAnnotation
	 * @param jsonPropertyAnnotation
	 * @param jsonPathStack
	 * @param jsonContext
	 * @throws IOException
	 */
	private void processMultiLevelAnnotations(final JsonPath jsonPathAnnotation,final JsonProperty jsonPropertyAnnotation,
											  final Field field, final Object fieldValue, final ReadContext jsonContext,
											  Deque<String> jsonPathStack)
			throws IOException {

		if (jsonAnnotationsModelIntrospector.hasJsonPathAnnotatatedFields(field)){
			if (fieldValue == null){
				return;
			}

			jsonPathStack = resolveRelativeJsonPaths(jsonPathAnnotation, jsonPropertyAnnotation, field.getName(), jsonPathStack);

			//handles arrays/Lists
			if (isFieldArrayOrListOfNonPrimitiveTypes(field)){
				unmarshalArrayOrList(fieldValue,jsonPathStack,jsonContext);

			}else {
				//handles anything else
				unmarshall(fieldValue, jsonContext, jsonPathStack);
			}
			jsonPathStack.pollLast();
		}
	}

	/*
	 * In case of arrays/lists, correct Json path must be created for accessing each Json node.
	  * The path looks like e.g. $.parent.array_node[0], $.parent.array_node[1] etc
	  *
	 * @param fieldValue used to determine whether a field is a list(iterable) or array
	 * @param jsonPathStack
	 * @param jsonContext
	 * @throws IOException
	 */
	private void unmarshalArrayOrList(final Object fieldValue, final Deque<String> jsonPathStack, final ReadContext jsonContext)
			throws IOException {

		Object[] fieldValueInstanceMembers;

		if (fieldValue instanceof List) {
			fieldValueInstanceMembers = ((List) fieldValue).toArray();
		} else {
			fieldValueInstanceMembers = (Object[]) fieldValue;
		}

		for (int i = 0; i < fieldValueInstanceMembers.length; i++) {
			Object member = fieldValueInstanceMembers[i];
			jsonPathStack.add("[" + i + "]");

			unmarshall(member, jsonContext, jsonPathStack);
			jsonPathStack.pollLast();
		}
	}

	/*
	 *
	   Rules to perform Json unmarshalling:
	   1. Field must be annotated with JsonPath or
	   2. Field is annotated with JsonProperty; it is primitive or (non-primitive and null)
	   3. if both annotations are missing, then check if field is non-primitive and null

	   Note:
			getFieldValue(resultObject,field) can't be resolved into var because in very first loop, returned value is null
			while after performing unmarshalling may be non-null

	  *
	 * @param jsonPathAnnotation
	 * @param jsonPropertyAnnotation
	 * @param field
	 * @param fieldValue
	 * @return
	 */
	private boolean shouldPerformJsonPathUnmarshalling(final JsonPath jsonPathAnnotation, final JsonProperty jsonPropertyAnnotation,
													   final Field field, final Object fieldValue) {

		final boolean isFieldPrimitive = field.getType().isPrimitive();


		return jsonPathAnnotation !=null || (jsonPropertyAnnotation != null && (isFieldPrimitive || fieldValue == null) ||
													 isFieldPrimitive || fieldValue == null);
	}



	/*
	 * Ensure that field cannot have both JsonPath and JsonProperty annotations
	  *
	 * @param resultClassName target class's name
	 * @param field target class's field
	 * @param jsonPathAnnotation
	 * @param jsonPropertyAnnotation
	 */
	private void sanityCheck(final JsonPath jsonPathAnnotation, final JsonProperty jsonPropertyAnnotation, final String resultClassName,
							 final Field field) {

		if (jsonPathAnnotation != null && jsonPropertyAnnotation != null) {
			String errorMessage = format("JsonProperty and JsonPath annotations both detected on field [%s] in class [%s]",
					field.getName(), resultClassName);
			LOG.error(errorMessage);
			throw new IllegalStateException(errorMessage);
		}
	}

	/*
	 * Unmarshalls Json value using Jway ReadContext and Jakson ObjectMapper into proper Java structure
	 *
	 * @param jsonContext Jway Json context
	 * @param resultObject target object, field owner
	 * @param field the field to be set with Json value
	 * @param jsonAnnotationValue Json path
	 * @param pathBuilder Contains full Json path
	 * @param <T>
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	private <T> void performJsonPathUnmarshalling(final ReadContext jsonContext, final T resultObject, final Field field,
												  String jsonPath, final String parentJsonPath)
			throws IllegalAccessException, IOException {

		final Class<?> fieldType = field.getType();
		jsonPath = buildCorrectJsonPath(jsonPath,parentJsonPath);

		final Object read = readField(jsonContext, jsonPath, fieldType);
		if (read == null && fieldType.isPrimitive()){
			return;
		}
		setField(resultObject, field, fieldType, read);
	}

	/*
	 * Set Java field using value obtained from readField method
	  *
	 * @param resultObject target object, field owner
	 * @param field the field to be set with found Json value
	 * @param fieldType field type; method checks if field is primitive, List or none of these
	 * @param read Json value
	 * @param <T>
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	private <T> void setField(final T resultObject, final Field field, final Class<?> fieldType, final Object read)
			throws IllegalAccessException, IOException {

		Type genericType = field.getGenericType();

		if (fieldType.isPrimitive()) {
			FieldUtil.setField(resultObject, field, read);

		} else if (genericType instanceof ParameterizedType) {//FIXME what about arrays? make a test
			final Class actualTypeArgument = getActualTypeArgument(genericType);

			final JavaType typedField = objectMapper.getTypeFactory().constructParametricType(fieldType, actualTypeArgument);
			FieldUtil.setField(resultObject, field, objectMapper.convertValue(read, typedField));

		} else {
			FieldUtil.setField(resultObject, field, objectMapper.convertValue(read, fieldType));
		}
	}

	/*
	 * Read value from Json tree for given JsonPath
	 	 *
	 * @param jsonContext Jway Json Context object that resolves fields for given path
	 * @param jsonPath Json path
	 * @param fieldType field type, used to determine whether field is Iterable or not
	 * @return
	 */
	private Object readField(final ReadContext jsonContext, final String jsonPath, final Class<?> fieldType) {

		Object read = null;
		try {
			read = jsonContext.read(jsonPath);
		} catch (PathNotFoundException e) {
			if (Iterable.class.isAssignableFrom(fieldType)) {//FIXME what about arrays? make a test
				read = new ArrayList();
			} else {
				LOG.trace(e.getMessage(), e);
			}
		}
		return read;
	}
}
