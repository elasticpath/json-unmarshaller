package com.elasticpath.rest.client.config;

import static org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import org.codehaus.jackson.map.ObjectMapper;

import com.elasticpath.rest.client.CortexClient;
import com.elasticpath.rest.client.DefaultCortexClient;
import com.elasticpath.rest.client.url.CortexUrlFactory;
import com.elasticpath.rest.client.url.CortexUrlFactoryImpl;
/**
 * Guice configuration class.
 */
public class GuiceConfig extends AbstractModule {

	@Override
	protected void configure() {
		bind(CortexClient.class).to(DefaultCortexClient.class);
		bind(CortexUrlFactory.class).to(CortexUrlFactoryImpl.class);
	}

	@Provides
	public ObjectMapper provideObjectMapper() {
		return new ObjectMapper()
				.disable(FAIL_ON_UNKNOWN_PROPERTIES);
	}
}
