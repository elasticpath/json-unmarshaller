package com.elasticpath.rest.clientsdk;

import javax.ws.rs.client.Client;

public interface CortexClient {

	Client newCortexClient();

	Client newAuthClient();
}
