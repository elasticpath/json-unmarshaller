/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data.malformed;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;

/**
 * This test-data class will not be annotated in a testing class.
 * The purpose is to test a case when a non-primitive type is not annotated
 * but contains JsonPath/Property annotations. Such field must be processed
 * and all annotated fields set correctly (if path has a match in Json structure)
 */
//TODO use lombok
public class BrokenNonAnnotatedField {

	@JsonPath("$.brokenNonAn notatedField.field1")
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

	private String field5 = "anything else";

	public String getAbsJsonPathField() {
		return absJsonPathField;
	}

	public void setAbsJsonPathField(final String absJsonPathField) {
		this.absJsonPathField = absJsonPathField;
	}

	public String getRelJsonPathField() {
		return relJsonPathField;
	}

	public void setRelJsonPathField(final String relJsonPathField) {
		this.relJsonPathField = relJsonPathField;
	}

	public int getJsonProp() {
		return jsonProp;
	}

	public void setJsonProp(final int jsonProp) {
		this.jsonProp = jsonProp;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(final String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(final String field2) {
		this.field2 = field2;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(final String field4) {
		this.field4 = field4;
	}

	public String getField5() {
		return field5;
	}

	public void setField5(final String field5) {
		this.field5 = field5;
	}
}
