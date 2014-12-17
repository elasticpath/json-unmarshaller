package com.elasticpath.rest.json.unmarshalling.impl;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;

/**
 * Various util methods for getting/fixing Json paths, taken from annotations.
 * Some paths are handled by Jway fmwk, but not all.
 *
 * TODO - This class is possibly a bit large to be static.
 */
public final class JsonPathUtil {

	private JsonPathUtil() {
	}

	/**
	 * Get annotation value and transform JsonProperty value to a relative JsonPath, if required.
	 * In case that either of Json annotations is not present, field name will be used on the same way
	 * like the field is annotated with JsonProperty
	 *
	 * @param jsonPathAnnotation the json path annotation, which may be null.
	 * @param jsonPropertyAnnotation the json property annotation, which may ne null.
	 * @param fieldName the field to inspect.
	 * @param isAbsolutePath is this the root, or absolute
	 * @return the resolved json path for the current field.
	 */
	public static String getJsonAnnotationValue(final JsonPath jsonPathAnnotation, final JsonProperty jsonPropertyAnnotation,
												final String fieldName, final boolean isAbsolutePath) {

		if (jsonPathAnnotation != null) {
			return jsonPathAnnotation.value();
		}

		String jsonPath;
		if (jsonPropertyAnnotation == null) {
			jsonPath = fieldName;
		} else {
			jsonPath = jsonPropertyAnnotation.value();
		}

		if (isAbsolutePath) {
			return jsonPath; //Jway ReadContext will resolve JsonProperty value as absolute path, by adding $.
		}

		//this makes JsonProperty annot value relative to parent JsonPath (or any other path)
		return "@." + jsonPath;
	}

	/**
	 * Make a string out of all elements in the json collection.
	 *
	 * @param jsonPathStack the collection of json paths.
	 * @return the string representation.
	 */
	public static String getJsonPath(final Iterable<String> jsonPathStack) {

		return Joiner.on(".").join(jsonPathStack);
	}

	/**
	 * Construct proper Json path using JsonProperty and JsonPath annotations.
	 * If parentJsonPath is empty then first JsonProperty/JsonPath will be resolved as absolute;
	 * all subsequent annotation values will be resolved as relative
	 *
	 * @param jsonPropertyAnnotation the json property annotation to inspect.
	 * @param jsonPathAnnotation the json path annotation to inspect.
	 * @param fieldName the field name.
	 * @param parentJsonPath the current relative json path.
	 * @return the relative json path of the passed field.
	 */
	public static Iterable<String> resolveRelativeJsonPaths(final JsonPath jsonPathAnnotation, final JsonProperty jsonPropertyAnnotation,
															final String fieldName, final Iterable<String> parentJsonPath) {

		String fieldJsonPath = getJsonPathFromField(jsonPathAnnotation, jsonPropertyAnnotation, fieldName);

		Iterable<String> combinedJsonPath;
		
		if (fieldJsonPath.charAt(0) == '$' && !Iterables.isEmpty(parentJsonPath)) { //Handle new absolute json path defined on field
			combinedJsonPath = makeNewRootRelativePath(fieldJsonPath);
		} else {
			combinedJsonPath = updateExistingRelativePath(parentJsonPath, fieldJsonPath);
		}
		return combinedJsonPath;
	}

	private static String getJsonPathFromField(final JsonPath jsonPathAnnotation, final JsonProperty jsonPropertyAnnotation,
											   final String fieldName) {
		if (jsonPathAnnotation != null) {
			return jsonPathAnnotation.value();
		}
		if (jsonPropertyAnnotation != null) {
			return jsonPropertyAnnotation.value();
		}
		return fieldName;
	}

	private static Iterable<String> makeNewRootRelativePath(final String jsonPathVal) {
		List<String> updatedJsonPathStack = new ArrayList<>();
		updatedJsonPathStack.add(jsonPathVal);
		return updatedJsonPathStack;
	}

	private static Iterable<String> updateExistingRelativePath(final Iterable<String> parentJsonPath, final String jsonPathVal) {
		List<String> currentJsonPath = Lists.newArrayList(parentJsonPath);
		String jsonPathPrefixRegex = "^[^.]*\\.";
		String sanitizedJsonPathValue = jsonPathVal.replaceFirst(jsonPathPrefixRegex, "");

		if (currentJsonPath.isEmpty()) { //transform first Jakson property into JsonPath root
				currentJsonPath.add("$." + sanitizedJsonPathValue);
		} else {
				currentJsonPath.add(sanitizedJsonPathValue); //all other jakson props will be simply appended
		}
		return currentJsonPath;
	}

	/**
	 * Build correct Jway Json path when provided annotation value may not be valid.
	 * E.g. paths like path @path or $path will not be resolved properly by Jway
	 *
	 * @param jsonPath current field json path.
	 * @param parentJsonPath parent json path.
	 * @return the correct json path for the current field.
	 */
	public static String buildCorrectJsonPath(final String jsonPath, final String parentJsonPath) {

		//matches @.path   $.path  .path
		if (jsonPath.matches("[@\\$]?[.].+")) {
			if (jsonPath.charAt(0) == '@') {
				return (parentJsonPath.equals("") ? "$" : parentJsonPath) + jsonPath.substring(1);
			}
			if (jsonPath.charAt(0) == '.') {
				return (parentJsonPath.equals("") ? "$" : parentJsonPath) + jsonPath;
			}
			return jsonPath;
		}

		//path like @path is invalid should be resolved as relative or absolute, depending on parent path
		if (jsonPath.charAt(0) == '@') {
			return (parentJsonPath.equals("") ? "$" : parentJsonPath) + "." + jsonPath.substring(1);
		}

		//path like $path is invalid should be resolved as absolute
		if (jsonPath.charAt(0) == '$') {
			return "$." + jsonPath.substring(1);
		}

		//any path that doesn't start with @ or $
		return (parentJsonPath.equals("") ? "$" : parentJsonPath) + "." + jsonPath;
	}
}
