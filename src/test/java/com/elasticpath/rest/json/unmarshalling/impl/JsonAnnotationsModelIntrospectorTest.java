/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.impl;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import com.elasticpath.rest.json.unmarshalling.data.multi_level.FifthLevel;
import com.elasticpath.rest.json.unmarshalling.data.multi_level.FourthLevel;

/**
 * Tests for {@link JsonAnnotationsModelIntrospector}.
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonAnnotationsModelIntrospectorTest {

	private JsonAnnotationsModelIntrospector fixture;

	@Before
	public void setUp() {
		fixture = new JsonAnnotationsModelIntrospector();
	}

	@Test
	public void shouldReturnTrueWhenJsonPathAnnoationIsFoundInField() {
		assert fixture.hasJsonPathAnnotatedFields(FourthLevel.class);
	}

	@Test
	public void shouldReturnFalseWhenJsonPathAnnoationIsNotFoundInField() {
		assert !fixture.hasJsonPathAnnotatedFields(FifthLevel.class);
	}
}
