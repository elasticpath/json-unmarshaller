package com.elasticpath.rest.sdk.oauth2;

import javax.inject.Named;
import javax.inject.Singleton;

import com.elasticpath.rest.sdk.oauth2.model.OAuth2Token;

/**
 * Uses a thread local to manage a token. Do not use in a production environment, this is for test only.
 */
@Named
@Singleton
public class LocalOAuth2TokenService implements OAuth2TokenService {

	private static final ThreadLocal<OAuth2Token> token = new ThreadLocal<>();

	@Override
	public OAuth2Token getToken() {
		return LocalOAuth2TokenService.token
				.get();
	}

	@Override
	public void storeToken(OAuth2Token authToken) {
		LocalOAuth2TokenService.token
				.set(authToken);
	}
}
