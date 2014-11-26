package com.elasticpath.rest.client.config;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.elasticpath.rest.client.unmarshalling.JacksonProvider;
import com.elasticpath.rest.client.urlbuilding.CortexUrlFactory;
import com.elasticpath.rest.client.unmarshalling.JsonUnmarshallReaderInterceptor;

/**
 * The bundle activator, which registers the following services: cortexUrlFactory, jacksonProvider, jsonUnmarshallReaderInterceptor.
 **/
public class Activator implements BundleActivator {

	/**
	 * Implements BundleActivator.start().
	 * Registers the following services: cortexUrlFactory, jacksonProvider, jsonUnmarshallReaderInterceptor.
	 * @param context the framework context for the bundle.
	 **/
	public void start(BundleContext context)
	{
		Injector injector = Guice.createInjector(
				new GuiceConfig()
		);
		register(context, injector, CortexUrlFactory.class);
		register(context, injector, JacksonProvider.class);
		register(context, injector, JsonUnmarshallReaderInterceptor.class);
	}

	private void register(BundleContext context,
						  Injector injector,
						  Class<?> clazz) {
		context.registerService(
				clazz.getName(),
				injector.getInstance(clazz),
				null
		);
	}

	/**
	 * Implements BundleActivator.stop(). Does nothing since
	 * the framework will automatically unregister any registered services.
	 * @param context the framework context for the bundle.
	 **/
	public void stop(BundleContext context)
	{
		// NOTE: The services are automatically unregistered.
	}

}