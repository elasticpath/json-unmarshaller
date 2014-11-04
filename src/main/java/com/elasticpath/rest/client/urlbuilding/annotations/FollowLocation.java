package com.elasticpath.rest.client.urlbuilding.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Target({TYPE})
@Retention(RUNTIME)
public @interface FollowLocation {

	public static final String PARAM_NAME = "followLocation";

}
