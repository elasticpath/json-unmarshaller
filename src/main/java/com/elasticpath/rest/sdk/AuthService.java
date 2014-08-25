package com.elasticpath.rest.sdk;

import static javax.ws.rs.client.Entity.form;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.UriBuilder;

import com.elasticpath.rest.sdk.oauth.model.OAuthToken;

public class AuthService {

	private CortexClient cortexClient = new CortexClient();

	public void auth(UriBuilder targetUrl,
					 Form auth) {

		cortexClient.newAuthClient()
				.target(targetUrl)
				.request()
				.post(form(auth))
				.readEntity(OAuthToken.class);
	}
}
