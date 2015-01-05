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
}


