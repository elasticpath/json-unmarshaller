package com.elasticpath.rest.client.urlbuilding.zoom;

import static com.google.common.collect.Iterables.transform;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

import com.elasticpath.rest.client.urlbuilding.zoom.model.RelationPathModel;
import com.elasticpath.rest.client.urlbuilding.zoom.model.ZoomModel;


@Named
@Singleton
public class ZoomQueryFactory {

	private final Joiner relationJoiner = Joiner.on(":");
	private final Joiner relationPathJoiner = Joiner.on(",");

	public String create(ZoomModel zoom) {

		Iterable<RelationPathModel> relationPaths = zoom.getRelationPaths();

		return joinRelationPaths(
				transform(relationPaths, new Function<RelationPathModel, String>() {
					public String apply(RelationPathModel relationPath) {
						return joinRelations(relationPath.getRelations());
					}
				})
		);
	}

	private String joinRelations(Iterable<String> relations) {

		return relationJoiner
				.join(relations);
	}

	private String joinRelationPaths(Iterable<String> relationPaths) {

		return relationPathJoiner
				.join(relationPaths);
	}
}
