package com.elasticpath.rest.sdk.model;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class Linkable {

	private Iterable<Link> links;

	private Self self;

	public Self getSelf() {
		return self;
	}

	public void setSelf(Self self) {
		this.self = self;
	}

	public Iterable<Link> getLinks() {
		return links;
	}

	public void setLinks(Iterable<Link> links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return reflectionToString(this, MULTI_LINE_STYLE);
	}
}
