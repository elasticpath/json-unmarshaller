/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Reflective Java Field utilities.
 */
public final class FieldUtil {

	private FieldUtil() {
	}

	/**
	 * Checks if the field is an array or list of primitive types.
	 * @param field the field to check.
	 * @return true if the passed field is a list ot array of primitive types.
	 */
	public static boolean isFieldArrayOrListOfPrimitiveTypes(final Field field) {

		return isFieldArrayOfPrimitiveTypes(field.getType()) || isFieldAListOfPrimitiveTypes(field);
	}

	/**
	 * Checks if the field is an array or list.
	 * @param field the field to check.
	 * @return true if the field is an array or list.
	 */
	public static boolean isFieldArrayOrList(final Field field) {
		Class<?> type = field.getType();
		return type.isArray() || type.isAssignableFrom(Iterable.class);
	}

	/**
	 * Gets the first type argument out of a generic.
	 * <p>
	 * 		For example, it will retrieve String from Map{@literal <}String, Object{@literal >}
	 * </p>
	 * @param genericType the generic type to insect.
	 * @return the type of the first generic argument.
	 */
	public static Class<?> getFirstTypeArgumentFromGeneric(final Type genericType) {
		return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
	}

	/**
	 * Set a value into the passed field.
	 * @param resultObject the object in which to set the field.
	 * @param field the field to set.
	 * @param value the value to set.
	 * @param <T> the type of the result object.
	 * @throws IllegalAccessException if field cannot be set.
	 */
	public static <T> void setField(final T resultObject, final Field field, final Object value) throws IllegalAccessException {
		field.setAccessible(true);
		field.set(resultObject, value);
		field.setAccessible(false);
	}

	/**
	 * Gets the value from a specific field in the passed object.
	 * @param resultObject the object to search.
	 * @param field the field to get from.
	 * @return the value of the field.
	 * @throws IllegalAccessException if field cannot be accessed.
	 */
	public static Object getFieldValue(final Object resultObject, final Field field) throws IllegalAccessException {
		field.setAccessible(true);
		final Object fieldVal = field.get(resultObject);
		field.setAccessible(false);

		return fieldVal;
	}

	private static boolean isFieldAListOfPrimitiveTypes(final Field field) {

		return field.getType().isAssignableFrom(Iterable.class) && getFirstTypeArgumentFromGeneric(field.getGenericType()).isPrimitive();
	}

	private static boolean isFieldArrayOfPrimitiveTypes(final Class<?> fieldType) {

		return fieldType.isArray() && fieldType.getComponentType() != null && fieldType.getComponentType().isPrimitive();
	}

}

