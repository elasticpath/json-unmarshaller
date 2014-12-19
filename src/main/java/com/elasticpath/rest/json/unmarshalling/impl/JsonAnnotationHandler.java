package com.elasticpath.rest.json.unmarshalling.impl;

import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;

/**
 * Wraps given field and its annotations (if any) into a more convenient structure.
 * The class also provides additional set of methods for resoloving annotation values
 * as proper Json paths
 */
public class JsonAnnotationHandler {

	private final ReflectionUtil reflectionUtil;
	private final JsonProperty jsonPropertyAnnotation;
	private final JsonPath jsonPathAnnotation;
	private final Field field;

	/**
	 * Custom constructor that wraps field and its annotations (if any).
	 *
	 * @param field field to wrapped
	 */
	public JsonAnnotationHandler(final Field field) {
		this.field = field;
		this.jsonPathAnnotation = field.getAnnotation(JsonPath.class);
		this.jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
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
}