/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data;

import com.elasticpath.rest.json.unmarshalling.data.multi_level.NonAnnotatedField;

public class TestNonAnnotatedFields {

	//these fields should be ignored; don't match any Json node
	int primitive;
	Integer integer;
	String string;
	String[] strArray;
	int[] intArray;
	boolean primitiveBoolean;

	//these fields should be set, because they match Json nodes
	int firstInt;
	Integer lastInt;
	String firstString;
	String lastString;
	Boolean firstBoolean;
	String[] stringArray;
	int[] integerArray;
	boolean lastBoolean;

	NonAnnotatedField nonAnnotatedField;

	public int getPrimitive() {
		return primitive;
	}

	public void setPrimitive(int primitive) {
		this.primitive = primitive;
	}

	public Integer getInteger() {
		return integer;
	}

	public void setInteger(Integer integer) {
		this.integer = integer;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public String[] getStrArray() {
		return strArray;
	}

	public void setStrArray(String[] strArray) {
		this.strArray = strArray;
	}

	public int[] getIntArray() {
		return intArray;
	}

	public void setIntArray(int[] intArray) {
		this.intArray = intArray;
	}

	public boolean isPrimitiveBoolean() {
		return primitiveBoolean;
	}

	public void setPrimitiveBoolean(boolean primitiveBoolean) {
		this.primitiveBoolean = primitiveBoolean;
	}

	public int getFirstInt() {
		return firstInt;
	}

	public void setFirstInt(int firstInt) {
		this.firstInt = firstInt;
	}

	public Integer getLastInt() {
		return lastInt;
	}

	public void setLastInt(Integer lastInt) {
		this.lastInt = lastInt;
	}

	public String getFirstString() {
		return firstString;
	}

	public void setFirstString(String firstString) {
		this.firstString = firstString;
	}

	public String getLastString() {
		return lastString;
	}

	public void setLastString(String lastString) {
		this.lastString = lastString;
	}

	public Boolean getFirstBoolean() {
		return firstBoolean;
	}

	public void setFirstBoolean(Boolean firstBoolean) {
		this.firstBoolean = firstBoolean;
	}

	public String[] getStringArray() {
		return stringArray;
	}

	public void setStringArray(String[] stringArray) {
		this.stringArray = stringArray;
	}

	public int[] getIntegerArray() {
		return integerArray;
	}

	public void setIntegerArray(int[] integerArray) {
		this.integerArray = integerArray;
	}

	public boolean isLastBoolean() {
		return lastBoolean;
	}

	public void setLastBoolean(boolean lastBoolean) {
		this.lastBoolean = lastBoolean;
	}

	public NonAnnotatedField getNonAnnotatedField() {
		return nonAnnotatedField;
	}

	public void setNonAnnotatedField(NonAnnotatedField nonAnnotatedField) {
		this.nonAnnotatedField = nonAnnotatedField;
	}


}


