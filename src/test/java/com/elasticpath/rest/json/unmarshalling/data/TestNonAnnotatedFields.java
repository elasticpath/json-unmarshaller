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
	public int primitive;
	public Integer integer;
	public String string;
	public String[] strArray;
	public int[] intArray;
	public boolean primitiveBoolean;

	//these fields should be set, because they match Json nodes
	public int firstInt;
	public Integer lastInt;
	public String firstString;
	public String lastString;
	public Boolean firstBoolean;
	public String[] stringArray;
	public int[] integerArray;
	public boolean lastBoolean;

	public NonAnnotatedField nonAnnotatedField;
}


