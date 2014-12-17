/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data.multi_level;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;

//TODO use lombok
/**
 * Test data class.
 */
@SuppressWarnings("PMD")
public class FourthLevel {

	private static final int ODD_PRIME = 31;

	@JsonPath("@.field1")//must resolve as relative
	private String field1;

	@JsonPath("$.second_level.third_level.fourth_level.field2")
	private String field2;

	@JsonProperty("field3") //must resolve as relative to class JsonPath/Property annotation
	private String field3;

	private String field4; //matches JSON field in "fourth-level" JSON node; must be set

	private String field5 = "default 4th field5"; //doesn't match any Json node; will not be set nor affected


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

	public String getField3() {
		return field3;
	}

	public void setField3(final String field3) {
		this.field3 = field3;
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

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		FourthLevel that = (FourthLevel) o;

		if (!field1.equals(that.field1)) {
			return false;
		}
		if (!field2.equals(that.field2)) {
			return false;
		}
		if (!field3.equals(that.field3)) {
			return false;
		}
		if (!field4.equals(that.field4)) {
			return false;
		}
		if (!field5.equals(that.field5)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = field1.hashCode();
		result = ODD_PRIME * result + field2.hashCode();
		result = ODD_PRIME * result + field3.hashCode();
		result = ODD_PRIME * result + field4.hashCode();
		result = ODD_PRIME * result + field5.hashCode();
		return result;
	}
}
