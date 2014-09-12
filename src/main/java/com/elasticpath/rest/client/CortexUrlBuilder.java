package com.elasticpath.rest.client;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.UriBuilder.fromPath;

import com.google.common.base.Joiner;

import com.elasticpath.rest.client.annotations.ZoomTarget;

public class CortexUrlBuilder {

	public String get(String baseUrl,
					  Class<?> resultClass) {
		String targetUrl = baseUrl;

		if (resultClass.isAnnotationPresent(ZoomTarget.class)) {
			String zoomQuery = buildZoomQuery(resultClass);

			targetUrl = buildZoomUrl(targetUrl, zoomQuery);
		}

		return targetUrl;
	}

	private String buildZoomQuery(Class<?> resultClass) {

		Iterable<String> rawZooms = asList(
				resultClass.getAnnotation(ZoomTarget.class)
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
