package com.elasticpath.rest.client.zoom;

public interface ZoomUrlFactory {

	String create(String baseUrl,
				  Class<?> resultClass);
}
