package com.elasticpath.rest.sdk.oauth2;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

import com.elasticpath.rest.sdk.oauth2.model.OAuth2Token;

@Named
@Singleton
public class OAuth2ReaderInterceptor implements ReaderInterceptor {

	@Inject
	private OAuth2TokenService tokenService;

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

		OAuth2Token oAuthHeaderToken = (OAuth2Token) context.proceed();

		tokenService.storeToken(oAuthHeaderToken);

		return oAuthHeaderToken;
	}
}
