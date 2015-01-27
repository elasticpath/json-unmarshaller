/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data.multilevel;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.json.unmarshalling.data.multilevel.levels.ThirdLevelWithJsonPath;

/**
 * Test data class for multiple Json levels.
 */
@SuppressWarnings("PMD")
public class TestMultiLevelsWithNonAnnotatedField {

	@JsonProperty("second_level")
	public SecondLevelWithNonAnnotatedField secondLevelNonAnnotated;

	/**
	 * Test class.
	 */
	public static final class SecondLevelWithNonAnnotatedField {

		//CHECKSTYLE:OFF
		public ThirdLevelWithJsonPath third_level;
		//CHECKSTYLE:ON
	}
}


