package com.elasticpath.rest.client.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({TYPE})
@Retention(RUNTIME)
public @interface EntryPointUri {

	public static final String SCOPE = "?scope?";

	public static final String DEFAULT = "?scope?";

	String[] value();
}
