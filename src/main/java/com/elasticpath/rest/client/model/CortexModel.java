package com.elasticpath.rest.client.model;

import javax.ws.rs.core.Response;

/**
 * A Cortex model object that contains annotations dictating both Cortex access and data unmarshalling requirements.
 */
public interface CortexModel {

	/**
	 * Holds the response received from Cortex.
	 * @return the response object.
	 */
	Response getCortexResponse();

	/**
	 * Sets the response from Cortex.
	 * @param response the response object.
	 */
	void setCortexResponse(Response response);

}
