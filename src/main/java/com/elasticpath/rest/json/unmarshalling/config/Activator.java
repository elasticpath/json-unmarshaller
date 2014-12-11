package com.elasticpath.rest.json.unmarshalling.config;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.elasticpath.rest.json.unmarshalling.impl.DefaultJsonUnmarshaller;

/**
 * The bundle activator, which registers the following services: jsonPathResultFactory
 **/
public class Activator implements BundleActivator {

	/**
	 * Implements BundleActivator.start().
	 * Registers the following services: jsonUnmarshaller
	 * @param context the framework context for the bundle.
	 **/
	public void start(BundleContext context)
	{
		context.registerService(
				DefaultJsonUnmarshaller.class.getName(),
				new DefaultJsonUnmarshaller(), null);
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