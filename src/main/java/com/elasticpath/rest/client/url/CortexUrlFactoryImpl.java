package com.elasticpath.rest.client.url;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.UriBuilder;

import com.elasticpath.rest.client.annotations.FollowLocation;
import com.elasticpath.rest.client.zoom.ZoomModelIntrospector;
import com.elasticpath.rest.client.zoom.ZoomQueryFactory;

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

		if (zoomModelIntrospector.isZoomPresent(resultClass)) {
			String zoomQuery = zoomQueryFactory.create(
					zoomModelIntrospector.createZoomModel(resultClass));
			uriBuilder.queryParam("zoom", zoomQuery);
		}

		if (isFollowLocationPresent(resultClass)) {
			uriBuilder.queryParam(FollowLocation.PARAM_NAME, true);
		}

		return uriBuilder.toString();
	}

	private boolean isFollowLocationPresent(Class<?> requestClass) {
		return requestClass.isAnnotationPresent(FollowLocation.class);
	}

}
