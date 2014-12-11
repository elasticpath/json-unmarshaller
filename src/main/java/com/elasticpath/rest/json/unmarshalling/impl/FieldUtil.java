/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class FieldUtil {

	private FieldUtil(){}

	public static boolean isFieldArrayOrListOfNonPrimitiveTypes(final Field field){

		return isFieldArrayOfNonPrimitiveTypes(field.getType()) || isFieldListOfNonPrimitiveTypes(field);
	}

	//FIXME what if field is final? make a test
	public static boolean isFieldNonPrimitiveAndNonFinal(final Class<?> fieldType){
		return !fieldType.isPrimitive() && !Modifier.isFinal(fieldType.getModifiers());
	}

	public static Class getActualTypeArgument(final Type genericType) {
		return (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
	}

	public static <T> void setField(final T resultObject, final Field field, final Object value) throws IllegalAccessException, IOException {
		field.setAccessible(true);
		field.set(resultObject, value);
		field.setAccessible(false);
	}

	public static Object getFieldValue(final Object resultObject, final Field field) throws IllegalAccessException, IOException {
		field.setAccessible(true);
		final Object fieldVal = field.get(resultObject);
		field.setAccessible(false);

		return fieldVal;
	}

	private static boolean isFieldListOfNonPrimitiveTypes(final Field field){

		return field.getType().isAssignableFrom(Iterable.class) && !getActualTypeArgument(field.getGenericType()).isPrimitive();
	}

	private static boolean isFieldArrayOfNonPrimitiveTypes(final Class<?> fieldType){

		return fieldType.isArray() && fieldType.getComponentType() != null && !fieldType.getComponentType().isPrimitive();
	}
}
