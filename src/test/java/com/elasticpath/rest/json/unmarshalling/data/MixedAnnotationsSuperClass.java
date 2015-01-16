package com.elasticpath.rest.json.unmarshalling.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.json.unmarshalling.annotations.JsonPath;

/**
 * Test data superclass with mixed annotation types.
 */
public class MixedAnnotationsSuperClass {

	@JsonPath("$.links")
	private Iterable<Link> links;

	@JsonProperty("self")
	private Self self;

	public Self getSelf() {
		return self;
	}

	public void setSelf(final Self self) {
		this.self = self;
	}

	public Iterable<Link> getLinks() {
		return links;
	}

	public void setLinks(final Iterable<Link> links) {
		this.links = links;
	}
}
