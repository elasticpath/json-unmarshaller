/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.fasterxml.jackson.contrib.jsonpath.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;

/**
 * Test data class.
 */
@SuppressWarnings("PMD")
public class TestUnmarshallingClassesFromIgnoredPackages {

	@JsonPath("$.price")
	public BigDecimal price;

	@JsonProperty("items")
	public List<String> items;

	@JsonProperty("date")
	public Date aDate;
}


