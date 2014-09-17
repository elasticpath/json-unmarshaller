package com.elasticpath.rest.client.zoom

import static org.mockito.BDDMockito.given
import static org.mockito.Matchers.any
import static org.mockito.Mockito.verify

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import com.elasticpath.rest.client.zoom.model.ZoomModel

@RunWith(MockitoJUnitRunner)
class ZoomUrlFactoryImplTest {

	@Mock
	ZoomModelIntrospector zoomModelIntrospector

	@Mock
	ZoomQueryFactory zoomQueryFactory

	@InjectMocks
	ZoomUrlFactoryImpl zoomUrlFactory

	Class<?> irrelevantClass = null

	@Before
	void setUp() {
		given(zoomQueryFactory.create(any(ZoomModel)))
				.willReturn('defaultcart:lineitems,defaultcart:total')
	}

	@Test
	void 'Given no zoom, when creating cortex url, should require no changes'() {

		def sampleUrl = 'sampleUrl'
		given(zoomModelIntrospector.isZoom(irrelevantClass))
				.willReturn(false)

		def result = zoomUrlFactory.create(sampleUrl, irrelevantClass)

		assert sampleUrl == result
	}

	@Test
	void 'Given a zoom, when creating cortex url, should create zoom query'() {

		given(zoomModelIntrospector.isZoom(irrelevantClass))
				.willReturn(true)

		zoomUrlFactory.create('', irrelevantClass)

		verify(zoomQueryFactory).create(any(ZoomModel))
	}

	@Test
	void 'Given a zoom, when creating cortex url, should return zoom query'() {

		given(zoomModelIntrospector.isZoom(irrelevantClass))
				.willReturn(true)

		def result = zoomUrlFactory.create('', irrelevantClass)

		assert result.startsWith('?zoom=')
	}
}
