package com.elasticpath.rest.sdk;

import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;
import static javax.ws.rs.core.UriBuilder.fromPath;

import com.google.common.base.Joiner;

import com.elasticpath.rest.sdk.annotations.Zoom;
import com.elasticpath.rest.sdk.annotations.Zooms;

public class CortexUrlBuilder {

	public String get(String baseUrl,
					  Class<?> resultClass) {
		String targetUrl = baseUrl;

		if (resultClass.isAnnotationPresent(Zooms.class)) {
			String zoomQuery = buildZoomQuery(resultClass);

			targetUrl = buildZoomUrl(targetUrl, zoomQuery);
		}

		return targetUrl;
	}

	private String buildZoomQuery(Class<?> resultClass) {

		Iterable<Zoom> rawZooms = asList(
				resultClass.getAnnotation(Zooms.class)
						.value()
		);

		Iterable<String> joinedZooms = transform(
				rawZooms,
				z -> Joiner.on(":")
						.join(z.value())
		);

		return Joiner.on(",")
				.join(joinedZooms);
	}

	private String buildZoomUrl(String href,
								String zoomQuery) {
		return fromPath(href)
				.queryParam("zoom", zoomQuery)
				.toString();
	}
}
