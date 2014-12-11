package com.elasticpath.rest.json.unmarshalling.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;

public class MixedAnnotationsSuperClass {

	@JsonPath("$.links")
	private Iterable<Link> links;

	@JsonProperty("self")
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
