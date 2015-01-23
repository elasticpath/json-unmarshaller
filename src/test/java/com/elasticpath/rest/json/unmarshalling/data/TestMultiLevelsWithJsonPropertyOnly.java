/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.json.unmarshalling.data.multi_level.ThirdLevelWithJsonPath;

/**
 * Test data class for multiple Json levels.
 */
@SuppressWarnings("PMD")
public class TestMultiLevelsWithJsonPropertyOnly {

	@JsonProperty("second_level")
	public SecondLevelWithJsonPropertyOnly secondLevelJProperty;

	/**
	 * Test class.
	 */
	public static final class SecondLevelWithJsonPropertyOnly {

		@JsonProperty("third_level")
		public ThirdLevelWithJsonPath thirdLevelWithJsonPath;
	}
}


