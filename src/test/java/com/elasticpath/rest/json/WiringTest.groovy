package com.elasticpath.rest.json

import org.junit.Ignore

import static org.junit.Assert.assertNotNull

import org.junit.Test

import com.google.inject.Guice
import com.google.inject.Injector

import com.elasticpath.rest.json.unmarshalling.config.GuiceConfig
import com.elasticpath.rest.json.unmarshalling.JsonUnmarshaller

class WiringTest {


	@Test
	@Ignore
	void 'Wiring test'() {
		Injector injector = Guice.createInjector(new GuiceConfig());
		JsonUnmarshaller jsonUnmarshaller = injector.getInstance(JsonUnmarshaller.class);
		assertNotNull(jsonUnmarshaller);
	}
}
