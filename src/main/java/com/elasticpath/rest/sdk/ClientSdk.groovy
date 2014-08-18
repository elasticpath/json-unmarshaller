package com.elasticpath.rest.sdk

import static javax.ws.rs.client.Entity.form
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE

import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.Form

import com.elasticpath.rest.sdk.model.Auth
import com.elasticpath.rest.sdk.model.Linkable

class ClientSdk {

	static void main(String[] args) {

		def accessToken = auth()

		println accessToken

		def response = ClientBuilder.newClient()
				.register(JacksonProvider)
				.target('http://localhost:9080/cortex/root/mobee')
				.request(APPLICATION_JSON_TYPE)
				.header('Authorization', "Bearer $accessToken")
				.get()

		def linkable = response.readEntity(Linkable)
		println linkable.links.asCollection().rel
	}

	static auth() {
		Form auth = new Form()
				.param('grant_type', 'password')
				.param('username', 'ben.boxer@elasticpath.com')
				.param('password', 'password')
				.param('role', 'REGISTERED')
				.param('scope', 'mobee')

		def response = ClientBuilder.newClient()
				.register(JacksonProvider)
				.target('http://localhost:9080/cortex/oauth2/tokens')
				.request(APPLICATION_JSON_TYPE)
				.post(form(auth))

		response.readEntity(Auth).accessToken
	}
}