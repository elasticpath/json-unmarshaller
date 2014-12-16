package com.elasticpath.rest.json.unmarshalling.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.elasticpath.rest.json.unmarshalling.impl.ClassInstantiator;
import com.elasticpath.rest.json.unmarshalling.impl.DefaultJsonUnmarshaller;
import com.elasticpath.rest.json.unmarshalling.impl.JsonAnnotationsModelIntrospector;

/**
 * The bundle activator, which registers the following services: jsonPathResultFactory.
 **/
public class Activator implements BundleActivator {

	/**
	 * Implements BundleActivator.start().
	 * Registers the following services: jsonPathResultFactory
	 * @param context the framework context for the bundle.
	 **/
	public void start(final BundleContext context) {
		context.registerService(
				DefaultJsonUnmarshaller.class.getName(),
				new DefaultJsonUnmarshaller(new ClassInstantiator(),
											new ObjectMapper(),
											new JsonAnnotationsModelIntrospector()),
				null);
	}

	/**
	 * Implements BundleActivator.stop(). Does nothing since
	 * the framework will automatically unregister any registered services.
	 * @param context the framework context for the bundle.
	 **/
	public void stop(final BundleContext context) {
		// NOTE: The services are automatically unregistered.
	}

}