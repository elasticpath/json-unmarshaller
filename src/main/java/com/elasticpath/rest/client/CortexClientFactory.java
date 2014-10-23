package com.elasticpath.rest.client;

public interface CortexClientFactory {

	CortexClient newOAuth2RestClientForToken(String authToken, String cortexTargetUrl, String cortexScope);

	CortexClient newAuthClient(String cortexTargetUrl, String cortexScope);
}
