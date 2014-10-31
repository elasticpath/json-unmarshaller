package com.elasticpath.rest.client.zoom

import static groovy.test.GroovyAssert.shouldFail

import org.junit.Test

import com.elasticpath.rest.client.urlbuilding.annotations.RelationPath
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom
import com.elasticpath.rest.client.urlbuilding.zoom.ZoomModelIntrospector


class ZoomModelIntrospectorTest {

	ZoomModelIntrospector introspector = new ZoomModelIntrospector()

	@Test
	void 'Given no zoom annotations present, when introspecting, should fast fail'() {
		shouldFail(IllegalArgumentException) {
			introspector.createZoomModel(LookNoZoom)
		}
	}

	@Test
	void 'Given single relation path specified, when introspecting, should create model representing a single zoom endpoint'() {
		def result = introspector.createZoomModel(SingleZoom)

		assert [['defaultcart', 'total']] == result.relationPaths.toList().relations
	}

	@Test
	void 'Given multiple relation paths specified, when introspecting, should create model representing multiple zoom endpoints'() {

		def result = introspector.createZoomModel(MultiZoom)

		assert [['defaultcart', 'total'], ['defaultcart', 'lineitem']] == result.relationPaths.toList().relations
	}

	public class LookNoZoom {

	}

	@RelationPath(['defaultcart', 'total'])
	public class SingleZoom {

	}

	@Zoom([
	@RelationPath(['defaultcart', 'total']),
	@RelationPath(['defaultcart', 'lineitem'])
	])
	public class MultiZoom {

	}
}
