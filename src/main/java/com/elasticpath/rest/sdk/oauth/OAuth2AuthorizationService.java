package com.elasticpath.rest.sdk.oauth;

import static javax.ws.rs.client.Entity.form;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.UriBuilder;

import com.elasticpath.rest.sdk.CortexClient;
import com.elasticpath.rest.sdk.oauth.model.OAuth2Token;

public class OAuth2AuthorizationService {

	private CortexClient cortexClient = new CortexClient();

	public void auth(UriBuilder targetUrl,
					 Form auth) {

		cortexClient.newAuthClient()
				.target(targetUrl)
				.request()
				.post(form(auth))
				.readEntity(OAuth2Token.class);
	}
}
