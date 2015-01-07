/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.client.unmarshalling.annotations.malformed;

import com.fasterxml.jackson.annotation.JsonProperty;

//TODO use lombok

/**
 * Test data class.
 */
@SuppressWarnings("PMD")
public class Empty {

	private static final int ODD_PRIME = 31;

	@JsonProperty("")
	private String field1;

	private int field2;

	private String field3;

	public String getField1() {
		return field1;
	}

	public void setField1(final String field1) {
		this.field1 = field1;
	}

	public int getField2() {
		return field2;
	}

	public void setField2(final int field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(final String field3) {
		this.field3 = field3;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Empty that = (Empty) o;

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
		result = ODD_PRIME * result + field2;
		result = ODD_PRIME * result + field3.hashCode();
		return result;
	}
}
