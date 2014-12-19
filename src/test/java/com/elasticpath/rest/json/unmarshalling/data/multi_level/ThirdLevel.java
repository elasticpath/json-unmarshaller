/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data.multi_level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;
/**
 * Test data class.
 */
@SuppressWarnings("PMD")
public class ThirdLevel {
	
	private static final int PRIME_NUMBER = 31;
	
	//absolute path
	@JsonPath("$.second_level.third_level.field1")
	private String field1;

	//relative path
	@JsonPath("@.field2") //must resolve as relative path to @JsonPath/Property annotation on ThirdLevel class
	private String field2;

	@JsonProperty("field3") //must resolve as relative path to @JsonPath/Property annotation on ThirdLevel class
	private String field3;

	private String field4; //matches JSON field in "third-level" JSON node; must be set

	private String field5 = "default 3rd field5"; //doesn't match any Json node; will not be set nor affected

	@JsonPath("@.fourth_level_array")
	private Iterable<FourthLevel> fourthLevelIterable;

	@JsonProperty("fourth_level_array")
	private FourthLevel[] fourthLevelArray;

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
