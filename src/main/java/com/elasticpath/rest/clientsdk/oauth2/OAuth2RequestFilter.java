package com.elasticpath.rest.clientsdk.oauth2;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import com.elasticpath.rest.clientsdk.oauth2.model.OAuth2Token;

@Named
@Singleton
public class OAuth2RequestFilter implements ClientRequestFilter {

	@Inject
	private OAuth2TokenService tokenService;

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {

		OAuth2Token authToken = tokenService.getToken();

		requestContext.getHeaders()
				.putSingle(
						authToken.getHeaderName(), authToken.getHeaderValue()
				);
	}
}
