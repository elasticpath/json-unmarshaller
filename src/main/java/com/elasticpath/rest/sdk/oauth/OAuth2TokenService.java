package com.elasticpath.rest.sdk.oauth;

import com.elasticpath.rest.sdk.oauth.model.OAuth2Token;

public class OAuth2TokenService {

	private static final ThreadLocal<OAuth2Token> token = new ThreadLocal<>();

	public OAuth2Token getToken() {
		return OAuth2TokenService.token
				.get();
	}

	public void auth(OAuth2Token authToken) {
		OAuth2TokenService.token
				.set(authToken);
	}
}
