package com.elasticpath.rest.client;

import static javax.ws.rs.client.ClientBuilder.newClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;

import com.elasticpath.rest.client.config.JacksonProvider;
import com.elasticpath.rest.client.oauth2.OAuth2RequestFilter;
import com.elasticpath.rest.client.zoom.ZoomReaderInterceptor;

@Named
@Singleton
public class DefaultCortexClient implements CortexClient {

	@Inject
	private JacksonProvider jacksonProvider;

	@Inject
	private OAuth2RequestFilter oAuth2RequestFilter;

	@Inject
	private ZoomReaderInterceptor zoomReaderInterceptor;

	@Override
	public Client newOAuth2RestClient() {

		return newClient()
				.register(jacksonProvider)
				.register(zoomReaderInterceptor)
				.register(oAuth2RequestFilter);
	}

	@Override
	public Client newAuthClient() {
		return newClient()
				.register(jacksonProvider);
	}

}
