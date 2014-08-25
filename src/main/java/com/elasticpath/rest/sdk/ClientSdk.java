package com.elasticpath.rest.sdk;

import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;
import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.client.Entity.form;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.UriBuilder.fromPath;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.UriBuilder;

import com.google.common.base.Joiner;

import com.elasticpath.rest.sdk.annotations.Zoom;
import com.elasticpath.rest.sdk.annotations.Zooms;
import com.elasticpath.rest.sdk.config.JacksonProvider;
import com.elasticpath.rest.sdk.model.Auth;
import com.elasticpath.rest.sdk.model.AuthToken;
import com.elasticpath.rest.sdk.zoom.ZoomReaderInterceptor;

public class ClientSdk {

	public <T> T get(String targetUrl,
					 AuthToken authToken,
					 Class<T> resultClass) {

		if (resultClass.isAnnotationPresent(Zooms.class)) {
			return zoom(targetUrl, authToken, resultClass);
		}

		return httpGet(targetUrl, authToken, resultClass);
	}

	private <T> T zoom(String href,
					   AuthToken authToken,
					   Class<T> resultClass) {
		String zoomQuery = buildZoomQuery(resultClass);

		String targetUrl = buildZoomUrl(href, zoomQuery);

		return httpGet(targetUrl, authToken, resultClass);
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
						  AuthToken authToken,
						  Class<T> resultClass) {
		return newClient()
				.register(JacksonProvider.class)
				.register(ZoomReaderInterceptor.class)
				.target(targetUrl)
				.request(APPLICATION_JSON_TYPE)
				.header(authToken.getHeaderName(), authToken.getHeaderValue())
				.get()
				.readEntity(resultClass);
	}

	public AuthToken auth(UriBuilder targetUrl,
						  Form auth) {

		Auth accessToken = newClient()
				.register(JacksonProvider.class)
				.target(targetUrl)
				.request(APPLICATION_JSON_TYPE)
				.post(form(auth))
				.readEntity(Auth.class);

		return new AuthToken(accessToken.getAccessToken());
	}
}
