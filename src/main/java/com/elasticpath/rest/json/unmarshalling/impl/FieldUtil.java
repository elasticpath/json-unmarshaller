/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Util class for handling  {@link java.lang.reflect.Field} variables
 */
public final class FieldUtil {

	private FieldUtil(){}

	/**
	 * Check if field is array or list of non-primitive types
	 *
	 * @param field field to be inspected
	 * @return true if field is an array/list of non-primitive types
	 */
	public static boolean isFieldArrayOrListOfNonPrimitiveTypes(final Field field){

		return isFieldArrayOfNonPrimitiveTypes(field.getType()) || isFieldListOfNonPrimitiveTypes(field);
	}

	/**
	 * Inspects generic types and returns actual type
	 *
	 * @param genericType A class to be inspected
	 * @return actual type
	 */
	public static Class<?> getActualTypeArgument(final Type genericType) {
		return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
	}

	/**
	 * Set value to a field
	 *
	 * @param resultObject object holding the target field
	 * @param field target field
	 * @param value actual field value
	 * @param <T> type
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static <T> void setField(final T resultObject, final Field field, final Object value) throws IllegalAccessException, IOException {
		field.setAccessible(true);
		field.set(resultObject, value);
		field.setAccessible(false);
	}

	/**
	 * Return field value
	 *
	 * @param resultObject object holding the target field
	 * @param field target field from which value is returned
	 * @return field's value
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static Object getFieldValue(final Object resultObject, final Field field) throws IllegalAccessException, IOException {
		field.setAccessible(true);
		final Object fieldVal = field.get(resultObject);
		field.setAccessible(false);

		return fieldVal;
	}

	/*
	 * More concrete check whether a field is list of non-primitive types
	  *
	 * @param field
	 * @return true if field is list of non-primitive types
	 */
	private static boolean isFieldListOfNonPrimitiveTypes(final Field field){

		return field.getType().isAssignableFrom(Iterable.class) && !getActualTypeArgument(field.getGenericType()).isPrimitive();
	}

	/*
	 * More concrete check whether a field is array of non-primitive types
	  *
	 * @param fieldType
	 * @return if field is array of non-primitive types
	 */
	private static boolean isFieldArrayOfNonPrimitiveTypes(final Class<?> fieldType){

		return fieldType.isArray() && fieldType.getComponentType() != null && !fieldType.getComponentType().isPrimitive();
	}
}
