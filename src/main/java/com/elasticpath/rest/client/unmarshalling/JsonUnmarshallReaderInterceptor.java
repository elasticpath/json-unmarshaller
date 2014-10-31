package com.elasticpath.rest.client.unmarshalling;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

@Named
@Singleton
public class JsonUnmarshallReaderInterceptor implements ReaderInterceptor {

	@Inject
	private JsonPathResultFactory jsonPathResultFactory;

	@Inject
	private JsonPathModelIntrospector jsonPathModelIntrospector;

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException {

		if (!doesClassContainJsonPathAnnotations(context.getType())) {
			return context.proceed();
		}

		Class<?> unmarshalledType = context.getType();

		context.setType(String.class);

		String jsonResult = (String) context.proceed();

		return jsonPathResultFactory.create(unmarshalledType, jsonResult);
	}

	private boolean doesClassContainJsonPathAnnotations(Class clazz) {
		Iterable jsonFields = jsonPathModelIntrospector.retrieveFieldsWithJsonPathAnnotations(clazz);
		return jsonFields.iterator().hasNext();
	}

}
