package com.elasticpath.rest.client.unmarshalling;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class ClassInstantiator {

	public <T> T newInstance(Class<T> resultClass) throws InstantiationException, IllegalAccessException {
		return resultClass.newInstance();
	}
}
