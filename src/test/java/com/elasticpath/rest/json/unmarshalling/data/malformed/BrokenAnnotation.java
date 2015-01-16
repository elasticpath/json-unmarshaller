/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data.malformed;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;

/**
 * This test-data class will not be annotated in a testing class.
 * The purpose is to test a case when a non-primitive type is not annotated
 * but contains JsonPath/Property annotations. Such field must be processed
 * and all annotated fields set correctly (if path has a match in Json structure)
 */
@SuppressWarnings("unused")
public class BrokenAnnotation {

	@JsonPath("$.broken Field.field1")
	private String absJsonPathField;

	@JsonPath("@.field2")
	private String relJsonPathField;

}
