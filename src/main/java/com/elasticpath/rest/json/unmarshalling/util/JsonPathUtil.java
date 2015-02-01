package com.elasticpath.rest.json.unmarshalling.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;


/**
 * Various util methods for getting/fixing Json paths, taken from annotations.
 * Some paths are handled by Jway fmwk, but not all.
 *
 */
public class JsonPathUtil {

	/**
	 * Make a string out of all elements in the json collection.
	 *
	 * @param jsonPathStack the collection of json paths.
	 * @return the string representation.
	 */
	public String getJsonPath(final Collection<String> jsonPathStack) {
		StringBuilder stringBuilder = new StringBuilder();
		String delimiter = "";
		for (String entry : jsonPathStack) {
			stringBuilder.append(delimiter);
			stringBuilder.append(entry);
			delimiter = ".";
		}
		return stringBuilder.toString();
	}

	/**
	 * Construct proper Json path using JsonProperty and JsonPath annotations.
	 * If parentJsonPath is empty then first JsonProperty/JsonPath will be resolved as absolute;
	 * all subsequent annotation values will be resolved as relative
	 *
	 * @param candidateField wraps Json annotations.
	 * @param parentJsonPath the current relative json path.
	 * @return the relative json path of the passed field.
	 */
	public Collection<String> resolveRelativeJsonPaths(final CandidateField candidateField,
			final Collection<String> parentJsonPath) {

		String fieldJsonPath = candidateField.getJsonPathFromField();

		if (fieldJsonPath.charAt(0) == '$' && !parentJsonPath.isEmpty()) { //Handle new absolute json path defined on field
			return makeNewRootRelativePath(fieldJsonPath);
		}

		return updateExistingRelativePath(parentJsonPath, fieldJsonPath);
	}

	private Collection<String> makeNewRootRelativePath(final String jsonPathVal) {
		return Arrays.asList(jsonPathVal);
	}

	private Collection<String> updateExistingRelativePath(final Collection<String> parentJsonPath, final String jsonPathVal) {
		List<String> currentJsonPath = new ArrayList<>(parentJsonPath);
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
	public String buildCorrectJsonPath(final String jsonPath, final String parentJsonPath) {

		final String correctParentJsonPath = "".equals(parentJsonPath) ? "$" : parentJsonPath;

		//matches @.path   $.path  .path
		if (jsonPath.matches("[@\\$]?[.].+")) {
			if (jsonPath.charAt(0) == '@') {
				return correctParentJsonPath + jsonPath.substring(1);
			}
			if (jsonPath.charAt(0) == '.') {
				return correctParentJsonPath + jsonPath;
			}
			return jsonPath;
		}

		//path like @path is invalid should be resolved as relative or absolute, depending on parent path
		if (jsonPath.charAt(0) == '@') {
			return correctParentJsonPath + "." + jsonPath.substring(1);
		}

		//path like $path is invalid should be resolved as absolute
		if (jsonPath.charAt(0) == '$') {
			return "$." + jsonPath.substring(1);
		}

		//any path that doesn't start with @ or $
		return correctParentJsonPath + "." + jsonPath;
	}
}
