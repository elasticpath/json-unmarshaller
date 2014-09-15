package com.elasticpath.rest.client.zoom;

import static com.google.common.collect.Iterables.transform;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

import com.elasticpath.rest.client.zoom.model.RelationPathModel;
import com.elasticpath.rest.client.zoom.model.ZoomModel;

@Named
@Singleton
public class ZoomQueryFactory {

	public String create(ZoomModel zoom) {

		Iterable<RelationPathModel> relationPaths = zoom.getRelationPaths();

		return joinZooms(
				transform(relationPaths, new Function<RelationPathModel, String>() {
					public String apply(RelationPathModel relationPath) {
						return joinRelationPaths(relationPath.getRelations());
					}
				})
		);
	}

	private String joinRelationPaths(Iterable<String> relationPaths) {

		return Joiner.on(":")
				.join(relationPaths);
	}

	private String joinZooms(Iterable<String> zooms) {

		return Joiner.on(",")
				.join(zooms);
	}
}
