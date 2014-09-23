package com.elasticpath.rest.client.oauth2;

import com.elasticpath.rest.client.oauth2.model.OAuth2Token;

/**
 * Uses a thread local to manage a token. Do not use in a production environment, this is for test only.
 */
public class OAuth2TokenServiceImpl implements OAuth2TokenService {

	private OAuth2Token token;

	public OAuth2TokenServiceImpl(OAuth2Token token) {
		this.token = token;
	}

	@Override
	public OAuth2Token getToken() {
		return token;
	}

	@Override
	public void storeToken(OAuth2Token authToken) {
		this.token = authToken;
	}
}
