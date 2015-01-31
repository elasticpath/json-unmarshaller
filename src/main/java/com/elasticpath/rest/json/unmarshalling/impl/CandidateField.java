package com.elasticpath.rest.json.unmarshalling.impl;

import static java.lang.String.format;

import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;

/**
 * Wraps given field and its annotations (if any) into a more convenient structure.
 * The class also provides additional set of methods for resoloving annotation values
 * as proper Json paths
 */
public class CandidateField {

	private static final Logger LOG = LoggerFactory.getLogger(CandidateField.class);
	private final ReflectionUtil reflectionUtil;
	private final JsonProperty jsonPropertyAnnotation;
	private final JsonPath jsonPathAnnotation;
	private final Field field;
	private final Object containingObject;

	/**
	 * Custom constructor that wraps field and its annotations (if any).
	 *
	 * @param field field to wrapped
	 */
	public CandidateField(final Field field, final Object containingObject) {
		this.containingObject = containingObject;
		this.field = field;
		this.jsonPathAnnotation = field.getAnnotation(JsonPath.class);
		this.jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
		sanityCheck();
		this.reflectionUtil = new ReflectionUtil();
	}

	/**
	 * Get field value from a given object.
	 *
	 * @param resultObject object from which value will be taken
	 * @return field value
	 * @throws IllegalAccessException reflection exception
	 */
	public Object getFieldValue(final Object resultObject) throws IllegalAccessException {
		return reflectionUtil.getFieldValue(resultObject, this.field);
	}

	public Field getField() {
		return field;
	}

	public JsonProperty getJsonPropertyAnnotation() {
		return jsonPropertyAnnotation;
	}

	public JsonPath getJsonPathAnnotation() {
		return jsonPathAnnotation;
	}

	/**
	 * Check if @JsonPath and @JsonProperty are present.
	 * @return true if both annotations are present.
	 */
	public boolean areAnnotationsPresent() {
		return jsonPropertyAnnotation != null &&  jsonPathAnnotation != null;
	}

	/**
	 * Get annotation value from either annotation, if exists.
	 * In case that Json annotation is not present, field name will be used on the same way
	 * like the field is annotated with JsonProperty.
	 *
	 * @return Json path from either annotation or field name, if annotation is missing.
	 */
	public String getJsonPathFromField() {
		return getJsonAnnotationValue(false, false);
	}

	/**
	 * Get annotation value and transform JsonProperty value to a relative JsonPath, if required.
	 * In case that either of Json annotations is not present, field name will be used on the same way
	 * like the field is annotated with JsonProperty.
	 *
	 * @param isAbsolutePath if true, @JsonProperty value will be resoloved as relative; otherwise as absolute
	 * @return Json path
	 */
	public String getJsonAnnotationValue(final boolean isAbsolutePath) {
		return getJsonAnnotationValue(true, isAbsolutePath);
	}

	private String getJsonAnnotationValue(final boolean shouldResolveJsonPropertyAsJsonPath, final boolean isAbsolutePath) {

		if (jsonPathAnnotation != null) {
			return jsonPathAnnotation.value();
		}

		String jsonPath;
		if (jsonPropertyAnnotation == null) {
			jsonPath = field.getName();
		} else {
			jsonPath = jsonPropertyAnnotation.value();
		}

		if (shouldResolveJsonPropertyAsJsonPath) {
			return resolveJsonPropertyAsJsonPath(jsonPath, isAbsolutePath);
		}

		return jsonPath;
	}

	private String resolveJsonPropertyAsJsonPath(final String jsonPath, final boolean isAbsolutePath) {

		if (isAbsolutePath) {
			return jsonPath; //Jway ReadContext will resolve JsonProperty value as absolute path, by adding $.
		}

		//this makes JsonProperty annot value relative to parent JsonPath (or any other path)
		return "@." + jsonPath;
	}

	/*
	 * Ensure that field cannot have both JsonPath and JsonProperty annotations
	 */
	private void sanityCheck() {

		if (areAnnotationsPresent()) {
			String errorMessage = format("JsonProperty and JsonPath annotations both detected on field [%s] in class [%s]",
					getField().getName(), containingObject.getClass().getName());

			LOG.error(errorMessage);
			throw new IllegalStateException(errorMessage);
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
	 */
	public boolean isAppropriateForJsonPathUnmarshalling()
			throws IllegalAccessException {

		final boolean isFieldPrimitive = reflectionUtil.isFieldPrimitive(getField());
		final Object fieldValue = getFieldValue(containingObject);

		final boolean shouldUnmarshallJsonPathAnnotation = getJsonPathAnnotation() != null;

		return shouldUnmarshallJsonPathAnnotation
				|| shouldUnmarshallJsonPropertyAnnotation(getJsonPropertyAnnotation(), isFieldPrimitive, fieldValue)
				|| shouldUnmarshallNonAnnotatedField(isFieldPrimitive, fieldValue);
	}

	private boolean shouldUnmarshallJsonPropertyAnnotation(final JsonProperty jsonPropertyAnnotation, final boolean isFieldPrimitive,
			final Object fieldValue) {

		return jsonPropertyAnnotation != null && shouldUnmarshallNonAnnotatedField(isFieldPrimitive, fieldValue);
	}

	private boolean shouldUnmarshallNonAnnotatedField(final boolean isFieldPrimitive, final Object fieldValue) {

		return isFieldPrimitive || fieldValue == null;
	}

}