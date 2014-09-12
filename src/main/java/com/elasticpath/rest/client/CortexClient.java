package com.elasticpath.rest.client;

import javax.ws.rs.client.Client;

public interface CortexClient {

	Client newOAuth2RestClient();

	Client newAuthClient();
}
