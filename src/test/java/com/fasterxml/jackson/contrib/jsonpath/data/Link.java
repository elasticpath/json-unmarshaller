package com.fasterxml.jackson.contrib.jsonpath.data;

/**
 * Test data class.
 */
public class Link {

	private String href;
	private String rel;
	private String type;
	private String uri;

	public String getHref() {
		return href;
	}

	public void setHref(final String href) {
		this.href = href;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(final String rel) {
		this.rel = rel;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(final String uri) {
		this.uri = uri;
	}
}
