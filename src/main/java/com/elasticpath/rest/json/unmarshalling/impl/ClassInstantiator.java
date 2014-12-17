package com.elasticpath.rest.json.unmarshalling.impl;

/**
 * Generified way of instanciating a class.
 */
public class ClassInstantiator {

	/**
	 * Generified way of instanciating a class.
	 *
	 * @param resultClass the class to instanciate
	 * @param <T> the type of the class
	 * @return the object
	 * @throws InstantiationException on error
	 * @throws IllegalAccessException on error
	 */
	public <T> T newInstance(final Class<T> resultClass) throws InstantiationException, IllegalAccessException {
		return resultClass.newInstance();
	}
}
