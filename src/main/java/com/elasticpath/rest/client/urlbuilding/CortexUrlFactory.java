package com.elasticpath.rest.client.urlbuilding;

public interface CortexUrlFactory {

	String create(String baseUrl, Class<?> resultClass);

	String createFromAnnotationsAndResourcePath(String cortexBaseUrl, String resourcePath, Class<?> resultClass);

	String createFromResourcePath(String cortexBaseUrl, String resourcePath);

	String createFromAnnotationsAndScope(String cortexBaseUrl, String scope, Class<?> resultClass);
}
