/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data.multilevel;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;
import com.elasticpath.rest.json.unmarshalling.data.multilevel.levels.SecondLevel;

/**
 * Test data class for multiple Json levels.
 */
@SuppressWarnings("PMD")
public class TestMultiLevelsAnnotatedFields {

	//these 5 fields will not be processed because they don't match any Json node nor they are annotated
	public int int1;
	public Integer integer2;
	public String string3;
	public byte[] byteArray4;
	public char char5;

	@JsonProperty("second_level")
	public SecondLevel secondLevelJProperty;

	@JsonPath("@.second_level")//will be resolved as absolute i.e. $.second_level
	public SecondLevel secondLevelRelativeJPath;

	@JsonPath("$.second_level")
	public SecondLevel secondLevelAbsoluteJPath;

	//testing arrays using JsonProperty
	@JsonProperty("second_level_array")
	public SecondLevel[] secondLevelArrayJProperty;

	@JsonProperty("second_level_array")
	public Iterable<SecondLevel> secondLevelIterableJProperty;

	// simple fields
	@JsonProperty("field3")
	public String field3;

	@JsonPath("$.field2")
	public String field2AbsolutePath;

	@JsonPath("@.field2")
	public String field2RelativePath;

	//testing arrays using JsonPath
	@JsonPath("$.second_level_array")
	public SecondLevel[] secondLevelArrayJPath;

	@JsonPath("$.second_level_array")
	public Iterable<SecondLevel> secondLevelIterableJPath;

	@JsonProperty("simple-field-1")
	public int simpleField1;

	@JsonProperty("simple-field-2")
	public String simpleField2;
	
	
}


