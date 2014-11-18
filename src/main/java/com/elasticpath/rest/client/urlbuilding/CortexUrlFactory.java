package com.elasticpath.rest.client.urlbuilding;

public interface CortexUrlFactory {


	/**
	 * Adds query parameters defined in the request class to the Cortex Resource Url.
	 *
	 * @param resourceUrl the fully qualified Cortex Resource URL.
	 * @param requestClass the query-parameter-annotated request class.
	 * @return the fully-qualified Cortex resource URL.
	 */
	String addQueryParametersToResourceUrl(String resourceUrl, Class<?> requestClass);

	/**
	 * Creates the fully qualified Cortex Resource URL with query parameters derived from the request class.
	 *
	 * @param cortexBaseUrl the base url of the Cortex server.
	 * @param resourcePath the uri of, or path ot the Cortex Resource.
	 * @return the fully-qualified Cortex resource URL.
	 */
	String createResourceUrlWithQueryParameters(String cortexBaseUrl, String resourcePath, Class<?> requestClass);

	/**
	 * Creates the fully qualified Cortex Resource URL with no query parameters.
	 *
	 * @param cortexBaseUrl the base url of the Cortex server.
	 * @param resourcePath the uri of, or path ot the resource.
	 * @return the fully-qualified Cortex resource URL.
	 */
	String createResourceUrlWithNoQueryParameters(String cortexBaseUrl, String resourcePath);

	/**
	 * Creates the fully qualified Cortex Resource URL to an entry point resource using
	 * {@link com.elasticpath.rest.client.urlbuilding.annotations.EntryPointUri}.
	 * The supplied request class must contain the @EntryPointUri annotation. The @Zoom and @FollowLocation annotations may also be
	 * used in to add query parameters.
	 *
	 * @param cortexBaseUrl the base url of the Cortex server.
	 * @param scope the store code, or Cortex scope.
	 * @param requestClass the annotated request class.
	 * @return the fully-qualified Cortex resource URL.
	 */
	String createEntryPointResourceUrl(String cortexBaseUrl, String scope, Class<?> requestClass);
}
