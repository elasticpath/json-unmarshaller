package com.elasticpath.rest.json.unmarshalling.impl;

import static com.elasticpath.rest.json.unmarshalling.impl.FieldUtil.*;
import static com.jayway.jsonpath.JsonPath.using;
import static java.lang.String.format;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import javax.inject.Named;
import javax.inject.Singleton;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.internal.spi.json.JacksonJsonProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elasticpath.rest.json.unmarshalling.JsonUnmarshaller;
import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;

@Named
@Singleton
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
	public <T> T unmarshall(Class<T> resultClass, String jsonResult) throws IOException {

		final Configuration configuration = Configuration.defaultConfiguration().jsonProvider(new JacksonJsonProvider());
		final ReadContext jsonContext = using(configuration).parse(jsonResult);//for JSONPath

		try {
			return unmarshall(classInstantiator.newInstance(resultClass), jsonContext, new LinkedList<String>());
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private <T> T unmarshall(T resultObject, ReadContext jsonContext, Deque<String> jsonPathBuilder) throws IOException {

		final Class resultClass = resultObject.getClass();
		final String resultClassName = resultClass.getName();

		try {
			final Iterable<Field> jsonAnnotatedFields = jsonAnnotationsModelIntrospector.retrieveFieldsWithJsonAnnotations(resultClass);

			if (jsonAnnotatedFields.iterator().hasNext()) {

				final String fullJsonPath = getJsonPath(jsonPathBuilder);

				for (Field field : jsonAnnotatedFields) {
					JsonProperty jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
					JsonPath jsonPathAnnotation = field.getAnnotation(JsonPath.class);

					sanityCheck(resultClassName, field, jsonPathAnnotation, jsonPropertyAnnotation);

					String jsonAnnotationValue;

					if (jsonPathAnnotation != null) {
						jsonAnnotationValue = jsonPathAnnotation.value();
					} else {
						//this makes JsonProperty annot value relative to parent JsonPath (or any other path)

						if (jsonPathBuilder.isEmpty()){
							jsonAnnotationValue = jsonPropertyAnnotation.value();
						}else{
							jsonAnnotationValue = fullJsonPath + "." + jsonPropertyAnnotation.value();
						}
					}
					/*
					   Rules to perform Json unmarshalling:
					   1. Field must be annotated with JsonPath or
					   2. Field is annotated with JsonProperty; it is primitive or (non-primitive and null)

					   Note:
					   		getFieldValue(resultObject,field) can't be resolved into var because in very first loop, returned value is null
					   		while after performing unmarshalling may be non-null
					 */
					if (shouldPerformJsonPathUnmarshalling(jsonPathAnnotation, jsonPropertyAnnotation, field, getFieldValue(resultObject,field))){
						performJsonPathUnmarshalling(jsonContext, resultObject, field, jsonAnnotationValue, fullJsonPath);
					}

					processMultiLevelAnnotations(field, getFieldValue(resultObject,field), jsonPathAnnotation, jsonPropertyAnnotation, jsonPathBuilder,
														jsonContext);
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

	private String getJsonPath(Queue<String> jsonPathBuilder){
		return Joiner.on("").join(jsonPathBuilder);
	}

	//TODO make a test with array/list of strings or primitives
	private void processMultiLevelAnnotations(Field field, Object fieldValue, JsonPath jsonPathAnnotation,
												   JsonProperty jsonPropertyAnnotation, Deque<String> jsonPathBuilder,
												   ReadContext jsonContext)
			throws IOException {

		if (jsonAnnotationsModelIntrospector.hasJsonPathAnnotatatedFields(field)){
			if (fieldValue == null){
				return;
			}

			jsonPathBuilder = resolveRelativeJsonPaths(jsonPropertyAnnotation, jsonPathAnnotation, jsonPathBuilder);

			//handles arrays/Lists
			if (isFieldArrayOrListOfNonPrimitiveTypes(field)){

				Object[] fieldValueInstanceMembers;

				if (fieldValue instanceof List) {
					fieldValueInstanceMembers = ((List) fieldValue).toArray();
				} else {
					fieldValueInstanceMembers = (Object[]) fieldValue;
				}

				for (int i = 0; i < fieldValueInstanceMembers.length; i++) {
					Object member = fieldValueInstanceMembers[i];
					jsonPathBuilder.add(".[" + i + "]");

					unmarshall(member, jsonContext, jsonPathBuilder);
					jsonPathBuilder.pollLast();
				}
			}else {
				//handles anything else
				unmarshall(fieldValue, jsonContext, jsonPathBuilder);
			}
			jsonPathBuilder.pollLast();
		}
	}
	private boolean shouldPerformJsonPathUnmarshalling(final JsonPath jsonPathAnnotation, final JsonProperty jsonPropertyAnnotation,
													   final Field field, final Object fieldValue) {

		//ignore non-null, non-primitive JProperty annotated fields
		return jsonPathAnnotation !=null || (jsonPropertyAnnotation != null && (field.getType().isPrimitive() || fieldValue == null));
	}

	private Deque<String> resolveRelativeJsonPaths(JsonProperty jsonPropertyAnnotation, JsonPath jsonPathAnnotation, Deque<String> jsonPathBuilder){

		String jsonPathVal = jsonPathAnnotation==null?jsonPropertyAnnotation.value():jsonPathAnnotation.value();

		if (jsonPropertyAnnotation != null){//handle jakson propery annotations
			if (jsonPathBuilder.isEmpty()){//transform first Jakson property into JsonPath root
				jsonPathBuilder.add("$." + jsonPathVal);
			}else {
				jsonPathBuilder.add("." + jsonPathVal);//all other jakson props will be simply appended
			}

		}else if (jsonPathVal.charAt(0) == '@'){//@.property
			if (jsonPathBuilder.isEmpty()){
				jsonPathBuilder.add(jsonPathVal.replaceFirst("@", "\\$"));
			}else {
				jsonPathBuilder.add(jsonPathVal.substring(1));
			}
		}else if (jsonPathVal.charAt(0) == '$' && !jsonPathBuilder.isEmpty()) {
			jsonPathBuilder = new LinkedList<>();
			jsonPathBuilder.add(jsonPathVal);
		}else{
			jsonPathBuilder.add(jsonPathVal);
		}

		return jsonPathBuilder;
	}

	/*
	 * Ensure that field cannot have both JsonPath and JsonProperty annotations
	  *
	 * @param resultClassName target class's name
	 * @param field target class's field
	 * @param jsonPathAnnotation
	 * @param jsonPropertyAnnotation
	 */
	private void sanityCheck(final String resultClassName, final Field field, final JsonPath jsonPathAnnotation,
								 final JsonProperty jsonPropertyAnnotation) {

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
	private <T> void performJsonPathUnmarshalling(final ReadContext jsonContext, final T resultObject, final Field field, String jsonAnnotationValue,
												  final String fullJsonPath)
			throws IllegalAccessException, IOException {

		final Class<?> fieldType = field.getType();
		if (jsonAnnotationValue.charAt(0) == '@'){
			jsonAnnotationValue = (fullJsonPath.equals("")?"$":fullJsonPath) + jsonAnnotationValue.substring(1);
		}

		final Object read = readField(jsonContext, jsonAnnotationValue, fieldType);
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
			Class actualTypeArgument = getActualTypeArgument(genericType);

			JavaType typedField = objectMapper.getTypeFactory()
					.constructParametricType(fieldType, actualTypeArgument);
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
	private Object readField(ReadContext jsonContext,
							 String jsonPath,
							 Class<?> fieldType) {
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
