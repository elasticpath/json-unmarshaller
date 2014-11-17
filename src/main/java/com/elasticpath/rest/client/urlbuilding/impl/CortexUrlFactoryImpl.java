package com.elasticpath.rest.client.urlbuilding.impl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.UriBuilder;

import com.elasticpath.rest.client.urlbuilding.CortexUrlFactory;
import com.elasticpath.rest.client.urlbuilding.annotations.EntryPointUri;
import com.elasticpath.rest.client.urlbuilding.annotations.FollowLocation;
import com.elasticpath.rest.client.urlbuilding.zoom.ZoomModelIntrospector;
import com.elasticpath.rest.client.urlbuilding.zoom.ZoomQueryFactory;


@Named
@Singleton
public class CortexUrlFactoryImpl implements CortexUrlFactory {

	@Inject
	private ZoomModelIntrospector zoomModelIntrospector;

	@Inject
	private ZoomQueryFactory zoomQueryFactory;

	@Override
	public String create(final String baseUrl, final Class<?> resultClass) {
		UriBuilder uriBuilder = UriBuilder.fromPath(baseUrl);
		addZoomParameter(resultClass, uriBuilder);
		addFollowLocationParameter(resultClass, uriBuilder);
		return uriBuilder.toString();
	}

	@Override
	public String createFromAnnotationsAndResourcePath(final String cortexBaseUrl, final String resourcePath, final Class<?> resultClass) {
		UriBuilder uriBuilder = UriBuilder.fromPath(cortexBaseUrl);
		uriBuilder.path(resourcePath);
		addZoomParameter(resultClass, uriBuilder);
		addFollowLocationParameter(resultClass, uriBuilder);
		return uriBuilder.toString();
	}

	@Override
	public String createFromResourcePath(final String cortexBaseUrl, final String resourcePath) {
		UriBuilder uriBuilder = UriBuilder.fromPath(cortexBaseUrl);
		uriBuilder.path(resourcePath);
		return uriBuilder.toString();
	}

	@Override
	public String createFromAnnotationsAndScope(final String cortexBaseUrl, final String scope, final Class<?> resultClass) {
		if (!isEntryPointUriPresent(resultClass)) {
			throw new IllegalArgumentException(EntryPointUri.class.getName() + " was expected but is missing");
		}
		UriBuilder uriBuilder = UriBuilder.fromPath(cortexBaseUrl);
		addResourceUriFromAnnotatedClass(scope, resultClass, uriBuilder);
		addZoomParameter(resultClass, uriBuilder);
		addFollowLocationParameter(resultClass, uriBuilder);
		return uriBuilder.toString();
	}

	private void addResourceUriFromAnnotatedClass(final String scope, final Class<?> resultClass, final UriBuilder uriBuilder) {
		String[] uriParts = resultClass.getAnnotation(EntryPointUri.class).value();
		for (String uriPart : uriParts) {
			if(uriPart.contains(EntryPointUri.SCOPE)) {
				uriBuilder.path(uriPart.replace(EntryPointUri.SCOPE, scope));
			} else {
				uriBuilder.path(uriPart);
			}
		}
	}

	private void addFollowLocationParameter(final Class<?> resultClass, final UriBuilder uriBuilder) {
		if (isFollowLocationPresent(resultClass)) {
			uriBuilder.queryParam(FollowLocation.PARAM_NAME, true);
		}
	}

	private void addZoomParameter(final Class<?> resultClass, final UriBuilder uriBuilder) {
		if (zoomModelIntrospector.isZoomPresent(resultClass)) {
			String zoomQuery = zoomQueryFactory.create(
					zoomModelIntrospector.createZoomModel(resultClass));
			uriBuilder.queryParam("zoom", zoomQuery);
		}
	}

	private boolean isEntryPointUriPresent(final Class<?> requestClass) {
		return requestClass.isAnnotationPresent(EntryPointUri.class);
	}

	private boolean isFollowLocationPresent(Class<?> requestClass) {
		return requestClass.isAnnotationPresent(FollowLocation.class);
	}

}
