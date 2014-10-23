package com.elasticpath.rest.client.impl;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.elasticpath.rest.client.CortexClient;
import com.elasticpath.rest.client.model.CortexModel;
import com.elasticpath.rest.client.url.CortexUrlFactory;

/**
 * A client for interacting with the Cortex API.
 */
public class CortexClientImpl implements CortexClient {

	private Client client;
	private CortexUrlFactory cortexUrlFactory;
	private String cortexBaseUrl;
	private String scope;

	/**
	 * Constructor.
	 * @param client the pre-configured jax-rs client.
	 * @param cortexUrlFactory the factory for creating cortex urls.
	 * @param cortexBaseUrl the cortex base url.
	 * @param scope the cortex scope.
	 */
	public CortexClientImpl(final Client client, final CortexUrlFactory cortexUrlFactory, final String cortexBaseUrl, final String scope) {
		this.client = client;
		this.cortexUrlFactory = cortexUrlFactory;
		this.cortexBaseUrl = cortexBaseUrl;
		this.scope = scope;
	}

	@Override
	public <T> T get(final Class<T> cortexModelClass) {
		String requestUrl = cortexUrlFactory.createFromAnnotationsAndScope(cortexBaseUrl, scope, cortexModelClass);
		Response response = client.target(requestUrl).request().get();
		return response.readEntity(cortexModelClass);
	}

	@Override
	public <T> T get(final String cortexUri, final Class<T> cortexModelClass) {
		String requestUrl = cortexUrlFactory.createFromAnnotationsAndResourcePath(cortexBaseUrl, cortexUri, cortexModelClass);
		Response response = client.target(requestUrl).request().get();
		return response.readEntity(cortexModelClass);
	}


	@Override
	public <T extends CortexModel> T post(final String cortexUri, final Map<String, Object> formParameters,
										  final Class<T> cortexModelClass) {
		String requestUrl = cortexUrlFactory.createFromAnnotationsAndResourcePath(cortexBaseUrl, cortexUri, cortexModelClass);
		Response response = client.target(requestUrl).request().post(Entity.entity(formParameters, MediaType.APPLICATION_JSON_TYPE));
		T cortexModel = response.readEntity(cortexModelClass);
		cortexModel.setCortexResponse(response);
		return cortexModel;
	}

	@Override
	public Response post(final String cortexUri, final Map<String, Object> formParameters) {
		String requestUrl = cortexUrlFactory.createFromResourcePath(cortexBaseUrl, cortexUri);
		return client.target(requestUrl).request().post(Entity.entity(formParameters, MediaType.APPLICATION_JSON_TYPE));
	}


	@Override
	public <T extends CortexModel> T post(final Map<String, Object> formParameters, final Class<T> cortexModelClass) {
		String requestUrl = cortexUrlFactory.createFromAnnotationsAndScope(cortexBaseUrl, scope, cortexModelClass);
		Response response = client.target(requestUrl).request().post(Entity.entity(formParameters, MediaType.APPLICATION_JSON_TYPE));
		T cortexModel = response.readEntity(cortexModelClass);
		cortexModel.setCortexResponse(response);
		return cortexModel;
	}
}
