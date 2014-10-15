package com.elasticpath.rest.client.deserialization;

import com.elasticpath.rest.client.annotations.JsonPath;
import com.elasticpath.rest.client.annotations.RelationPath;
import com.elasticpath.rest.client.annotations.Zoom;

@Zoom({
		@RelationPath({"discount"})
})
public class Discount {
	@JsonPath("$.links[?(@.rel == 'discount')][0].type")
	private String type;
}