package com.elasticpath.rest.sdk.oauth;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

import com.elasticpath.rest.sdk.oauth.model.OAuth2Token;

public class OAuth2ReaderInterceptor implements ReaderInterceptor {

	private OAuth2TokenService tokenService = new OAuth2TokenService();

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

		OAuth2Token oAuthHeaderToken = (OAuth2Token) context.proceed();

		tokenService.auth(oAuthHeaderToken);

		return oAuthHeaderToken;
	}
}
