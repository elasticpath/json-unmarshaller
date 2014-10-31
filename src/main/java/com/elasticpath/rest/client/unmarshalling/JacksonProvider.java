package com.elasticpath.rest.client.unmarshalling;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Produces(APPLICATION_JSON)
@Named
@Singleton
public class JacksonProvider implements ContextResolver<ObjectMapper> {

	@Inject
	private ObjectMapper objectMapper;

	public JacksonProvider() {
		this.objectMapper = new ObjectMapper()
				.disable(FAIL_ON_UNKNOWN_PROPERTIES);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}
}
