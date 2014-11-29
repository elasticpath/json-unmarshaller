package com.elasticpath.rest.json.unmarshalling.config;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.elasticpath.rest.json.unmarshalling.impl.DefaultJsonUnmarshaller;

/**
 * The bundle activator, which registers the following services: jsonPathResultFactory
 **/
public class Activator implements BundleActivator {

	/**
	 * Implements BundleActivator.start().
	 * Registers the following services: jsonPathResultFactory
	 * @param context the framework context for the bundle.
	 **/
	public void start(BundleContext context)
	{
		Injector injector = Guice.createInjector(
				new GuiceConfig()
		);
		context.registerService(
				DefaultJsonUnmarshaller.class.getName(),
				injector.getInstance(DefaultJsonUnmarshaller.class),
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