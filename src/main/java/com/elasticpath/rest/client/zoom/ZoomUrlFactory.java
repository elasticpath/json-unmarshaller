package com.elasticpath.rest.client.zoom;

import static javax.ws.rs.core.UriBuilder.fromPath;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class ZoomUrlFactory {

	@Inject
	private ZoomModelIntrospector zoomModelIntrospector;

	@Inject
	private ZoomQueryFactory zoomQueryFactory;

	public String create(String baseUrl,
						 Class<?> resultClass) {

		String targetUrl = baseUrl;

		if (!zoomModelIntrospector.isZoom(resultClass)) {
			return targetUrl;
		}

		String zoomQuery = zoomQueryFactory.create(
				zoomModelIntrospector.createZoomModel(resultClass)
		);

		return createZoomUrl(targetUrl, zoomQuery);
	}

	private String createZoomUrl(String href,
								 String zoomQuery) {

		return fromPath(href)
				.queryParam("zoom", zoomQuery)
				.toString();
	}
}
