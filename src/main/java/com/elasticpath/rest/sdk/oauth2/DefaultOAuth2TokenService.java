package com.elasticpath.rest.sdk.oauth2;

import com.elasticpath.rest.sdk.oauth2.model.OAuth2Token;

public class DefaultOAuth2TokenService implements OAuth2TokenService {

	private static final ThreadLocal<OAuth2Token> token = new ThreadLocal<>();

	@Override
	public OAuth2Token getToken() {
		return DefaultOAuth2TokenService.token
				.get();
	}

	@Override
	public void storeToken(OAuth2Token authToken) {
		DefaultOAuth2TokenService.token
				.set(authToken);
	}
}
