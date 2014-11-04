package com.elasticpath.rest.client.unmarshalling;


import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.RelationPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;

@Zoom({
		@RelationPath({"discount"})
})
public class Discount {
	@JsonPath("$.links[?(@.rel == 'discount')].type")
	private Iterable<String> type;

	public String getType() {
		return type.iterator().next();
	}
}