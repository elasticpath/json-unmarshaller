package com.elasticpath.rest.sdk.config;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import javax.ws.rs.client.Client;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.elasticpath.rest.sdk.CortexClient;

@Configuration
@ComponentScan("com.elasticpath.rest")
public class SdkConfiguration {

	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper()
				.disable(FAIL_ON_UNKNOWN_PROPERTIES);
	}

	@Bean
	Client authClient(CortexClient cortexClient) {
		return cortexClient.newAuthClient();
	}

	@Bean
	Client cortexClient(CortexClient cortexClient) {
		return cortexClient.newCortexClient();
	}
}
