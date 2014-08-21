package com.elasticpath.rest.sdk.model;

public class AuthToken {

	private String headerName;
	private String headerValue;

	public AuthToken(String accessToken) {
		this.headerName = "Authorization";
		this.headerValue = "Bearer " + accessToken;
	}

	public String getHeaderName() {
		return headerName;
	}

	public String getHeaderValue() {
		return headerValue;
	}
}
