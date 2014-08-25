package com.elasticpath.rest.sdk;

import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;
import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.client.Entity.form;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.UriBuilder.fromPath;

import java.lang.reflect.Field;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.UriBuilder;

import com.google.common.base.Joiner;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import com.elasticpath.rest.sdk.annotations.JPath;
import com.elasticpath.rest.sdk.annotations.Zoom;
import com.elasticpath.rest.sdk.annotations.Zooms;
import com.elasticpath.rest.sdk.config.JacksonProvider;
import com.elasticpath.rest.sdk.debug.Logger;
import com.elasticpath.rest.sdk.model.Auth;
import com.elasticpath.rest.sdk.model.AuthToken;

public class ClientSdk {

	private Logger logger = new Logger();

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

		String jsonResult = httpGet(targetUrl, authToken, String.class);

		logger.prettyTrace(jsonResult);

		return parseZoomResult(resultClass, jsonResult);
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

	private <T> T parseZoomResult(Class<T> resultClass,
								  String jsonResult) {
		ReadContext jsonContext = JsonPath.parse(jsonResult);
		T resultObject;
		try {
			resultObject = resultClass.newInstance();

			for (Field field : resultClass.getDeclaredFields()) {
				JPath annotation = field.getAnnotation(JPath.class);
				Object read = jsonContext.read(annotation.value());
				field.set(resultObject, String.valueOf(read));
			}
		} catch (IllegalAccessException | InstantiationException e) {
			throw new IllegalArgumentException(e);
		}
		return resultObject;
	}

	private <T> T httpGet(String targetUrl,
						  AuthToken authToken,
						  Class<T> resultClass) {
		return newClient()
				.register(JacksonProvider.class)
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
