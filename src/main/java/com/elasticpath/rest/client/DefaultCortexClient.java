package com.elasticpath.rest.client;

import static javax.ws.rs.client.ClientBuilder.newClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;

import com.elasticpath.rest.client.config.JacksonProvider;
import com.elasticpath.rest.client.oauth2.OAuth2RequestFilter;
import com.elasticpath.rest.client.oauth2.OAuth2TokenService;
import com.elasticpath.rest.client.oauth2.OAuth2TokenServiceImpl;
import com.elasticpath.rest.client.oauth2.model.OAuth2Token;
import com.elasticpath.rest.client.zoom.ZoomReaderInterceptor;

@Named
@Singleton
public class DefaultCortexClient implements CortexClient {

	@Inject
	private JacksonProvider jacksonProvider;



	@Inject
	private ZoomReaderInterceptor zoomReaderInterceptor;


	@Override
	public Client newOAuth2RestClientForToken(String authToken) {
		OAuth2Token oAuth2Token = new OAuth2Token();
		oAuth2Token.setHeaderValue(authToken);
		OAuth2TokenService oAuth2TokenService = new OAuth2TokenServiceImpl(oAuth2Token);
		OAuth2RequestFilter oAuth2RequestFilter = new OAuth2RequestFilter(oAuth2TokenService);

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
