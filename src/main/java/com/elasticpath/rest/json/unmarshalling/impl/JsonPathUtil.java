package com.elasticpath.rest.json.unmarshalling.impl;

import java.util.Deque;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;

/**
 * Various util methods for getting/fixing Json paths, taken from annotations.
 * Some paths are handled by Jway fmwk, but not all.
 *
 */
public class JsonPathUtil {

	private JsonPathUtil(){}

	/**
	 * Get annotation value and transform JsonProperty value to a relative JsonPath, if required
	 * In case that either of Json annotations is not present, field name will be used on the same way
	 * like the field is annotated with JsonProperty
	 *
	 * @param jsonPathAnnotation
	 * @param jsonPropertyAnnotation
	 * @param fieldName
	 * @param isAbsolutePath
	 * @return
	 */
	public static String getJsonAnnotationValue(final JsonPath jsonPathAnnotation, final JsonProperty jsonPropertyAnnotation,
										  		final String fieldName, final boolean isAbsolutePath){

		if (jsonPathAnnotation != null) {
			return jsonPathAnnotation.value();
		}

		String jsonPath;
		if (jsonPropertyAnnotation == null){
			jsonPath = fieldName;
		}else{
			jsonPath = jsonPropertyAnnotation.value();
		}

		if (isAbsolutePath){
			return jsonPath; //Jway ReadContext will resolve JsonProperty value as absolute path, by adding $.
		}

		//this makes JsonProperty annot value relative to parent JsonPath (or any other path)
		return  "@." + jsonPath;
	}

	/**
	 * Make a string out of all deque elements
	  *
	 * @param jsonPathStack
	 * @return
	 */
	public static String getJsonPath(final Deque<String> jsonPathStack){

		return Joiner.on("").join(jsonPathStack);
	}

	/**
	 * Construct proper Json path using JsonProperty and JsonPath annotations;
	 * If jsonPathStack is empty then first JsonProperty/JsonPath will be resolved as absolute;
	 * all subsequent annotation values will be resolved as relative
	 *
	 * @param jsonPropertyAnnotation
	 * @param jsonPathAnnotation
	 * @param jsonPathStack
	 * @return
	 */
	public static Deque<String> resolveRelativeJsonPaths(final JsonPath jsonPathAnnotation,final JsonProperty jsonPropertyAnnotation,
												   		 final String fieldName, Deque<String> jsonPathStack){

		String jsonPathVal;

		if (jsonPathAnnotation!=null){
			jsonPathVal = jsonPathAnnotation.value();
		}else if (jsonPropertyAnnotation != null){
			jsonPathVal = jsonPropertyAnnotation.value();
		}else{
			jsonPathVal = fieldName;
		}

		if (jsonPropertyAnnotation != null){//handle jakson propery annotations
			if (jsonPathStack.isEmpty()){//transform first Jakson property into JsonPath root
				jsonPathStack.add("$." + jsonPathVal);
			}else {
				jsonPathStack.add("." + jsonPathVal);//all other jakson props will be simply appended
			}

		}else if (jsonPathVal.charAt(0) == '@'){//@.property
			if (jsonPathStack.isEmpty()){
				jsonPathStack.add(jsonPathVal.replaceFirst("@", "\\$"));
			}else {
				jsonPathStack.add(jsonPathVal.substring(1));
			}
		}else if (jsonPathVal.charAt(0) == '$' && !jsonPathStack.isEmpty()) {
			jsonPathStack = new LinkedList<>();
			jsonPathStack.add(jsonPathVal);
		}else{
			jsonPathStack.add(jsonPathVal);
		}

		return jsonPathStack;
	}

	/**
	 * Build correct Jway Json path when provided annotation value may not be valid
	 * E.g. paths like path @path or $path will not be resolved properly by Jway
	 *
	 * @param jsonPath
	 * @param parentJsonPath
	 * @return
	 */
	public static String buildCorrectJsonPath(String jsonPath, final String parentJsonPath){

		//matches @.path   $.path  .path
		if (jsonPath.matches("[@\\$]?[.].+")){
			if (jsonPath.charAt(0) == '@') {
				return (parentJsonPath.equals("") ? "$" : parentJsonPath) + jsonPath.substring(1);
			}
			if (jsonPath.charAt(0) == '.') {
				return (parentJsonPath.equals("") ? "$" : parentJsonPath) + jsonPath;
			}
			return jsonPath;
		}

		//path like @path is invalid should be resolved as relative or absolute, depending on parent path
		if (jsonPath.charAt(0) == '@'){
			return (parentJsonPath.equals("") ? "$" : parentJsonPath) + "." + jsonPath.substring(1);
		}

		//path like $path is invalid should be resolved as absolute
		if (jsonPath.charAt(0) == '$'){
			return  "$." + jsonPath.substring(1);
		}

		//any path that doesn't start with @ or $
		return (parentJsonPath.equals("") ? "$" : parentJsonPath) + "." + jsonPath;
	}
}
