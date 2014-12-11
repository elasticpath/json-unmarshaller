package com.elasticpath.rest.json.unmarshalling.impl;

import static com.elasticpath.rest.json.unmarshalling.impl.FieldUtil.*;
import static com.google.common.collect.FluentIterable.from;
import static java.util.Arrays.asList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;


public class JsonAnnotationsModelIntrospector {

	/**
	 * Given a class, return all fields in the entire class hierarchy that contain JsonPath or JsonProperty annotations.
	 * @param clazz the class to search.
	 * @param <T> the Class type
	 * @return all fields in that class hierarchy that contain JsonPath annotation(s).
	 */
	public <T> Iterable<Field> retrieveFieldsWithJsonAnnotations(final Class<T> clazz) {
		return getInjectableFields(getSuperclassHierarchy(clazz),false);
	}

	/**
	 * Given a class, check if class contains at least one field (including super class) JsonPath annotation
	 * @param clazz the class to search
	 * @param <T> the Class type
	 * @return true if at least one field is annotated with @JsonPath
	 */
	public <T> boolean hasJsonPathAnnotatedFields(final Class<T> clazz) {
		return getInjectableFields(getSuperclassHierarchy(clazz),true).iterator().hasNext();
	}

	/**
	 * Given a field, check if field contains JsonPath annotations.
	 * The method inspects both array/list and non-array/list types, including also super classes
	 *
	 * @param field the field to be inspected
	 * @return true if field contains at least one JsonPath annotation at any level
	 */
	public boolean hasJsonPathAnnotatatedFields(final Field field){
		final Class<?> fieldType = field.getType();

		final boolean isFieldArrayOrListOfNonPrimitiveTypes = isFieldArrayOrListOfNonPrimitiveTypes(field);

		//isFieldArrayOrListOfNonPrimitiveTypes must go first because arrays' FINAL modifier is TRUE
		if (isFieldArrayOrListOfNonPrimitiveTypes || isFieldNonPrimitiveAndNonFinal(fieldType)){
			if (isFieldArrayOrListOfNonPrimitiveTypes) {
				//iterable
				if (fieldType.isAssignableFrom(Iterable.class)) {
					return hasJsonPathAnnotatedFields(getActualTypeArgument(field.getGenericType()));
				}

				//array
				return hasJsonPathAnnotatedFields(fieldType.getComponentType());
			}

			//non-array/list field
			return hasJsonPathAnnotatedFields(fieldType);
		}

		return false;
	}

	/*
	 * Retrieves current class and all superclasses except Object.
	 *
	 * @param resultClass leaf class in the hierarchy to scan
	 * @return an iterable collection of classes
	 */
	private Iterable<Class<?>> getSuperclassHierarchy(Class<?> resultClass) {

		Collection<Class<?>> superclasses = new ArrayList<>();
		Class<?> klass = resultClass;
		while (!(klass.equals(Object.class))) {
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
	private Iterable<Field> getInjectableFields(final Iterable<Class<?>> classes, final boolean lookForJsonPathOnly) {

		return from(classes)
				.transformAndConcat(new Function<Class<?>, Iterable<? extends Field>>() {
					public Iterable<? extends Field> apply(java.lang.Class<?> input) {
						return asList(input.getDeclaredFields());
					}
				})
			   .filter(new Predicate<Field>() {
				   public boolean apply(Field input) {
					   if (lookForJsonPathOnly) {
						   return input.isAnnotationPresent(JsonPath.class);
					   }

					   return input.isAnnotationPresent(JsonPath.class) || input.isAnnotationPresent(JsonProperty.class);
				   }
				});
	}
}