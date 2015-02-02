package com.fasterxml.jackson.contrib.jsonpath.data;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;

/**
 * Test data class.
 */
public class TestViewWithOtherFields {

	@JsonPath("$._total[0].cost[0].currency")
	private String currency;

	private String notForDeserialization;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(final String currency) {
		this.currency = currency;
	}

	public String getNotForDeserialization() {
		return notForDeserialization;
	}

	public void setNotForDeserialization(final String notForDeserialization) {
		this.notForDeserialization = notForDeserialization;
	}
}
