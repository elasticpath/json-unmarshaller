package com.elasticpath.rest.sdk

import static javax.ws.rs.client.ClientBuilder.newClient
import static javax.ws.rs.client.Entity.form
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE

import javax.ws.rs.core.Form
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriBuilder

import com.elasticpath.rest.sdk.model.Auth
import com.elasticpath.rest.sdk.model.Linkable

class ClientSdk {

	static void main(String[] args) {

		def scope = 'mobee'
		def serverPath = cortexUri()
		def accessToken = auth(
				scope,
				serverPath.clone()
						.path('oauth2')
						.path('tokens')
		)

		def href = serverPath.path('root')
				.path(scope)
		Response response = get(href, accessToken)
		def linkable = response.readEntity(Linkable)

		def href2 = UriBuilder.fromPath(linkable.links[0].href)
		def response2 = get(href2, accessToken)
		println response2.readEntity(Linkable).links.asCollection().rel
	}

	static Response get(UriBuilder href, String accessToken) {
		newClient()
				.register(JacksonProvider)
				.target(href)
				.request(APPLICATION_JSON_TYPE)
				.header('Authorization', "Bearer $accessToken")
				.get()
	}

	static auth(String scope, UriBuilder path) {
		Form auth = new Form()
				.param('grant_type', 'password')
				.param('username', 'ben.boxer@elasticpath.com')
				.param('password', 'password')
				.param('role', 'REGISTERED')
				.param('scope', scope)

		def response = newClient()
				.register(JacksonProvider)
				.target(path)
				.request(APPLICATION_JSON_TYPE)
				.post(form(auth))

		response.readEntity(Auth)
				.accessToken
	}

	static def cortexUri() {
		UriBuilder.newInstance()
				.scheme('http')
				.host('localhost')
				.port(9080)
				.path('cortex')
	}
}