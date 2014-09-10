package com.elasticpath.rest.sdk;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.UriBuilder.fromPath;

import com.google.common.base.Joiner;

import com.elasticpath.rest.sdk.annotations.Zoom;

public class CortexUrlBuilder {

	public String get(String baseUrl,
					  Class<?> resultClass) {
		String targetUrl = baseUrl;

		if (resultClass.isAnnotationPresent(Zoom.class)) {
			String zoomQuery = buildZoomQuery(resultClass);

			targetUrl = buildZoomUrl(targetUrl, zoomQuery);
		}

		return targetUrl;
	}

	private String buildZoomQuery(Class<?> resultClass) {

		Iterable<String> rawZooms = asList(
				resultClass.getAnnotation(Zoom.class)
						.value()
		);

		return Joiner.on(":")
				.join(rawZooms);
	}

	private String buildZoomUrl(String href,
								String zoomQuery) {
		return fromPath(href)
				.queryParam("zoom", zoomQuery)
				.toString();
	}
}
