package com.elasticpath.rest.client.zoom;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

@Named
@Singleton
public class ZoomReaderInterceptor implements ReaderInterceptor {

	@Inject
	private ZoomModelIntrospector zoomModelIntrospector;

	@Inject
	private ZoomResultFactory zoomResultFactory;

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException {

		if (!zoomModelIntrospector.isZoom(context.getType())) {
			return context.proceed();
		}

		Class<?> unmarshalledType = context.getType();

		context.setType(String.class);

		String jsonResult = (String) context.proceed();

		return zoomResultFactory.create(unmarshalledType, jsonResult);
	}

}
