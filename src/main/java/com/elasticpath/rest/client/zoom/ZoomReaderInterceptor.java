package com.elasticpath.rest.client.zoom;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

import com.elasticpath.rest.client.debug.Logger;

@Named
@Singleton
public class ZoomReaderInterceptor implements ReaderInterceptor {

	@Inject
	private Logger logger;

	@Inject
	private ZoomModelIntrospector zoomModelIntrospector;

	@Inject
	private ZoomResultFactory zoomResultFactory;

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

		String jsonResult;
		if (zoomModelIntrospector.isZoom(context.getType())) {
			Class<?> unmarshalledType = context.getType();
			context.setType(String.class);

			jsonResult = (String) context.proceed();
			logger.trace(jsonResult);

			return zoomResultFactory.create(unmarshalledType, jsonResult);
		}

		return context.proceed();
	}

}
