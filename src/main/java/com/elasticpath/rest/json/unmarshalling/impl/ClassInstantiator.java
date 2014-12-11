package com.elasticpath.rest.json.unmarshalling.impl;

/**
 * Creates an instance of a given class
 */
public class ClassInstantiator {

	public <T> T newInstance(Class<T> resultClass) throws InstantiationException, IllegalAccessException {
		return resultClass.newInstance();
	}
}
