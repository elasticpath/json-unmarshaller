package com.elasticpath.rest.client.urlbuilding.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({TYPE})
@Retention(RUNTIME)
public @interface EntryPointUri {

	public static final String SCOPE = "{scope}";

	public static final String DEFAULT = "default";

	String[] value();
}
