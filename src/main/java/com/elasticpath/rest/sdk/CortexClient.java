package com.elasticpath.rest.sdk;

import static javax.ws.rs.client.ClientBuilder.newClient;

import javax.ws.rs.client.Client;

import com.elasticpath.rest.sdk.config.JacksonProvider;
import com.elasticpath.rest.sdk.oauth.OAuth2ReaderInterceptor;
import com.elasticpath.rest.sdk.oauth.OAuth2RequestFilter;
import com.elasticpath.rest.sdk.zoom.ZoomReaderInterceptor;

public class CortexClient {

	public Client newCortexClient() {
		return newClient()
				.register(JacksonProvider.class)
				.register(ZoomReaderInterceptor.class)
				.register(OAuth2RequestFilter.class);
	}

	public Client newAuthClient() {
		return newClient()
				.register(JacksonProvider.class)
				.register(OAuth2ReaderInterceptor.class);
	}
}
