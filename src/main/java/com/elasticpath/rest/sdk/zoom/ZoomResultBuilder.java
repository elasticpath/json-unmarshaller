package com.elasticpath.rest.sdk.zoom;

import java.lang.reflect.Field;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import com.elasticpath.rest.sdk.annotations.JPath;

public class ZoomResultBuilder {

	public <T> T parseZoomResult(Class<T> resultClass,
								 String jsonResult) {
		ReadContext jsonContext = JsonPath.parse(jsonResult);
		try {
			T resultObject = resultClass.newInstance();

			for (Field field : resultClass.getDeclaredFields()) {
				JPath annotation = field.getAnnotation(JPath.class);
				Object read = jsonContext.read(annotation.value());
				field.set(resultObject, String.valueOf(read));
			}

			return resultObject;
		} catch (IllegalAccessException | InstantiationException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
