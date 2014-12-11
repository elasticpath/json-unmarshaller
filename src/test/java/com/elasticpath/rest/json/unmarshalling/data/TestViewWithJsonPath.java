package com.elasticpath.rest.json.unmarshalling.data;


import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;

public class TestViewWithJsonPath {
	@JsonPath("$.links[?(@.rel == 'discount')].type")
	private Iterable<String> type;

	public String getType() {
		return type.iterator().next();
	}
}