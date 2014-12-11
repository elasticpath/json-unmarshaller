/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;

//TODO use lombok
public class FifthLevel {

	@JsonProperty("field1")
	private String field1;

	private int field2;

	private String field3;

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public int getField2() {
		return field2;
	}

	public void setField2(int field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		FifthLevel that = (FifthLevel) o;

		if (field2 != that.field2) {
			return false;
		}
		if (!field1.equals(that.field1)) {
			return false;
		}
		if (!field3.equals(that.field3)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = field1.hashCode();
		result = 31 * result + field2;
		result = 31 * result + field3.hashCode();
		return result;
	}
}
