/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;
//TODO use lombok
public class TestMultiLevels {

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

	public int getSimpleField1() {
		return simpleField1;
	}

	public void setSimpleField1(int simpleField1) {
		this.simpleField1 = simpleField1;
	}

	public String getSimpleField2() {
		return simpleField2;
	}

	public void setSimpleField2(String simpleField2) {
		this.simpleField2 = simpleField2;
	}

	public SecondLevel getSecondLevelJProperty() {
		return secondLevelJProperty;
	}

	public void setSecondLevelJProperty(SecondLevel secondLevelJProperty) {
		this.secondLevelJProperty = secondLevelJProperty;
	}

	public SecondLevel getSecondLevelRelativeJPath() {
		return secondLevelRelativeJPath;
	}

	public void setSecondLevelRelativeJPath(SecondLevel secondLevelRelativeJPath) {
		this.secondLevelRelativeJPath = secondLevelRelativeJPath;
	}

	public SecondLevel getSecondLevelAbsoluteJPath() {
		return secondLevelAbsoluteJPath;
	}

	public void setSecondLevelAbsoluteJPath(SecondLevel secondLevelAbsoluteJPath) {
		this.secondLevelAbsoluteJPath = secondLevelAbsoluteJPath;
	}

	public SecondLevel[] getSecondLevelArrayJProperty() {
		return secondLevelArrayJProperty;
	}

	public void setSecondLevelArrayJProperty(SecondLevel[] secondLevelArrayJProperty) {
		this.secondLevelArrayJProperty = secondLevelArrayJProperty;
	}

	public Iterable<SecondLevel> getSecondLevelIterableJProperty() {
		return secondLevelIterableJProperty;
	}

	public void setSecondLevelIterableJProperty(Iterable<SecondLevel> secondLevelIterableJProperty) {
		this.secondLevelIterableJProperty = secondLevelIterableJProperty;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public String getField2AbsolutePath() {
		return field2AbsolutePath;
	}

	public void setField2AbsolutePath(String field2AbsolutePath) {
		this.field2AbsolutePath = field2AbsolutePath;
	}

	public String getField2RelativePath() {
		return field2RelativePath;
	}

	public void setField2RelativePath(String field2RelativePath) {
		this.field2RelativePath = field2RelativePath;
	}

	public SecondLevel[] getSecondLevelArrayJPath() {
		return secondLevelArrayJPath;
	}

	public void setSecondLevelArrayJPath(SecondLevel[] secondLevelArrayJPath) {
		this.secondLevelArrayJPath = secondLevelArrayJPath;
	}

	public Iterable<SecondLevel> getSecondLevelIterableJPath() {
		return secondLevelIterableJPath;
	}

	public void setSecondLevelIterableJPath(Iterable<SecondLevel> secondLevelIterableJPath) {
		this.secondLevelIterableJPath = secondLevelIterableJPath;
	}

}


