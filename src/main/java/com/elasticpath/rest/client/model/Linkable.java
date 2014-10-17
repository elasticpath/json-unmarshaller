package com.elasticpath.rest.client.model;

import java.util.Collection;

import com.elasticpath.rest.client.annotations.JsonPath;

public class Linkable {

	@JsonPath("$.links")
	private Collection<Link> links;

	@JsonPath("$.self")
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

	public void setLinks(Collection<Link> links) {
		this.links = links;
	}
}
