/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data.multilevel.levels;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;

/**
 * Test data class.
 */
@SuppressWarnings("PMD")
public final class SecondLevel {
	
	public static final int PRIME_NUMBER = 31;
	
	//absolute path
	@JsonPath("$.second_level.field1")
	public String field1;

	//relative path
	@JsonPath("@.field2") //must resolve as relative path to @JsonPath/Property annotation on ThirdLevel class
	public String field2;

	@JsonProperty("field3") //must resolve as relative path to @JsonPath/Property annotation on ThirdLevel class
	public String field3;

	public String field4; //matches JSON field in "third-level" JSON node; must be set

	public String field5 = "default 2nd field5"; //doesn't match any Json node; will not be set nor affected

	@JsonProperty("third_level")
	public ThirdLevel thirdLevelJsonProperty;

	@JsonPath("$.second_level.third_level")
	public ThirdLevel thirdLevelAbsoluteJPath;

	@JsonPath("@.third_level")
	public ThirdLevel thirdLevelRelativeJPath;

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SecondLevel that = (SecondLevel) o;

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
		result = PRIME_NUMBER * result + field2.hashCode();
		result = PRIME_NUMBER * result + field3.hashCode();
		result = PRIME_NUMBER * result + field4.hashCode();
		result = PRIME_NUMBER * result + field5.hashCode();
		result = PRIME_NUMBER * result + thirdLevelJsonProperty.hashCode();
		result = PRIME_NUMBER * result + thirdLevelAbsoluteJPath.hashCode();
		result = PRIME_NUMBER * result + thirdLevelRelativeJPath.hashCode();
		return result;
	}
}
