package com.elasticpath.rest.client

import static org.junit.Assert.assertNotNull

import org.junit.Test

import com.google.inject.Guice
import com.google.inject.Injector

import com.elasticpath.rest.client.config.GuiceConfig
import com.elasticpath.rest.client.zoom.ZoomUrlFactory

class WiringTest {


	@Test
	void 'Wiring test'() {
		Injector injector = Guice.createInjector(new GuiceConfig());
		CortexClient cortexClient = injector.getInstance(CortexClient.class);
		ZoomUrlFactory zoomUrlFactory = injector.getInstance(ZoomUrlFactory.class);
		assertNotNull(cortexClient);
		assertNotNull(zoomUrlFactory);
	}
}
