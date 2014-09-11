package com.elasticpath.rest.sdk.zoom;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

import com.elasticpath.rest.sdk.annotations.Zoom;
import com.elasticpath.rest.sdk.annotations.Zooms;
import com.elasticpath.rest.sdk.debug.Logger;

@Named
@Singleton
public class ZoomReaderInterceptor implements ReaderInterceptor {

	@Inject
	private Logger logger;

	@Inject
	private ZoomResultBuilder zoomResultBuilder;

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

		String jsonResult;
		if (isSingleZoom(context.getType()) || isMultiZoom(context.getType())) {
			Class<?> unmarshalledType = context.getType();
			context.setType(String.class);

			jsonResult = (String) context.proceed();
			logger.trace(jsonResult);

			return zoomResultBuilder.parseZoomResult(unmarshalledType, jsonResult);
		}

		return context.proceed();
	}

	private boolean isSingleZoom(Class<?> type) {
		return type.isAnnotationPresent(Zoom.class);
	}

	private boolean isMultiZoom(Class<?> type) {
		return type.isAnnotationPresent(Zooms.class);
	}
}
