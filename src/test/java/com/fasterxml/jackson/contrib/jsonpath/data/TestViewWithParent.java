package com.fasterxml.jackson.contrib.jsonpath.data;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;

/**
 * Test data class.
 */
public class TestViewWithParent extends MixedAnnotationsSuperClass {

	@JsonPath("$._total[0].cost[0].currency")
	private String currency;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(final String currency) {
		this.currency = currency;
	}
}
