/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;

/**
 * Test data class.
 */
@SuppressWarnings("PMD")
public class TestUnmarshallingClassesFromIgnoredPackages {

	@JsonPath("$.price")
	private BigDecimal price;

	@JsonProperty("items")
	private List<String> items;

	@JsonProperty("date")
	private Date aDate;
}


