package com.elasticpath.rest.client.oauth2;

import com.elasticpath.rest.client.oauth2.model.OAuth2Token;

/**
 * OAuth2Token Service.
 */
public interface OAuth2TokenService {

	/**
	 * Retrieves an Oauth2Token.
	 *
	 * @return the {@link com.elasticpath.rest.client.oauth2.model.OAuth2Token}
	 */
	public OAuth2Token getToken();

	/**
	 * Stores the Oauth2Token.
	 *
	 * @param authToken the {@link com.elasticpath.rest.client.oauth2.model.OAuth2Token}
	 */
	public void storeToken(OAuth2Token authToken);
}
