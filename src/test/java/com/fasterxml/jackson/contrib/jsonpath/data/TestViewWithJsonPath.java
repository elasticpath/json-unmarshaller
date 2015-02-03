package com.fasterxml.jackson.contrib.jsonpath.data;


import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;

/**
 * Test data class.
 */
public class TestViewWithJsonPath {
	@JsonPath("$.links[?(@.rel == 'discount')].type")
	private Iterable<String> type;

	public String getType() {
		return type.iterator().next();
	}
}