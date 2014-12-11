package com.elasticpath.rest.json.unmarshalling.config;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import com.elasticpath.rest.json.unmarshalling.impl.DefaultJsonUnmarshaller;
import com.elasticpath.rest.json.unmarshalling.JsonUnmarshaller;

/**
 * Guice configuration class.
 */
@Deprecated
public class GuiceConfig extends AbstractModule {

	@Override
	protected void configure() {
		bind(JsonUnmarshaller.class).to(DefaultJsonUnmarshaller.class);
	}

	@Provides
	public ObjectMapper provideObjectMapper() {
		return new ObjectMapper()
				.disable(FAIL_ON_UNKNOWN_PROPERTIES);
	}
}
