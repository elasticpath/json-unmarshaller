package com.elasticpath.rest.sdk;

import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.client.Entity.form;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.UriBuilder.fromPath;

import java.lang.reflect.Field;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.google.common.base.Joiner;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import com.elasticpath.rest.sdk.annotations.Json;
import com.elasticpath.rest.sdk.annotations.Zoom;
import com.elasticpath.rest.sdk.config.JacksonProvider;
import com.elasticpath.rest.sdk.model.Auth;
import com.elasticpath.rest.sdk.model.AuthToken;

public class ClientSdk {

	public static final MediaType TYPE = APPLICATION_JSON_TYPE;

	public <T> T get(String target,
					 AuthToken authToken,
					 Class<T> resultClass) {

		if (resultClass.isAnnotationPresent(Zoom.class)) {
			T resultObject = zoom(target, authToken, resultClass);
			return resultObject;
		}

		return getDetailed(target, authToken, resultClass);
	}

	private <T> T zoom(String href,
					   AuthToken authToken,
					   Class<T> resultClass) {
		String[] zoom = resultClass.getAnnotation(Zoom.class)
				.value();

		String target = fromPath(href)
				.queryParam("zoom", Joiner.on(":")
						.join(zoom))
				.toString();

		String json = getDetailed(target, authToken, String.class);

		ReadContext jsonContext = JsonPath.parse(json);

		T resultObject;
		try {
			resultObject = resultClass.newInstance();

			for (Field field : resultClass.getDeclaredFields()) {
				Json annotation = field.getAnnotation(Json.class);
				Object read = jsonContext.read(annotation.value());
				field.set(resultObject, String.valueOf(read));
			}
		} catch (IllegalAccessException | InstantiationException e) {
			throw new IllegalArgumentException(e);
		}
		return resultObject;
	}

	private <T> T getDetailed(String target,
							  AuthToken authToken,
							  Class<T> resultClass) {
		return newClient()
				.register(JacksonProvider.class)
				.target(target)
				.request(TYPE)
				.header(authToken.getHeaderName(), authToken.getHeaderValue())
				.get()
				.readEntity(resultClass);
	}

	public AuthToken auth(UriBuilder target,
						  Form auth) {

		Response response = newClient()
				.register(JacksonProvider.class)
				.target(target)
				.request(TYPE)
				.post(form(auth));

		String accessToken = response.readEntity(Auth.class)
				.getAccessToken();

		return new AuthToken(accessToken);
	}
}
