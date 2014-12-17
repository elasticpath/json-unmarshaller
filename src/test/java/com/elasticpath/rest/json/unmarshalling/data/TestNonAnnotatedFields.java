/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data;

import com.elasticpath.rest.json.unmarshalling.data.multi_level.NonAnnotatedField;

/**
 * Test data class.
 */
@SuppressWarnings("PMD")
public class TestNonAnnotatedFields {

	//these fields should be ignored; don't match any Json node
	private int primitive;
	private Integer integer;
	private String string;
	private String[] strArray;
	private int[] intArray;
	private boolean primitiveBoolean;

	//these fields should be set, because they match Json nodes
	private int firstInt;
	private Integer lastInt;
	private String firstString;
	private String lastString;
	private Boolean firstBoolean;
	private String[] stringArray;
	private int[] integerArray;
	private boolean lastBoolean;

	private NonAnnotatedField nonAnnotatedField;

	public int getPrimitive() {
		return primitive;
	}

	public void setPrimitive(final int primitive) {
		this.primitive = primitive;
	}

	public Integer getInteger() {
		return integer;
	}

	public void setInteger(final Integer integer) {
		this.integer = integer;
	}

	public String getString() {
		return string;
	}

	public void setString(final String string) {
		this.string = string;
	}

	public String[] getStrArray() {
		return strArray;
	}

	public void setStrArray(final String[] strArray) {
		this.strArray = strArray;
	}

	public int[] getIntArray() {
		return intArray;
	}

	public void setIntArray(final int[] intArray) {
		this.intArray = intArray;
	}

	public boolean isPrimitiveBoolean() {
		return primitiveBoolean;
	}

	public void setPrimitiveBoolean(final boolean primitiveBoolean) {
		this.primitiveBoolean = primitiveBoolean;
	}

	public int getFirstInt() {
		return firstInt;
	}

	public void setFirstInt(final int firstInt) {
		this.firstInt = firstInt;
	}

	public Integer getLastInt() {
		return lastInt;
	}

	public void setLastInt(final Integer lastInt) {
		this.lastInt = lastInt;
	}

	public String getFirstString() {
		return firstString;
	}

	public void setFirstString(final String firstString) {
		this.firstString = firstString;
	}

	public String getLastString() {
		return lastString;
	}

	public void setLastString(final String lastString) {
		this.lastString = lastString;
	}

	public Boolean getFirstBoolean() {
		return firstBoolean;
	}

	public void setFirstBoolean(final Boolean firstBoolean) {
		this.firstBoolean = firstBoolean;
	}

	public String[] getStringArray() {
		return stringArray;
	}

	public void setStringArray(final String[] stringArray) {
		this.stringArray = stringArray;
	}

	public int[] getIntegerArray() {
		return integerArray;
	}

	public void setIntegerArray(final int[] integerArray) {
		this.integerArray = integerArray;
	}

	public boolean isLastBoolean() {
		return lastBoolean;
	}

	public void setLastBoolean(final boolean lastBoolean) {
		this.lastBoolean = lastBoolean;
	}

	public NonAnnotatedField getNonAnnotatedField() {
		return nonAnnotatedField;
	}

	public void setNonAnnotatedField(final NonAnnotatedField nonAnnotatedField) {
		this.nonAnnotatedField = nonAnnotatedField;
	}


}


