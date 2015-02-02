/*
 * Copyright © 2015 Elastic Path Software Inc. All rights reserved.
 */

package com.fasterxml.jackson.contrib.jsonpath.data.multilevel.levels;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
/**
 * Test data class.
 */
@SuppressWarnings("PMD")
public class ThirdLevelWithAnnotatedAccessors {

	private String absoluteJsonPath;

	//absolute path
	@JsonPath("$.second_level_array[1].third_level.field1")
	public String getAbsoluteJsonPath() {
		return absoluteJsonPath;
	}
}
