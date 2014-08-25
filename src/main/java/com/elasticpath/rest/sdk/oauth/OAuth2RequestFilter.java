package com.elasticpath.rest.sdk.oauth;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import com.elasticpath.rest.sdk.oauth.model.OAuth2Token;

public class OAuth2RequestFilter implements ClientRequestFilter {

	private final OAuth2TokenService tokenService = new OAuth2TokenService();

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {

		OAuth2Token authToken = tokenService.getToken();

		requestContext.getHeaders()
				.putSingle(
						authToken.getHeaderName(), authToken.getHeaderValue()
				);
	}
}
