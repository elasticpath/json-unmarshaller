package com.elasticpath.rest.client.zoom;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.common.base.Function;

import com.elasticpath.rest.client.annotations.RelationPath;
import com.elasticpath.rest.client.annotations.Zoom;
import com.elasticpath.rest.client.zoom.model.RelationPathModel;
import com.elasticpath.rest.client.zoom.model.ZoomModel;

@Named
@Singleton
public class ZoomModelIntrospector {

	public ZoomModel createZoomModel(Class<?> zoomClass) {

		checkArgument(isZoom(zoomClass));

		Iterable<RelationPath> relationPaths;
		if (isSingleZoom(zoomClass)) {
			relationPaths = asList(
					zoomClass.getAnnotation(RelationPath.class)
			);
		} else {
			relationPaths = asList(
					zoomClass.getAnnotation(Zoom.class)
							.value()
			);
		}

		return new ZoomModel(
				toRelationPaths(relationPaths)
		);
	}

	private Iterable<RelationPathModel> toRelationPaths(Iterable<RelationPath> relationPaths) {
		return transform(relationPaths, new Function<RelationPath, RelationPathModel>() {
			public RelationPathModel apply(RelationPath relationPath) {
				return new RelationPathModel(relationPath.value());
			}
		});
	}

	public boolean isZoom(Class<?> zoomClass) {
		return isSingleZoom(zoomClass) || isMultiZoom(zoomClass);
	}

	private boolean isSingleZoom(Class<?> zoomClass) {
		return zoomClass.isAnnotationPresent(RelationPath.class);
	}

	private boolean isMultiZoom(Class<?> zoomClass) {
		return zoomClass.isAnnotationPresent(Zoom.class);
	}
}
