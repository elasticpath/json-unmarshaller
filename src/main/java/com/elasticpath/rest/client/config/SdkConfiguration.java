package com.elasticpath.rest.client.config;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import javax.ws.rs.client.Client;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.elasticpath.rest.client.DefaultCortexClient;

@Configuration
@ComponentScan("com.elasticpath.rest")
public class SdkConfiguration {

	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper()
				.disable(FAIL_ON_UNKNOWN_PROPERTIES);
	}

	@Bean
	Client authClient(DefaultCortexClient cortexClient) {
		return cortexClient.newAuthClient();
	}

	@Bean
	Client cortexClient(DefaultCortexClient cortexClient) {
		return cortexClient.newOAuth2RestClient();
	}
}
