package com.elasticpath.rest.client.zoom

import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.InjectMocks
import org.mockito.runners.MockitoJUnitRunner

import com.elasticpath.rest.client.urlbuilding.zoom.ZoomQueryFactory
import com.elasticpath.rest.client.urlbuilding.zoom.model.RelationPathModel
import com.elasticpath.rest.client.urlbuilding.zoom.model.ZoomModel


@RunWith(MockitoJUnitRunner)
class ZoomQueryFactoryTest {

	@InjectMocks
	ZoomQueryFactory factory

	def defaultCart = 'defaultcart'
	def lineItems = 'lineitems'
	def totals = 'totals'

	@Test
	void 'Given single zoom, when creating zoom query, should build correct zoom'() {
		def singleZoom = [
				[defaultCart, lineItems],
		] as String[][]
		def model = new ZoomModel(singleZoom.collect { path -> new RelationPathModel(path) })

		def result = factory.create(model)

		assert "$defaultCart:$lineItems" == result
	}

	@Test
	void 'Given multi zoom, when creating zoom query, should build correct zoom'() {
		def singleZoom = [
				[defaultCart, totals],
				[defaultCart, lineItems],
		] as String[][]
		def model = new ZoomModel(singleZoom.collect { path -> new RelationPathModel(path) })

		def result = factory.create(model)

		assert "$defaultCart:$totals,$defaultCart:$lineItems" == result
	}
}
