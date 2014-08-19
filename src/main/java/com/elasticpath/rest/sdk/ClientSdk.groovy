package com.elasticpath.rest.sdk

import static javax.ws.rs.client.ClientBuilder.newClient
import static javax.ws.rs.client.Entity.form
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE

import javax.ws.rs.core.Form
import javax.ws.rs.core.UriBuilder

import com.elasticpath.rest.sdk.model.Auth
import com.elasticpath.rest.sdk.model.Link
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

		def root = get(href, accessToken)
		trace(root)

		def cart = get(root.links[0], accessToken)
		trace(cart)

		def lineItems = get(cart.links[0], accessToken)
		trace(lineItems)
	}

	static void trace(Linkable linkable) {
		println linkable.links.asCollection().rel
	}

	static def get(UriBuilder href, String accessToken) {

		newClient()
				.register(JacksonProvider)
				.target(href)
				.request(APPLICATION_JSON_TYPE)
				.header('Authorization', "Bearer $accessToken")
				.get()
				.readEntity(Linkable)
	}

	static def get(Link link, String accessToken) {

		def target = UriBuilder.fromPath(link.href)

		get(target, accessToken)
	}

	static auth(String scope, UriBuilder target) {
		Form auth = new Form()
				.param('grant_type', 'password')
				.param('username', 'ben.boxer@elasticpath.com')
				.param('password', 'password')
				.param('role', 'REGISTERED')
				.param('scope', scope)

		def response = newClient()
				.register(JacksonProvider)
				.target(target)
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