/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;

/**
 * Test data class.
 */
@SuppressWarnings("PMD")
public class TestUTF8 {

	@JsonPath("$.utf8_greek")
	public String utf8Greek;

	@JsonPath("$.utf8_cyrillic")
	public String utf8Cyrillic;

	@JsonPath("$.utf8_chinese")
	public String utf8Chinese;
}