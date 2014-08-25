package com.elasticpath.rest.sdk;

import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;
import static javax.ws.rs.client.Entity.form;
import static javax.ws.rs.core.UriBuilder.fromPath;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.UriBuilder;

import com.google.common.base.Joiner;

import com.elasticpath.rest.sdk.annotations.Zoom;
import com.elasticpath.rest.sdk.annotations.Zooms;
import com.elasticpath.rest.sdk.oauth.model.OAuthToken;

public class ClientSdk {

	private CortexClient cortexClient = new CortexClient();

	public <T> T get(String baseUrl,
					 Class<T> resultClass) {
		String targetUrl = baseUrl;

		if (resultClass.isAnnotationPresent(Zooms.class)) {
			String zoomQuery = buildZoomQuery(resultClass);

			targetUrl = buildZoomUrl(targetUrl, zoomQuery);
		}

		return httpGet(targetUrl, resultClass);
	}

	private <T> String buildZoomQuery(Class<T> resultClass) {

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

	private <T> T httpGet(String targetUrl,
						  Class<T> resultClass) {

		return cortexClient.newCortexClient()
				.target(targetUrl)
				.request()
				.get()
				.readEntity(resultClass);
	}

	public void auth(UriBuilder targetUrl,
					 Form auth) {

		cortexClient.newAuthClient()
				.target(targetUrl)
				.request()
				.post(form(auth))
				.readEntity(OAuthToken.class);
	}
}
