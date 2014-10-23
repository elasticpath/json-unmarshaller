package com.elasticpath.rest.client.impl;

import static javax.ws.rs.client.ClientBuilder.newClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;

import com.elasticpath.rest.client.CortexClient;
import com.elasticpath.rest.client.CortexClientFactory;
import com.elasticpath.rest.client.deserialization.JacksonProvider;
import com.elasticpath.rest.client.oauth2.OAuth2RequestFilter;
import com.elasticpath.rest.client.oauth2.OAuth2TokenService;
import com.elasticpath.rest.client.oauth2.OAuth2TokenServiceImpl;
import com.elasticpath.rest.client.oauth2.model.OAuth2Token;
import com.elasticpath.rest.client.url.CortexUrlFactory;
import com.elasticpath.rest.client.zoom.ZoomReaderInterceptor;

@Named
@Singleton
public class CortexClientFactoryImpl implements CortexClientFactory {

	@Inject
	private JacksonProvider jacksonProvider;

	@Inject
	private ZoomReaderInterceptor zoomReaderInterceptor;

	@Inject
	private CortexUrlFactory cortexUrlFactory;

	@Override
	public CortexClient newOAuth2RestClientForToken(final String authToken, final String cortexTargetUrl, final String cortexScope) {
		OAuth2Token oAuth2Token = new OAuth2Token();
		oAuth2Token.setHeaderValue(authToken);
		OAuth2TokenService oAuth2TokenService = new OAuth2TokenServiceImpl(oAuth2Token);
		OAuth2RequestFilter oAuth2RequestFilter = new OAuth2RequestFilter(oAuth2TokenService);

		Client jaxRsClient =  newClient()
				.register(jacksonProvider)
				.register(zoomReaderInterceptor)
				.register(oAuth2RequestFilter);

		return new CortexClientImpl(jaxRsClient, cortexUrlFactory, cortexTargetUrl, cortexScope);
	}

	@Override
	public CortexClient newAuthClient(final String cortexTargetUrl, final String cortexScope) {
		Client jaxRsClient =  newClient().register(jacksonProvider);

		return new CortexClientImpl(jaxRsClient, cortexUrlFactory, cortexTargetUrl, cortexScope);
	}
}
