package com.elasticpath.rest.json.unmarshalling.data;


import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;

public class TestViewWithBadAnnotations {

	@JsonProperty("links")
	@JsonPath("$.links[?(@.rel == 'discount')].type")
	private Iterable<String> type;

	public String getType() {
		return type.iterator().next();
	}
}