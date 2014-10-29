package com.elasticpath.rest.client;

import java.util.Map;

import javax.ws.rs.core.Response;

import com.elasticpath.rest.client.impl.CortexResponse;

/**
 * A client for interacting with the Cortex API.
 */
public interface CortexClient {

	/**
	 * Perform a GET operation on a Cortex resource. Expects a model object that has been annotated with Cortex resource access
	 * information. Should also contain variable annotations that dictate the JSON unmarshalling instructions.
	 *
	 * @param cortexModelClass the model class containing the resource access annotations.
	 * @return the model object populated with Cortex response data.
	 */
	<T> T get(Class<T> cortexModelClass);

	/**
	 * Perform a GET operation on a Cortex resource. Expects a model object that has been annotated with Cortex resource access
	 * information. Should also contain variable annotations that dictate the JSON unmarshalling instructions.
	 *
	 * @param cortexUri optionally contains the path to the resource, which will take precedence over any class level annotations that
	 *                     also provide the resource path.
	 * @param cortexModelClass the model class containing the resource access annotations.
	 * @return the model object populated with Cortex response data.
	 */
	<T> T get(String cortexUri, Class<T> cortexModelClass);

	/**
	 * Perform a POST operation on a Cortex resource. This does not permit a follow location (or re-direct), it returns the POST response.
	 *
	 * @param cortexUri contains the resource uri, which will take precedence over any class level annotations that provide the resource uri.
	 * @param formParameters the map containing the values to be posted.
	 * @return the Cortex response data.
	 */
	Response post(String cortexUri, Map<String, ?> formParameters);

	/**
	 * Perform a POST operation on a Cortex resource. Expects a model object that has been annotated with Cortex resource access
	 * information. May also contain variable annotations that dictate the JSON unmarshalling instructions if followLocation
	 * has been specified.
	 *
	 * @param formParameters the map containing the values to be posted.
	 * @param cortexModelClass the model class containing the resource access annotations.
	 * @return the model object populated with Cortex response data.
	 */
	<T extends CortexResponse> T post(Map<String, ?> formParameters, Class<T> cortexModelClass);

	/**
	 * Perform a POST operation on a Cortex resource. Expects a model object that has been annotated with Cortex resource access
	 * information to provide query parameters to be added to the provided cortexUri.
	 * May also contain variable annotations that dictate the JSON unmarshalling instructions if followLocation has been specified.
	 *
	 * @param cortexUri contains the resource uri, which will take precedence over any class level annotations that provide the resource uri.
	 * @param formParameters the map containing the values to be posted.
	 * @param cortexModelClass the model class containing the resource access annotations.
	 * @return the model object populated with Cortex response data.
	 */
	<T extends CortexResponse> T post(String cortexUri, Map<String, ?> formParameters, Class<T> cortexModelClass);

	/**
	 * Perform a PUT operation on a Cortex resource.
	 *
	 * @param cortexUri contains the resource uri.
	 * @param formParameters the map containing the values to be put.
	 * @return the Cortex response data.
	 */
	Response put(String cortexUri, Map<String, ?> formParameters);

	/**
	 * Perform a DELETE operation on a Cortex resource.
	 *
	 * @param cortexUri contains the resource uri.
	 * @return the model object populated with Cortex response data.
	 */
	Response delete(String cortexUri);

}
