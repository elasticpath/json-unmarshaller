package com.elasticpath.rest.client.oauth2

import static javax.ws.rs.core.Response.Status.Family.SERVER_ERROR
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL
import static org.mockito.Answers.RETURNS_DEEP_STUBS
import static org.mockito.BDDMockito.given
import static org.mockito.Matchers.any
import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.verify

import javax.ws.rs.client.Client
import javax.ws.rs.client.Entity
import javax.ws.rs.client.Invocation
import javax.ws.rs.core.Response

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.slf4j.Logger

import com.elasticpath.rest.client.oauth2.model.OAuth2Token

@RunWith(MockitoJUnitRunner)
class OAuth2AuthorizationServiceTest {

	@Mock(answer = RETURNS_DEEP_STUBS)
	Client client

	@Mock
	Logger logger

	@Mock
	OAuth2TokenService oAuth2TokenService

	@InjectMocks
	OAuth2AuthorizationService service

	Invocation.Builder builder
	Response authResponse

	@Before
	void setUp() {
		builder = client.target(anyString())
				.request()

		authResponse = builder.post(any(Entity))

		given(authResponse.readEntity(OAuth2Token))
				.willReturn(new OAuth2Token())
	}

	@Test
	void 'Given successful, when authenticating, should deserialize auth token'() {

		given(authResponse.getStatusInfo().getFamily())
				.willReturn(SUCCESSFUL)

		service.auth(client, null)

		verify(builder.post(any(Entity)))
				.readEntity(OAuth2Token)
	}

	@Test
	void 'Given unsuccessful, when authenticating, should log'() {

		given(authResponse.readEntity(String))
				.willReturn('auth failed')
		given(authResponse.getStatusInfo().getFamily())
				.willReturn(SERVER_ERROR)

		service.auth(client, null)

		verify(logger).error(anyString())
	}
}
