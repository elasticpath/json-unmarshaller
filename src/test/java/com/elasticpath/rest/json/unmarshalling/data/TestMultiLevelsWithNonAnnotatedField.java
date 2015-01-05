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
public class TestMultiLevelsWithNonAnnotatedField {

	@JsonProperty("second_level")
	private SecondLevelWithNonAnnotatedField secondLevelNonAnnotated;

	/**
	 * Test class.
	 */
	public static final class SecondLevelWithNonAnnotatedField {

		//CHECKSTYLE:OFF
		private ThirdLevelWithJsonPath third_level;
		//CHECKSTYLE:ON
	}
}


