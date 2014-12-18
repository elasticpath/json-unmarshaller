package com.elasticpath.rest.json.unmarshalling.impl;

import static com.elasticpath.rest.json.unmarshalling.impl.FieldUtil.getFirstTypeArgumentFromGeneric;
import static com.elasticpath.rest.json.unmarshalling.impl.FieldUtil.isFieldArrayOrList;
import static com.elasticpath.rest.json.unmarshalling.impl.FieldUtil.isFieldArrayOrListOfPrimitiveTypes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;

/**
 * Tool for inspecting the fields in a Class for Json unmarshalling.
 */
public class JsonAnnotationsModelIntrospector {

	/**
	 * Given a class, return all fields in the entire class hierarchy.
	 *
	 * @param clazz the class to search.
	 * @param <T>   the Class type
	 * @return all fields in that class hierarchy
	 */
	public <T> Iterable<Field> retrieveAllFields(final Class<T> clazz) {
		return getInjectableFields(getSuperclassHierarchy(clazz), false);
	}

	/**
	 * Given a class, check if class contains at least one field (including super class) JsonPath annotation.
	 *
	 * @param clazz the class to search
	 * @param <T>   the Class type
	 * @return true if at least one field is annotated with @JsonPath
	 */
	public <T> boolean hasJsonPathAnnotatedFields(final Class<T> clazz) {
		return getInjectableFields(getSuperclassHierarchy(clazz), true).iterator().hasNext();
	}

	/**
	 * Given a field, check if field contains JsonPath annotations.
	 * The method inspects both array/list and non-array/list types, including also super classes
	 *
	 * @param field the field to be inspected
	 * @return true if field contains at least one JsonPath annotation at any level
	 */
	public boolean hasJsonPathAnnotations(final Field field) {
		final Class<?> fieldType = field.getType();

		if (isFieldArrayOrListOfPrimitiveTypes(field) || fieldType.isPrimitive()) {
			return false;
		}
		if (isFieldArrayOrList(field)) {
			//iterable
			if (fieldType.isAssignableFrom(Iterable.class)) {
				return hasJsonPathAnnotatedFields(getFirstTypeArgumentFromGeneric(field.getGenericType()));
			}
			//array
			return hasJsonPathAnnotatedFields(fieldType.getComponentType());
		}

		//non-array/list field
		return hasJsonPathAnnotatedFields(fieldType);


	}

	/*
	 * Retrieves current class and all superclasses except Object.
	 *
	 * @param resultClass leaf class in the hierarchy to scan
	 * @return an iterable collection of classes
	 */
	private Collection<Class<?>> getSuperclassHierarchy(final Class<?> resultClass) {

		Collection<Class<?>> superclasses = new ArrayList<>();
		Class<?> klass = resultClass;
		while (klass != null && !Object.class.equals(klass)) {
			superclasses.add(klass);
			klass = klass.getSuperclass();
		}
		return superclasses;
	}

	/*
	 * Retrieves all fields valid for injection from the given iterable of classes.
	 * <p/>
	 * Fields are valid if they are annotated @JsonPath.
	 *
	 * @param classes any iterable collection of classes
	 * @return an iterable collection of fields
	 */
	private Iterable<Field> getInjectableFields(final Collection<Class<?>> classes, final boolean lookForJsonPathOnly) {
		List<Field> injectableFields = new ArrayList<>();
		for (Class<?> clazz : classes) {
			for (Field potentialField : clazz.getDeclaredFields()) {
				if (!lookForJsonPathOnly || potentialField.isAnnotationPresent(JsonPath.class)) {
					injectableFields.add(potentialField);
				}
			}
		}
		return injectableFields;
		/* In Java 8 the perfomance of above can massively be improved using Streams as below:
		return classes.stream()
				.flatMap(clazz -> Arrays.stream(clazz.getDeclaredFields()))
				.filter(field -> !lookForJsonPathOnly || field.isAnnotationPresent(JsonPath.class))
				.collect(Collectors.toList());*/
	}
}