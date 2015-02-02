/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.fasterxml.jackson.contrib.jsonpath.data.multilevel.levels;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
/**
 * Test data class.
 */
@SuppressWarnings("PMD")
public class ThirdLevelWithJsonPath {

	//absolute path
	@JsonPath("$.second_level_array[1].third_level.field1")
	public String absoluteJsonPath;
}
