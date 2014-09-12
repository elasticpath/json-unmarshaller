package com.elasticpath.rest.client.oauth2;

import static javax.ws.rs.client.Entity.form;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.UriBuilder;

import com.elasticpath.rest.client.DefaultCortexClient;
import com.elasticpath.rest.client.oauth2.model.OAuth2Token;

@Named
@Singleton
public class OAuth2AuthorizationService {

	@Inject
	private DefaultCortexClient cortexClient;

	public OAuth2Token auth(UriBuilder targetUrl,
							Form auth) {

		return cortexClient.newAuthClient()
				.target(targetUrl)
				.request()
				.post(form(auth))
				.readEntity(OAuth2Token.class);
	}
}
