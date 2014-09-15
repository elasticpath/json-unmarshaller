package com.elasticpath.rest.client.zoom;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.ReadContext;

import com.elasticpath.rest.client.annotations.JsonPath;

@Named
@Singleton
public class ZoomResultFactory {

	@Inject
	private ObjectMapper objectMapper;

	public <T> T create(Class<T> resultClass,
						String jsonResult) throws IOException {
		ReadContext jsonContext = com.jayway
				.jsonpath
				.JsonPath
				.parse(jsonResult);
		try {
			T resultObject = resultClass.newInstance();

			for (Field field : resultClass.getDeclaredFields()) {
				JsonPath annotation = field.getAnnotation(JsonPath.class);
				Object read = jsonContext.read(annotation.value());
				Class<?> fieldType = field.getType();
				Type genericType = field.getGenericType();

				if (fieldType.isPrimitive()) {
					field.set(resultObject, read);
				} else if (genericType instanceof ParameterizedType) {
					ParameterizedType genericType1 = (ParameterizedType) genericType;
					Class aClass = (Class) genericType1.getActualTypeArguments()[0];

					JavaType typedField = objectMapper.getTypeFactory()
							.constructParametricType(fieldType, aClass);
					Object value = objectMapper.readValue(String.valueOf(read), typedField);
					field.set(resultObject, value);
				} else if (fieldType.isAssignableFrom(String.class)) {
					field.set(resultObject, read);
				} else {
					field.set(resultObject, objectMapper.readValue(String.valueOf(read), fieldType));
				}
			}
			return resultObject;
		} catch (IllegalAccessException | InstantiationException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
