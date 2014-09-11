package com.elasticpath.rest.sdk.oauth2;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import com.elasticpath.rest.sdk.oauth2.model.OAuth2Token;

public class OAuth2RequestFilter implements ClientRequestFilter {

	private final OAuth2TokenService tokenService;

	public OAuth2RequestFilter(final OAuth2TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {

		OAuth2Token authToken = tokenService.getToken();

		requestContext.getHeaders()
				.putSingle(
						authToken.getHeaderName(), authToken.getHeaderValue()
				);
	}
}
