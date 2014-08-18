package com.elasticpath.rest.sdk

import static javax.ws.rs.client.Entity.form
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE

import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.Form

class ClientSdk {

	static void main(String[] args) {

		Form auth = new Form()
				.param('grant_type', 'password')
				.param('username', 'ben.boxer@elasticpath.com')
				.param('password', 'password')
				.param('role', 'REGISTERED')
				.param('scope', 'mobee')

		def submit = ClientBuilder.newClient()
				.target('http://localhost:9080/cortex/oauth2/tokens')
				.request(APPLICATION_JSON_TYPE)
				.post(form(auth))

		def accessToken = submit.readEntity(Auth).accessToken
		println accessToken
	}
}