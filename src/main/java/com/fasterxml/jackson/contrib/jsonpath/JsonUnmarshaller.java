package com.fasterxml.jackson.contrib.jsonpath;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unmarshals Json into a class annotated with
 * {@link com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath} and
 * {@link com.fasterxml.jackson.annotation.JsonProperty} annotations.
 */
public interface JsonUnmarshaller {

	/**
	 * Unmarshals Json according to the {@link com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath} and
	 * {@link com.fasterxml.jackson.annotation.JsonProperty} annotations in the provided class.
	 *
	 * @param resultClass the class to instantiate and populate with result data.
	 * @param json the json.
	 * @param <T> the class type.
	 * @return the resulting pojo.
	 * @throws IOException if something goes wrong with unmarshalling.
	 */
	<T> T unmarshal(Class<T> resultClass, String json) throws IOException;

	/**
	 * Checks to see if a class is valid for json unmarshalling.
	 *
	 * @param resultClass the class to inspect.
	 * @param <T> the class type.
	 * @return true if class is valid for unmarshalling.
	 */
	<T> boolean isSuitableForUnmarshalling(Class<T> resultClass);

	/**
	 * Set a custom ObjectMapper to use during unmarshalling.
	 * @param objectMapper the object mapper.
	 */
	void setObjectMapper(ObjectMapper objectMapper);
}
