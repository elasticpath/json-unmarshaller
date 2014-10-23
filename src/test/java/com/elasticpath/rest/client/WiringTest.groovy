package com.elasticpath.rest.client

import static org.junit.Assert.assertNotNull

import org.junit.Test

import com.google.inject.Guice
import com.google.inject.Injector

import com.elasticpath.rest.client.config.GuiceConfig
import com.elasticpath.rest.client.url.CortexUrlFactory

class WiringTest {


	@Test
	void 'Wiring test'() {
		Injector injector = Guice.createInjector(new GuiceConfig());
		CortexClientFactory cortexClient = injector.getInstance(CortexClientFactory.class);
		CortexUrlFactory cortexUrlFactory = injector.getInstance(CortexUrlFactory.class);
		assertNotNull(cortexClient);
		assertNotNull(cortexUrlFactory);
	}
}
