package com.elasticpath.rest.sdk;

import static javax.ws.rs.client.ClientBuilder.newClient;

import javax.ws.rs.client.Client;

import com.elasticpath.rest.sdk.config.JacksonProvider;
import com.elasticpath.rest.sdk.oauth2.OAuth2ReaderInterceptor;
import com.elasticpath.rest.sdk.oauth2.OAuth2RequestFilter;
import com.elasticpath.rest.sdk.oauth2.OAuth2TokenService;
import com.elasticpath.rest.sdk.zoom.ZoomReaderInterceptor;

public class CortexClient {

	public Client newCortexClient(OAuth2TokenService tokenService) {
		return newClient()
				.register(JacksonProvider.class)
				.register(ZoomReaderInterceptor.class)
				.register(new OAuth2RequestFilter(tokenService));
	}

	public Client newAuthClient(OAuth2TokenService tokenService) {
		return newClient()
				.register(JacksonProvider.class)
				.register(new OAuth2ReaderInterceptor(tokenService));
	}
}
