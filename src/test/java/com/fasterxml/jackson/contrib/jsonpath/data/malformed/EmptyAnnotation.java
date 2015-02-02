/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.fasterxml.jackson.contrib.jsonpath.data.malformed;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Test data class.
 */
@SuppressWarnings("unused")
public class EmptyAnnotation {

	@JsonProperty("")
	private String field1;

}
