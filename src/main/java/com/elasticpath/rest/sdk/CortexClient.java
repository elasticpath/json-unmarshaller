package com.elasticpath.rest.sdk;

import static javax.ws.rs.client.ClientBuilder.newClient;

import javax.ws.rs.client.Client;

import com.elasticpath.rest.sdk.config.JacksonProvider;
import com.elasticpath.rest.sdk.oauth.OAuthReaderInterceptor;
import com.elasticpath.rest.sdk.oauth.OAuthRequestFilter;
import com.elasticpath.rest.sdk.zoom.ZoomReaderInterceptor;

public class CortexClient {

	public Client newCortexClient() {
		return newClient()
				.register(JacksonProvider.class)
				.register(ZoomReaderInterceptor.class)
				.register(OAuthRequestFilter.class);
	}

	public Client newAuthClient() {
		return newClient()
				.register(JacksonProvider.class)
				.register(OAuthReaderInterceptor.class);
	}
}
