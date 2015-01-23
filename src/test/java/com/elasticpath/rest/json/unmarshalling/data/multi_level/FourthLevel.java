/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data.multi_level;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;

/**
 * Test data class.
 */
@SuppressWarnings("PMD")
public class FourthLevel {

	public static final int PRIME_NUMBER = 31;
	@JsonPath("@.field1")//must resolve as relative
	public String field1;

	@JsonPath("$.second_level.third_level.fourth_level.field2")
	public String field2;

	@JsonProperty("field3") //must resolve as relative to class JsonPath/Property annotation
	public String field3;

	public String field4; //matches JSON field in "fourth-level" JSON node; must be set

	public String field5 = "default 4th field5"; //doesn't match any Json node; will not be set nor affected

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
		int result = field1 != null ? field1.hashCode() : 0;
		result = PRIME_NUMBER * result + (field2 != null ? field2.hashCode() : 0);
		result = PRIME_NUMBER * result + (field3 != null ? field3.hashCode() : 0);
		result = PRIME_NUMBER * result + (field4 != null ? field4.hashCode() : 0);
		result = PRIME_NUMBER * result + (field5 != null ? field5.hashCode() : 0);
		return result;
	}
}
