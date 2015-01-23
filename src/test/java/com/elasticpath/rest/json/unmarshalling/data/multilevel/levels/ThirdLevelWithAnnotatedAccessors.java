/*
 * Copyright © 2015 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data.multilevel.levels;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;
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
