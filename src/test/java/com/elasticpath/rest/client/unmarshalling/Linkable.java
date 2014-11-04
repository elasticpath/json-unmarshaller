package com.elasticpath.rest.client.unmarshalling;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;

public class Linkable {

	@JsonPath("$.links")
	private Iterable<Link> links;

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

	public void setLinks(Iterable<Link> links) {
		this.links = links;
	}
}
