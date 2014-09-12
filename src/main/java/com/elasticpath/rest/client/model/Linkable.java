package com.elasticpath.rest.client.model;

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
}
