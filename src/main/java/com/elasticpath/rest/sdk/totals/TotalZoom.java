package com.elasticpath.rest.sdk.totals;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import com.elasticpath.rest.sdk.annotations.JPath;
import com.elasticpath.rest.sdk.annotations.Zoom;

@Zoom({"defaultcart", "total"})
@Zoom({"defaultcart", "discount"})
public class TotalZoom {

	@JPath("$._defaultcart[0]._discount[0].discount[0].amount")
	public String discountAmount;

	@JPath("$._defaultcart[0]._discount[0].discount[0].currency")
	public String discountCurrency;

	@JPath("$._defaultcart[0]._discount[0].discount[0].display")
	public String discountDisplay;

	@JPath("$._defaultcart[0]._total[0].cost[0].amount")
	public String totalAmount;

	@JPath("$._defaultcart[0]._total[0].cost[0].currency")
	public String totalCurrency;

	@JPath("$._defaultcart[0]._total[0].cost[0].display")
	public String totalDisplay;

	@Override
	public String toString() {
		return reflectionToString(this, MULTI_LINE_STYLE);
	}
}
