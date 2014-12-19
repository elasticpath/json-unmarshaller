/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;
import com.elasticpath.rest.json.unmarshalling.data.multi_level.SecondLevel;

/**
 * Test data class for multiple Json levels.
 */
@SuppressWarnings("PMD")
public class TestMultiLevels {

	//these 5 fields will not be processed because they don't match any Json node nor they are annotated
	private int int1;
	private Integer integer2;
	private String string3;
	private byte[] byteArray4;
	private char char5;

	@JsonProperty("second_level")
	private SecondLevel secondLevelJProperty;

	@JsonPath("@.second_level")//will be resolved as absolute i.e. $.second_level
	private SecondLevel secondLevelRelativeJPath;

	@JsonPath("$.second_level")
	private SecondLevel secondLevelAbsoluteJPath;

	//testing arrays using JsonProperty
	@JsonProperty("second_level_array")
	private SecondLevel[] secondLevelArrayJProperty;

	@JsonProperty("second_level_array")
	private Iterable<SecondLevel> secondLevelIterableJProperty;

	// simple fields
	@JsonProperty("field3")
	private String field3;

	@JsonPath("$.field2")
	private String field2AbsolutePath;

	@JsonPath("@.field2")
	private String field2RelativePath;

	//testing arrays using JsonPath
	@JsonPath("$.second_level_array")
	private SecondLevel[] secondLevelArrayJPath;

	@JsonPath("$.second_level_array")
	private Iterable<SecondLevel> secondLevelIterableJPath;

	@JsonProperty("simple-field-1")
	private int simpleField1;

	@JsonProperty("simple-field-2")
	private String simpleField2;
}


