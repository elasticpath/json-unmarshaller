package com.elasticpath.rest.client.unmarshalling;

import static com.google.common.collect.FluentIterable.from;
import static java.util.Arrays.asList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;


@Named
@Singleton
public class JsonPathModelIntrospector {

	/**
	 * Given a class, return all fields in the entire class hierarchy that contain JsonPath annotations.
	 * @param clazz the class to search.
	 * @param <T> the Class type
	 * @return all fields in that class hierarchy that contain JsonPath annotation(s).
	 */
	public <T> Iterable<Field> retrieveFieldsWithJsonPathAnnotations(final Class<T> clazz) {
		return getInjectableFields(getSuperclassHierarchy(clazz));
	}

	/*
	 * Retrieves current class and all superclasses except Object.
	 *
	 * @param resultClass leaf class in the hierarchy to scan
	 * @return an iterable collection of classes
	 */
	private Iterable<Class<?>> getSuperclassHierarchy(Class<?> resultClass) {

		Collection<Class<?>> superclasses = new ArrayList<Class<?>>();
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
	private Iterable<Field> getInjectableFields(Iterable<Class<?>> classes) {

		return from(classes)
				.transformAndConcat(new Function<Class<?>, Iterable<? extends Field>>() {
					public Iterable<? extends Field> apply(java.lang.Class<?> input) {
						return asList(input.getDeclaredFields());
					}
				})
				.filter(new Predicate<Field>() {
					public boolean apply(Field input) {
						return input.isAnnotationPresent(JsonPath.class);
					}
				});
	}
}