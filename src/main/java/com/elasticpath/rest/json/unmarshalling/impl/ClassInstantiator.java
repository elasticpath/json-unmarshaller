package com.elasticpath.rest.json.unmarshalling.impl;

public class ClassInstantiator {

	public <T> T newInstance(final Class<T> resultClass) throws InstantiationException, IllegalAccessException {
		return resultClass.newInstance();
	}
}
