package com.elasticpath.rest.client.oauth2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuth2Token {

	private String headerName;

	@JsonProperty("access_token")
	private String headerValue;

	public OAuth2Token() {
		this.headerName = "Authorization";
	}

	public OAuth2Token(String token) {
		this();
		this.headerValue = token;
	}

	public String getHeaderName() {
		return headerName;
	}

	public String getHeaderValue() {
		return headerValue;
	}

	public void setHeaderValue(String accessToken) {
		this.headerValue = accessToken;
	}
}
