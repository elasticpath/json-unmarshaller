package com.elasticpath.rest.client.impl;

import javax.ws.rs.core.Response;

/**
 * Holds the response to a Cortex API call.
 */
public interface CortexResponse {

	/**
	 * Holds the response received from Cortex.
	 * @return the response object.
	 */
	Response getResponse();

	/**
	 * Sets the response from Cortex.
	 * @param response the response object.
	 */
	void setResponse(Response response);

}
