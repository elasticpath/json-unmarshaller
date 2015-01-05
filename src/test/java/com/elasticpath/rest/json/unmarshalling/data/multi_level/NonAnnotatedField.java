/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data.multi_level;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;

/**
 * This test-data class will not be annotated in a testing class.
 * The purpose is to test a case when a non-primitive type is not annotated
 * but contains JsonPath/Property annotations. Such field must be processed
 * and all annotated fields set correctly (if path has a match in Json structure)
 */
@SuppressWarnings("PMD")
public class NonAnnotatedField {

	@JsonPath("$.nonAnnotatedField.field1")
	private String absJsonPathField;

	@JsonPath("@.field2")
	private String relJsonPathField;

	/*
	it is not allowed to have 2 fields with JsonProp pointing to a same field
	even if field names are different (thus, setter methods will be different as well)
	 */
	@JsonProperty("field3")
	private int jsonProp;

	/*fields field1, field2, field3 have matching JSon nodes
		when Jakson ObjectMapper resolves the values,
		these fields will be set reading matching nodes;

		However, if this class is processed in recursive call,
		field values will be overriden using JsonPath values

	*/
	@JsonPath("$.firstString")
	private String field1;

	@JsonPath("@.lastString")
	private String field2; //will be null because the path will be resolved as relative to field name in TestNonAnnotatedField i.e. nonAnnotatedField

	//matches json node
	private String field4;

	private String anythingElse = "non-annotated, doesn't match Json node";
}
