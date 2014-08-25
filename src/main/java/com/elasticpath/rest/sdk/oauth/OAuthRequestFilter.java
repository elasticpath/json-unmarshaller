package com.elasticpath.rest.sdk.oauth;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import com.elasticpath.rest.sdk.oauth.model.OAuthToken;

public class OAuthRequestFilter implements ClientRequestFilter {

	private final OAuthTokenService tokenService = new OAuthTokenService();

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {

		OAuthToken authToken = tokenService.getToken();

		requestContext.getHeaders()
				.putSingle(
						authToken.getHeaderName(), authToken.getHeaderValue()
				);
	}
}
