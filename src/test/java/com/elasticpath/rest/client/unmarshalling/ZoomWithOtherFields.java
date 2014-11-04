package com.elasticpath.rest.client.unmarshalling;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.RelationPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;

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
