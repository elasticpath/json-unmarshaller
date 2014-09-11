package com.elasticpath.rest.sdk.oauth;

import com.elasticpath.rest.sdk.oauth.model.OAuth2Token;

/**
 * OAuth2Token Service.
 */
public interface OAuth2TokenService {
	/**
	 * Retrieves an Oauth2Token.
	 *
	 * @return the {@link com.elasticpath.rest.sdk.oauth.model.OAuth2Token}
	 */
	public OAuth2Token getToken();

	/**
	 * Stores the Oauth2Token.
	 *
	 * @param authToken the {@link com.elasticpath.rest.sdk.oauth.model.OAuth2Token}
	 */
	public void storeToken(OAuth2Token authToken);
}
