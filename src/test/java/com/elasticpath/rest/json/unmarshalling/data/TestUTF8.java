/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;

/**
 * Test data class.
 */
@SuppressWarnings("PMD")
public class TestUTF8 {

	@JsonPath("$.utf8_greek")
	private String utf8Greek;

	@JsonPath("$.utf8_cyrillic")
	private String utf8Cyrillic;

	@JsonPath("$.utf8_chinese")
	private String utf8Chinese;
}