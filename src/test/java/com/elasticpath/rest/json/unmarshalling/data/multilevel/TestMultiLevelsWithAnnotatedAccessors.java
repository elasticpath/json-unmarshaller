/*
 * Copyright © 2015 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data.multilevel;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.json.unmarshalling.data.multilevel.levels.ThirdLevelWithJsonPath;

public class TestMultiLevelsWithAnnotatedAccessors {

	private SecondLevelWithAnnotatedAccessor secondLevelNonAnnotated;

	@JsonProperty("second_level")
	public void setSecondLevelNonAnnotated(final SecondLevelWithAnnotatedAccessor secondLevelNonAnnotated) {
		this.secondLevelNonAnnotated = secondLevelNonAnnotated;
	}

	public SecondLevelWithAnnotatedAccessor getSecondLevelNonAnnotated() {
		return secondLevelNonAnnotated;
	}

	/**
	 * Test class.
	 */
	public static final class SecondLevelWithAnnotatedAccessor {

		//CHECKSTYLE:OFF
		private ThirdLevelWithJsonPath thirdLevelWithJsonPath;
		//CHECKSTYLE:ON

		@JsonProperty("third_level")
		public void setThirdLevelWithJsonPath(final ThirdLevelWithJsonPath thirdLevelWithJsonPath) {
			this.thirdLevelWithJsonPath = thirdLevelWithJsonPath;
		}

		public ThirdLevelWithJsonPath getThird_level() {
			return thirdLevelWithJsonPath;
		}
	}
}
