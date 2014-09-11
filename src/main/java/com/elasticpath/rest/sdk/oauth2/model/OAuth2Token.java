package com.elasticpath.rest.sdk.oauth2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuth2Token {

	private String headerName;

	@JsonProperty("access_token")
	private String headerValue;

	public OAuth2Token() {
		this.headerName = "Authorization";
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
