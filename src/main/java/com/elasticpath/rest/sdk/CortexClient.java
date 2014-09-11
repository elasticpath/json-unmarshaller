package com.elasticpath.rest.sdk;

import static javax.ws.rs.client.ClientBuilder.newClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;

import com.elasticpath.rest.sdk.config.JacksonProvider;
import com.elasticpath.rest.sdk.oauth2.OAuth2ReaderInterceptor;
import com.elasticpath.rest.sdk.oauth2.OAuth2RequestFilter;
import com.elasticpath.rest.sdk.zoom.ZoomReaderInterceptor;

@Named
@Singleton
public class CortexClient {

	@Inject
	private JacksonProvider jacksonProvider;

	@Inject
	private OAuth2RequestFilter oAuth2RequestFilter;

	@Inject
	private OAuth2ReaderInterceptor oAuth2ReaderInterceptor;

	@Inject
	private ZoomReaderInterceptor zoomReaderInterceptor;

	public Client newCortexClient() {

		return newClient()
				.register(jacksonProvider)
				.register(zoomReaderInterceptor)
				.register(oAuth2RequestFilter);
	}

	public Client newAuthClient() {
		return newClient()
				.register(jacksonProvider)
				.register(oAuth2ReaderInterceptor);
	}
}
