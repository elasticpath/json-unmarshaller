package com.elasticpath.rest.sdk.oauth;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

import com.elasticpath.rest.sdk.oauth.model.OAuthToken;

public class OAuthReaderInterceptor implements ReaderInterceptor {

	private OAuthTokenService tokenService = new OAuthTokenService();

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

		OAuthToken oAuthHeaderToken = (OAuthToken) context.proceed();

		tokenService.auth(oAuthHeaderToken);

		return oAuthHeaderToken;
	}
}
