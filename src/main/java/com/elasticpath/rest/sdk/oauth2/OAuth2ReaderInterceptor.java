package com.elasticpath.rest.sdk.oauth2;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

import com.elasticpath.rest.sdk.oauth2.model.OAuth2Token;

public class OAuth2ReaderInterceptor implements ReaderInterceptor {

	private final OAuth2TokenService tokenService;

	public OAuth2ReaderInterceptor(final OAuth2TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

		OAuth2Token oAuthHeaderToken = (OAuth2Token) context.proceed();

		tokenService.storeToken(oAuthHeaderToken);

		return oAuthHeaderToken;
	}
}
