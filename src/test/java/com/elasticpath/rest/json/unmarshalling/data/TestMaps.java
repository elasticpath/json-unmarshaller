/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;

/**
 * Test data class for multiple Json levels.
 */
@SuppressWarnings("PMD")
public class TestMaps {

	@JsonPath("$.countryMap")
	public Map<String, List<String>> mapOfCountries;

	public Hashtable<String, List<String>> countryMap;
}


