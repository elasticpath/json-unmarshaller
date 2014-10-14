package com.elasticpath.rest.client.url;

public interface CortexUrlFactory {

	String create(String baseUrl,
				  Class<?> resultClass);
}
