package com.elasticpath.rest.sdk.totals;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import com.elasticpath.rest.sdk.annotations.Json;
import com.elasticpath.rest.sdk.annotations.Zoom;
import com.elasticpath.rest.sdk.model.Linkable;

@Zoom({"defaultcart", "total"})
public class TotalZoom extends Linkable {

	@Json("$._defaultcart[0]._total[0].cost[0].amount")
	public String amount;

	@Json("$._defaultcart[0]._total[0].cost[0].currency")
	public String currency;

	@Json("$._defaultcart[0]._total[0].cost[0].display")
	public String display;

	@Override
	public String toString() {
		return reflectionToString(this, MULTI_LINE_STYLE);
	}
}
