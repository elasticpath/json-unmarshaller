package com.elasticpath.rest.client.oauth2;

import javax.inject.Named;
import javax.inject.Singleton;

import com.elasticpath.rest.client.oauth2.model.OAuth2Token;

/**
 * Uses a thread local to manage a token. Do not use in a production environment, this is for test only.
 */
@Named
@Singleton
public class LocalOAuth2TokenService implements OAuth2TokenService {

	private static final ThreadLocal<OAuth2Token> LOCAL_AUTH_TOKEN = new ThreadLocal<>();

	@Override
	public OAuth2Token getToken() {
		return LocalOAuth2TokenService.LOCAL_AUTH_TOKEN
				.get();
	}

	@Override
	public void storeToken(OAuth2Token authToken) {
		LocalOAuth2TokenService.LOCAL_AUTH_TOKEN
				.set(authToken);
	}
}
