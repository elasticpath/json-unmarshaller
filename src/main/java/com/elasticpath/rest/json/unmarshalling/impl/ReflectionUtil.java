package com.elasticpath.rest.json.unmarshalling.impl;

import static com.google.common.collect.FluentIterable.from;
import static java.util.Arrays.asList;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;

import com.google.common.base.Function;

/**
 * This class provides set of reflection call methods.
 */
public class ReflectionUtil {

	private static final List<String> IGNORED_PACKAGES = asList("java.lang", "java.math", "java.util");
	/**
	 * Checks if field is primitive.
	 *
	 * @param field the field to check
	 * @return true if field is primitive
	 */
	public boolean isFieldPrimitive(final Field field) {
		return field.getType().isPrimitive();
	}

	/**
	 * Checks if the field is an array or list.
	 * @param field the field to check.
	 * @return true if the field is an array or list.
	 */
	public boolean isFieldArrayOrList(final Field field) {
		final Class<?> type = field.getType();
		return type.isArray() || Iterable.class.isAssignableFrom(type);
	}

	/**
	 * Gets all type arguments out of a generic.
	 * <p>
	 * 		For example, it will retrieve String from Map{@literal <}String, Object{@literal >}
	 * </p>
	 * @param genericType the generic type to insect.
	 * @return the array of generic types
	 */
	@SuppressWarnings("PMD")
	public Class<?>[] geClassArgumentsFromGeneric(final Type genericType) {
		final Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
		final List<Class<?>> actualClassArguments = new ArrayList<>();

		for (Type actualTypeArgument : actualTypeArguments) {
			if (actualTypeArgument instanceof ParameterizedType) {
				actualClassArguments.add((Class) ((ParameterizedType) actualTypeArgument).getRawType());
			} else {
				actualClassArguments.add((Class) actualTypeArgument);
			}
		}
		return actualClassArguments.toArray(new Class[0]);
	}

	/**
	 * Set a value into the passed field.
	 * @param resultObject the object in which to set the field.
	 * @param field the field to set.
	 * @param value the value to set.
	 * @param <T> the type of the result object.
	 * @throws IllegalAccessException if field cannot be set.
	 */
	public <T> void setField(final T resultObject, final Field field, final Object value) throws IllegalAccessException {
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
	public Object getFieldValue(final Object resultObject, final Field field) throws IllegalAccessException {
		field.setAccessible(true);
		final Object fieldVal = field.get(resultObject);
		field.setAccessible(false);

		return fieldVal;
	}

	/**
	 * Given a class, return all fields in the entire class hierarchy.
	 *
	 * @param clazz the class to search.
	 * @param <T>   the Class type
	 * @return all fields in that class hierarchy
	 */
	public <T> Iterable<Field> retrieveAllFields(final Class<T> clazz) {

		final Iterable<Class<?>> classes = getClassHierarchy(clazz);

		return from(classes)
					   .transformAndConcat(new Function<Class<?>, Iterable<? extends Field>>() {
						   public Iterable<? extends Field> apply(final java.lang.Class<?> input) {
							   return asList(input.getDeclaredFields());
						   }
					   });
	}

	/**
	 * Checks if given class can be unmarshalled by checking following conditions:
	 * 1. class can't be primitive.
	 * 2. class can't be interface and can't come from java.lang pkg, java.util
	 * 		(eliminates recursive calls in classes like String, Integer, Long..).
	 * 3. if class is array, can't be array of primitives.
	 * 4. class can't be a map
	 *
	 * @param clazz a class to check.
	 * @return true if class can be un
	 */
	public boolean canUnmarshallClass(final Class<?> clazz) {

		return !(clazz.isPrimitive() || isNonInterfaceFromIgnoredPackage(clazz) || isArrayOfPrimitiveTypes(clazz)
						 || Map.class.isAssignableFrom(clazz));
	}

	private boolean isNonInterfaceFromIgnoredPackage(final Class<?> clazz) {
		final Package pkg = clazz.getPackage();
		return !clazz.isInterface() && pkg != null && isClassFromIgnoredPackage(pkg.getName());
	}

	private boolean isClassFromIgnoredPackage(final String pkg) {
		for (final String ignoredPkg : IGNORED_PACKAGES) {
			if (pkg.startsWith(ignoredPkg)) {
				return true;
			}
		}

		return false;
	}
	private boolean isArrayOfPrimitiveTypes(final Class<?> clazz) {
		return  clazz.isArray() && clazz.getComponentType().isPrimitive();
	}

	/*
	 * Retrieves current class and all superclasses except Object.
	 *
	 * @param resultClass leaf class in the hierarchy to scan
	 * @return an iterable collection of classes
	 */
	private Iterable<Class<?>> getClassHierarchy(final Class<?> resultClass) {

		Collection<Class<?>> classes = new ArrayList<>();
		Class<?> klass = resultClass;
		while (klass != null && !Object.class.equals(klass)) {
			classes.add(klass);
			klass = klass.getSuperclass();
		}
		return classes;
	}
}
