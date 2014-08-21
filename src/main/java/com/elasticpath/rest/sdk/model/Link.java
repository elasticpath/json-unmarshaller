package com.elasticpath.rest.sdk.model;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class Link {

	public String href;
	public String rel;
	public String type;
	public String uri;

	@Override
	public String toString() {
		return reflectionToString(this, MULTI_LINE_STYLE);
	}
}
