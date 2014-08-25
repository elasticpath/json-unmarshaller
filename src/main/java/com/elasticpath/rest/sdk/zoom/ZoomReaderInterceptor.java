package com.elasticpath.rest.sdk.zoom;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

import com.elasticpath.rest.sdk.annotations.Zoom;
import com.elasticpath.rest.sdk.annotations.Zooms;
import com.elasticpath.rest.sdk.debug.Logger;
import com.elasticpath.rest.sdk.totals.TotalZoom;
import com.elasticpath.rest.sdk.zoom.ZoomResultBuilder;

public class ZoomReaderInterceptor implements ReaderInterceptor {

	private Logger logger = new Logger();
	private ZoomResultBuilder zoomResultBuilder = new ZoomResultBuilder();

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

		String jsonResult;
		if (isSingleZoom(context.getType()) || isMultiZoom(context.getType())) {
			context.setType(String.class);

			jsonResult = (String) context.proceed();
			logger.prettyTrace(jsonResult);

			return zoomResultBuilder.parseZoomResult(TotalZoom.class, jsonResult);
		}

		return context.proceed();
	}

	private boolean isSingleZoom(Class<?> type) {
		return type
				.isAnnotationPresent(Zoom.class);
	}

	private boolean isMultiZoom(Class<?> type) {
		return type
				.isAnnotationPresent(Zooms.class);
	}
}
