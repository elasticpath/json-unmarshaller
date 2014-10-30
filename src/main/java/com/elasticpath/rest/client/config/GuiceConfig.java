package com.elasticpath.rest.client.config;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;


import com.elasticpath.rest.client.url.CortexUrlFactory;
import com.elasticpath.rest.client.url.CortexUrlFactoryImpl;

/**
 * Guice configuration class.
 */
public class GuiceConfig extends AbstractModule {

	@Override
	protected void configure() {
		bind(CortexUrlFactory.class).to(CortexUrlFactoryImpl.class);
	}

	@Provides
	public ObjectMapper provideObjectMapper() {
		return new ObjectMapper()
				.disable(FAIL_ON_UNKNOWN_PROPERTIES);
	}
}
