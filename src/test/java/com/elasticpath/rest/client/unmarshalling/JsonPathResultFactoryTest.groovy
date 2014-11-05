package com.elasticpath.rest.client.unmarshalling
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import static org.mockito.BDDMockito.given

import org.junit.Test
import org.junit.runner.RunWith

import com.fasterxml.jackson.databind.ObjectMapper

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.runners.MockitoJUnitRunner

import com.elasticpath.rest.client.unmarshalling.data.TestViewWithBadAnnotations
import com.elasticpath.rest.client.unmarshalling.data.TestViewWithJsonPath
import com.elasticpath.rest.client.unmarshalling.data.TestViewWithOtherFields
import com.elasticpath.rest.client.unmarshalling.data.TestViewWithParent

@RunWith(MockitoJUnitRunner)
class JsonPathResultFactoryTest {

	@Mock
	ClassInstantiator classInstantiator

	@Spy
	JsonAnnotationsModelIntrospector jsonPathModelIntrospector = new JsonAnnotationsModelIntrospector();

	@Spy
	ObjectMapper objectMapper = new ObjectMapper().disable(FAIL_ON_UNKNOWN_PROPERTIES)

	@InjectMocks
	JsonPathResultFactory factory

	@Test
	void 'Given object with non-annotated fields, when unmarshalling, then should only unmarshal into annotated fields'() {
		def protectTheString = 'do not delete me!'
		def returnObject = new TestViewWithOtherFields(
				notForDeserialization: protectTheString
		)
		given(classInstantiator.newInstance(TestViewWithOtherFields))
				.willReturn(returnObject)

		factory.create(TestViewWithOtherFields, cartTotalZoomedJson)

		assert protectTheString == returnObject.notForDeserialization
	}

	@Test
	void 'Given object with superclass fields, when unmarshalling, then should unmarshal superclass fields'() {
		def returnObject = new TestViewWithParent()
		given(classInstantiator.newInstance(TestViewWithParent))
				.willReturn(returnObject)

		def result = factory.create(TestViewWithParent, cartTotalZoomedJson)

		assert '/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu=?zoom=total' == result.self.uri
	}


	@Test
	void 'Given multiple rels to choose from, select the correct one based on a query'() {
		def returnObject = new TestViewWithJsonPath()
		given(classInstantiator.newInstance(TestViewWithJsonPath))
				.willReturn(returnObject)

		def result = factory.create(TestViewWithJsonPath, cartTotalZoomedJson)

		assert 'elasticpath.discounts.discount' == result.type
	}

	@Test(expected = IllegalStateException)
	void 'Given bad usage of JsonPath and JsonProperty, expect an error'() {
		def returnObject = new TestViewWithBadAnnotations()
		given(classInstantiator.newInstance(TestViewWithBadAnnotations))
				.willReturn(returnObject)

		def result = factory.create(TestViewWithBadAnnotations, cartTotalZoomedJson)
	}



	def cartTotalZoomedJson = '''
{
  "self": {
    "type": "elasticpath.carts.cart",
    "uri": "/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu=?zoom=total",
    "href": "http://localhost:9080/cortex/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu=?zoom=total",
    "max-age": 0
  },
  "links": [
    {
      "rel": "lineitems",
      "rev": "cart",
      "type": "elasticpath.collections.links",
      "uri": "/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu=/lineitems",
      "href": "http://localhost:9080/cortex/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu=/lineitems"
    },
    {
      "rel": "discount",
      "type": "elasticpath.discounts.discount",
      "uri": "/discounts/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu=",
      "href": "http://localhost:9080/cortex/discounts/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu="
    },
    {
      "rel": "order",
      "rev": "cart",
      "type": "elasticpath.orders.order",
      "uri": "/orders/geometrixx/gy2tgm3fmy4tsljzhezdkljugmytqllbg5tdeljxgaywembzmfsgezdegi=",
      "href": "http://localhost:9080/cortex/orders/geometrixx/gy2tgm3fmy4tsljzhezdkljugmytqllbg5tdeljxgaywembzmfsgezdegi="
    },
    {
      "rel": "appliedpromotions",
      "type": "elasticpath.collections.links",
      "uri": "/promotions/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu=/applied",
      "href": "http://localhost:9080/cortex/promotions/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu=/applied"
    },
    {
      "rel": "total",
      "rev": "cart",
      "type": "elasticpath.totals.total",
      "uri": "/totals/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu=",
      "href": "http://localhost:9080/cortex/totals/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu="
    }
  ],
  "_total": [
    {
      "self": {
        "type": "elasticpath.totals.total",
        "uri": "/totals/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu=",
        "href": "http://localhost:9080/cortex/totals/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu=",
        "max-age": 0
      },
      "links": [
        {
          "rel": "cart",
          "rev": "total",
          "type": "elasticpath.carts.cart",
          "uri": "/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu=",
          "href": "http://localhost:9080/cortex/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu="
        }
      ],
      "cost": [
        {
          "amount": 0,
          "currency": "USD",
          "display": "$0.00"
        }
      ]
    }
  ],
  "total-quantity": 0
}
'''
}
