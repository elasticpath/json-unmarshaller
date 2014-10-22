package com.elasticpath.rest.client.deserialization;

import com.elasticpath.rest.client.annotations.JsonPath;
import com.elasticpath.rest.client.annotations.RelationPath;
import com.elasticpath.rest.client.annotations.Zoom;

@Zoom(
		@RelationPath("total")
)
public class ZoomWithParent extends Linkable {

	@JsonPath("$._total[0].cost[0].currency")
	private String currency;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
