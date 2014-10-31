package com.elasticpath.rest.client.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.UriBuilder.fromUri;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.varia.NullAppender;

import com.elasticpath.rest.client.config.GuiceConfig;
import com.elasticpath.rest.client.unmarshalling.JacksonProvider;
import com.elasticpath.rest.client.oauth2.model.OAuth2Token;
import com.elasticpath.rest.client.unmarshalling.JsonUnmarshallReaderInterceptor;

public class IntegrationTest {

	private Integer port = 8089;

	//WireMock runs a lightweight, in-memory server to test against
	@Rule
	public WireMockRule wireMock = new WireMockRule(port);

	private Client client;

	//WireMock causes log4j to whine about configuration
	{
		BasicConfigurator.configure(new NullAppender());
	}

	// Manually setting up the DI. In production, these can be accessed as services through @Reference or blueprint
	{
		Injector guice = Guice.createInjector(new GuiceConfig());
		JacksonProvider jacksonProvider = guice.getInstance(JacksonProvider.class);
		JsonUnmarshallReaderInterceptor jsonUnmarshallReaderInterceptor = guice.getInstance(JsonUnmarshallReaderInterceptor.class);

		client = ClientBuilder.newBuilder()
				.register(jacksonProvider)
				.register(new ClientRequestFilter() {
					public void filter(ClientRequestContext requestContext) throws IOException {
						OAuth2Token authToken = new OAuth2Token("token");

						requestContext.getHeaders()
								.putSingle(authToken.getHeaderName(), authToken.getHeaderValue());
					}
				})
				.register(jsonUnmarshallReaderInterceptor)
				.build();
	}

	@Test
	public void givenSimpleJaxRsCallShouldReturnSimpleString() {

		String expected = "Expected result!";
		givenThat(get(urlEqualTo("/testPath"))
				.willReturn(aResponse()
						.withStatus(200)
						.withBody(expected)));

		String result = client.target(fromUri("http://localhost")
				.port(port))
				.path("testPath")
				.request()
				.get()
				.readEntity(String.class);

		assertEquals(expected, result);
	}

	@Test
	public void givenSampleZoomJsonWhenCallingMockCortexShouldDeserializeToPojo() {

		givenThat(get(urlEqualTo("/testPath"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", APPLICATION_JSON)
						.withBody(Data.cartTotalZoom)));

		TotalZoom result = client.target(fromUri("http://localhost")
				.port(port))
				.path("testPath")
				.request()
				.get()
				.readEntity(TotalZoom.class);

		assertEquals("USD", result.getCurrency());
	}

}
