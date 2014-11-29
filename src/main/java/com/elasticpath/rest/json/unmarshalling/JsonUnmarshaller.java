package com.elasticpath.rest.json.unmarshalling;

import java.io.IOException;

/**
 * Unmarshalls Json using a class annotated with
 * {@link com.elasticpath.rest.json.unmarshalling.annotations.JsonPath} and
 * {@link com.fasterxml.jackson.annotation.JsonProperty} annotations.
 */
public interface JsonUnmarshaller {

	/**
	 * Unmarshalls Json according to the {@link com.elasticpath.rest.json.unmarshalling.annotations.JsonPath} and
	 * {@link com.fasterxml.jackson.annotation.JsonProperty} annotations in the provided class.
	 *
	 * @param resultClass the class to instantiate and populate with result data.
	 * @param jsonResult the json.
	 * @param <T> the class type.
	 * @return the resulting pojo.
	 * @throws IOException
	 */
	<T> T unmarshall(Class<T> resultClass, String jsonResult) throws IOException;
}
