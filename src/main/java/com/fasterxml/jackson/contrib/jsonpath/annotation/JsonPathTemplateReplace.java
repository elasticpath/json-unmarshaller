package com.fasterxml.jackson.contrib.jsonpath.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by dkopel on 8/5/16.
 */
@Target({ANNOTATION_TYPE, FIELD, METHOD, PARAMETER})
@Retention(RUNTIME)
public @interface JsonPathTemplateReplace {
    String key();
    String value();
}
