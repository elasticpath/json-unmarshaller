package com.elasticpath.rest.sdk.oauth;

import com.elasticpath.rest.sdk.oauth.model.OAuthToken;

public class OAuthTokenService {

	private static final ThreadLocal<OAuthToken> token = new ThreadLocal<>();

	public OAuthToken getToken() {
		return OAuthTokenService.token
				.get();
	}

	public void auth(OAuthToken authToken) {
		OAuthTokenService.token
				.set(authToken);
	}
}
