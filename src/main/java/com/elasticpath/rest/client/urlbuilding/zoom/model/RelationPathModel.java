package com.elasticpath.rest.client.urlbuilding.zoom.model;

import static java.util.Arrays.asList;

public class RelationPathModel {

	private Iterable<String> relations;

	public RelationPathModel(String[] relations) {
		this.relations = asList(relations);
	}

	public Iterable<String> getRelations() {
		return relations;
	}
}
