package com.elasticpath.rest.client.oauth2;

import static javax.ws.rs.client.Entity.form;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Form;

import com.elasticpath.rest.client.oauth2.model.OAuth2Token;

@Named
@Singleton
public class OAuth2AuthorizationService {

	private static final String targetUrl = "??";

	@Inject
	private OAuth2TokenService oAuth2TokenService;

	public void auth(Client client,
					 Form auth) {

		OAuth2Token oAuth2Token = client
				.target(targetUrl)
				.request()
				.post(form(auth))
				.readEntity(OAuth2Token.class);

		oAuth2TokenService.storeToken(oAuth2Token);
	}
}
