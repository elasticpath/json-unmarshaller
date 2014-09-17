package com.elasticpath.rest.client.oauth2;

import static javax.ws.rs.client.Entity.form;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import com.elasticpath.rest.client.oauth2.model.OAuth2Token;

@Named
@Singleton
public class OAuth2AuthorizationService {

	private Logger logger = getLogger(OAuth2AuthorizationService.class);

	private static final String AUTH_URL = "http://localhost:9080/cortex/oauth2/tokens";

	@Inject
	private OAuth2TokenService oAuth2TokenService;

	public void auth(Client client,
					 Form auth) {

		Response authResponse = client
				.target(AUTH_URL)
				.request()
				.post(form(auth));

		if (authResponse.getStatusInfo()
				.getFamily() == SUCCESSFUL) {
			OAuth2Token oAuth2Token = authResponse
					.readEntity(OAuth2Token.class);
			oAuth2TokenService.storeToken(oAuth2Token);
		} else {
			String errorResponse = authResponse.readEntity(String.class);
			logger.error("error: " + errorResponse);
		}
	}
}
