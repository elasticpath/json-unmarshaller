package com.elasticpath.rest.client.deserialization;

import com.elasticpath.rest.client.annotations.JsonPath;
import com.elasticpath.rest.client.annotations.RelationPath;
import com.elasticpath.rest.client.annotations.Zoom;

@Zoom(
		@RelationPath("total")
)
public class ZoomWithOtherFields {

	@JsonPath("$._total[0].cost[0].currency")
	private String currency;

	private String notForDeserialization;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getNotForDeserialization() {
		return notForDeserialization;
	}

	public void setNotForDeserialization(String notForDeserialization) {
		this.notForDeserialization = notForDeserialization;
	}
}
