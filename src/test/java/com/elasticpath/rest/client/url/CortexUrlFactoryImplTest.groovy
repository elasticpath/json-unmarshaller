package com.elasticpath.rest.client.url
import static org.mockito.BDDMockito.given
import static org.mockito.Matchers.any
import static org.mockito.Mockito.verify

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import com.elasticpath.rest.client.annotations.EntryPointUri
import com.elasticpath.rest.client.annotations.FollowLocation
import com.elasticpath.rest.client.annotations.RelationPath
import com.elasticpath.rest.client.annotations.Zoom
import com.elasticpath.rest.client.zoom.ZoomModelIntrospector
import com.elasticpath.rest.client.zoom.ZoomQueryFactory
import com.elasticpath.rest.client.zoom.model.ZoomModel

@RunWith(MockitoJUnitRunner)
class CortexUrlFactoryImplTest {

	@Mock
	ZoomModelIntrospector zoomModelIntrospector

	@Mock
	ZoomQueryFactory zoomQueryFactory

	@InjectMocks
	CortexUrlFactoryImpl cortexUrlFactory

	def baseUrl = "http://test.com/cortex"

	@Before
	void setUp() {
		given(zoomQueryFactory.create(any(ZoomModel)))
				.willReturn('defaultcart:lineitems,defaultcart:total')
	}

	@Test
	void 'Given no zoom, when creating cortex url, should require no changes'() {

		def sampleUrl = 'sampleUrl'
		given(zoomModelIntrospector.isZoomPresent(NoAnnotationsPresent.class))
				.willReturn(false)

		def result = cortexUrlFactory.create(sampleUrl, NoAnnotationsPresent.class)

		assert sampleUrl == result
	}

	@Test
	void 'Given a zoom, when creating cortex url, should create zoom query'() {

		given(zoomModelIntrospector.isZoomPresent(ZoomPresent.class))
				.willReturn(true)

		cortexUrlFactory.create('', ZoomPresent.class)

		verify(zoomQueryFactory).create(any(ZoomModel))
	}

	@Test
	void 'Given a zoom, when creating cortex url, should return zoom query'() {

		given(zoomModelIntrospector.isZoomPresent(ZoomPresent.class))
				.willReturn(true)

		def result = cortexUrlFactory.create('', ZoomPresent.class)

		assert result.startsWith('?zoom=')
	}

	@Test
	void 'Given a follow location, when creating cortex url, should return follow location query'() {

		given(zoomModelIntrospector.isZoomPresent(FollowLocationPresent.class))
				.willReturn(false)

		def result = cortexUrlFactory.create('', FollowLocationPresent.class)

		assert result.startsWith('?followLocation=')
	}

	@Test
	void 'Given a follow location and zoom, when creating cortex url, should return zoom query'() {

		given(zoomModelIntrospector.isZoomPresent(FollowAndZoomPresent.class))
				.willReturn(true)

		def result = cortexUrlFactory.create('', FollowAndZoomPresent.class)

		assert result.contains('zoom=') && result.contains('followLocation')
	}

	@Test
	void 'Given an object annotated with EntryPointUri, scope should correctly be placed in the url'() {

		given(zoomModelIntrospector.isZoomPresent(FollowAndZoomPresent.class))
				.willReturn(false)

		def result = cortexUrlFactory.createFromAnnotationsAndScope(baseUrl, "scoped", EntryPointUriPresent.class)

		assert result.equals(baseUrl + '/im/a/scoped/uri')
	}

	@Test
	void 'Given the resourcePath and an annotated Object, cortex url should be created correctly'() {

		given(zoomModelIntrospector.isZoomPresent(FollowAndZoomPresent.class))
				.willReturn(false)

		def result = cortexUrlFactory.createFromAnnotationsAndResourcePath(baseUrl, "/blah/blah", NoAnnotationsPresent.class)

		assert result.equals(baseUrl + '/blah/blah')
	}


	class NoAnnotationsPresent {}

	@Zoom(@RelationPath("blah"))
	class ZoomPresent {}

	@FollowLocation
	class FollowLocationPresent {}

	@Zoom(@RelationPath("blah"))
	@FollowLocation
	class FollowAndZoomPresent {}

	@EntryPointUri(["im", "a", EntryPointUri.SCOPE, "uri"])
	class EntryPointUriPresent {}
}
