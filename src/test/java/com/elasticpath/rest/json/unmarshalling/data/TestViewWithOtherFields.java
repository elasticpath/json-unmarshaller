package com.elasticpath.rest.json.unmarshalling.data;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;

public class TestViewWithOtherFields {

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
