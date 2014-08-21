package com.elasticpath.rest.sdk.model;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class Linkable {

	public Iterable<Link> links;

	public Self self;

	@Override
	public String toString() {
		return reflectionToString(this, MULTI_LINE_STYLE);
	}
}
