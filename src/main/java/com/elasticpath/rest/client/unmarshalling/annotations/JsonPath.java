package com.elasticpath.rest.client.unmarshalling.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Useful for specifying the JsonPath of a field via annotations. Set the json path into the value field.
 */
@Target({ANNOTATION_TYPE, FIELD, METHOD, PARAMETER})
@Retention(RUNTIME)
public @interface JsonPath {

	/**
	 * The json path value.
	 */
	String value();
}
