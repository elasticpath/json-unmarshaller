/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.fasterxml.jackson.contrib.jsonpath.data.multilevel.levels;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
/**
 * Test data class.
 */
@SuppressWarnings("PMD")
public class ThirdLevel {
	
	public static final int PRIME_NUMBER = 31;
	
	//absolute path
	@JsonPath("$.second_level.third_level.field1")
	public String field1;

	//relative path
	@JsonPath("@.field2") //must resolve as relative path to @JsonPath/Property annotation on ThirdLevel class
	public String field2;

	@JsonProperty("field3") //must resolve as relative path to @JsonPath/Property annotation on ThirdLevel class
	public String field3;

	public String field4; //matches JSON field in "third-level" JSON node; must be set

	public String field5 = "default 3rd field5"; //doesn't match any Json node; will not be set nor affected

	@JsonPath("@.fourth_level_array")
	public Iterable<FourthLevel> fourthLevelIterable;

	@JsonProperty("fourth_level_array")
	public FourthLevel[] fourthLevelArray;

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}

		ThirdLevel that = (ThirdLevel) object;

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
		if (!(fourthLevelIterable).equals(that.fourthLevelIterable)) {
			return false;
		}
		if (!Arrays.equals(fourthLevelArray, that.fourthLevelArray)) {
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
		result = PRIME_NUMBER * result + fourthLevelIterable.hashCode();
		result = PRIME_NUMBER * result + Arrays.hashCode(fourthLevelArray);
		return result;
	}
}
