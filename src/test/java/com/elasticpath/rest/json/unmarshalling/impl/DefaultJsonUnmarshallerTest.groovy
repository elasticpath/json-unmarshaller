package com.elasticpath.rest.json.unmarshalling.impl

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import static org.mockito.BDDMockito.given

import org.junit.Test
import org.junit.runner.RunWith

import com.fasterxml.jackson.databind.ObjectMapper

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.runners.MockitoJUnitRunner

import com.elasticpath.rest.json.unmarshalling.data.TestMultiLevels
import com.elasticpath.rest.json.unmarshalling.data.TestNonAnnotatedFields
import com.elasticpath.rest.json.unmarshalling.data.TestViewWithBadAnnotations
import com.elasticpath.rest.json.unmarshalling.data.TestViewWithJsonPath
import com.elasticpath.rest.json.unmarshalling.data.TestViewWithOtherFields
import com.elasticpath.rest.json.unmarshalling.data.TestViewWithParent

/**
 * Tests for {@link DefaultJsonUnmarshaller}
 */
@RunWith(MockitoJUnitRunner)
class DefaultJsonUnmarshallerTest {

	@Mock
	ClassInstantiator classInstantiator

	@Spy
	JsonAnnotationsModelIntrospector jsonPathModelIntrospector = new JsonAnnotationsModelIntrospector();

	@Spy
	ObjectMapper objectMapper = new ObjectMapper().disable(FAIL_ON_UNKNOWN_PROPERTIES)

	@InjectMocks
	DefaultJsonUnmarshaller factory

	@Test
	void 'Given object with non-annotated fields, when unmarshalling, then should only unmarshal into annotated fields'() {
		def protectTheString = 'do not delete me!'
		def returnObject = new TestViewWithOtherFields(
				notForDeserialization: protectTheString
		)
		given(classInstantiator.newInstance(TestViewWithOtherFields))
				.willReturn(returnObject)

		factory.unmarshall(TestViewWithOtherFields, cartTotalZoomedJson)

		assert protectTheString == returnObject.notForDeserialization
	}

	@Test
	void 'Given object with superclass fields, when unmarshalling, then should unmarshal superclass fields'() {
		def returnObject = new TestViewWithParent()
		given(classInstantiator.newInstance(TestViewWithParent))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestViewWithParent, cartTotalZoomedJson)

		assert '/carts/geometrixx/gy4gemzsgzqwkllggyygcljumvstsllbga2dgllbgm4dgmjygftdiztemu=?zoom=total' == result.self.uri
	}


	@Test
	void 'Given multiple rels to choose from, select the correct one based on a query'() {
		def returnObject = new TestViewWithJsonPath()
		given(classInstantiator.newInstance(TestViewWithJsonPath))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestViewWithJsonPath, cartTotalZoomedJson)

		assert 'elasticpath.discounts.discount' == result.type
	}


	@Test(expected = IllegalStateException)
	void 'Given bad usage of JsonPath and JsonProperty, expect an error'() {
		def returnObject = new TestViewWithBadAnnotations()
		given(classInstantiator.newInstance(TestViewWithBadAnnotations))
				.willReturn(returnObject)

		factory.unmarshall(TestViewWithBadAnnotations, cartTotalZoomedJson)
	}

	@Test
	void 'Given object with complex structure (multiple levels) should unmarshall fields without any Json annotation to type default values'() {
		def returnObject = new TestMultiLevels()

		given(classInstantiator.newInstance(TestMultiLevels))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestMultiLevels, multiLevelJson)

		assert 0 == result.getInt1();
		assert 0 == result.getChar5()
		assert null == result.getInteger2()
		assert null == result.getString3()
		assert null == result.getByteArray4()
	}

	@Test
	void 'Given object with complex structure (multiple levels) should unmarshall all second-level annotated fields properly'() {
		def returnObject = new TestMultiLevels()

		given(classInstantiator.newInstance(TestMultiLevels))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestMultiLevels, multiLevelJson)

		// ============ assert second level ===================
		def secondLevelJProperty = result.secondLevelJProperty

		assert null != secondLevelJProperty

		assert '2nd field1' == secondLevelJProperty.field1
		assert '2nd field2' == secondLevelJProperty.field2
		assert '2nd field3' == secondLevelJProperty.field3
		assert '2nd field4' == secondLevelJProperty.field4
	}

	@Test
//testing 3 fields, using JsonProperty, absolute and relative JsonPath paths - all fields must be identical
	void 'In multi-level structure, unmarshalled fields of the same type should be identical if Json path resolves to a same path'() {
		def returnObject = new TestMultiLevels()

		given(classInstantiator.newInstance(TestMultiLevels))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestMultiLevels, multiLevelJson)

		// ============ assert second level ===================
		def secondLevelJProperty = result.secondLevelJProperty

		assert null != secondLevelJProperty

		def secondLevelAbsoluteJPath = result.secondLevelAbsoluteJPath
		def secondLevelRelativeJPath = result.secondLevelRelativeJPath

		assert null != secondLevelAbsoluteJPath
		assert null != secondLevelRelativeJPath

		assert secondLevelJProperty == secondLevelAbsoluteJPath
		assert secondLevelJProperty == secondLevelRelativeJPath
	}

	@Test
	void 'In multi-level structure array and iterable objects should be the same when annotated with same JsonProperty'() {
		def returnObject = new TestMultiLevels()

		given(classInstantiator.newInstance(TestMultiLevels))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestMultiLevels, multiLevelJson)

		// @@@@@@ assert second level arrays/iterables annotated with JsonProperty
		def secondLevelArrayJProperty = result.secondLevelArrayJProperty
		def secondLevelIterableJProperty = result.secondLevelIterableJProperty

		assert null != secondLevelArrayJProperty
		assert null != secondLevelIterableJProperty

		assert 3 == secondLevelArrayJProperty.size()
		assert 3 == secondLevelIterableJProperty.asCollection().size()

		assert secondLevelArrayJProperty as Collection == secondLevelIterableJProperty.asCollection()
	}

	@Test
//regardless of the annotations, if they all resolve to same path, arrays/iterables should contain same data
	void 'In multi-level structure array and iterable objects should be the same when annotated with same JsonPath and JsonProperty'() {
		def returnObject = new TestMultiLevels()

		given(classInstantiator.newInstance(TestMultiLevels))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestMultiLevels, multiLevelJson)

		// ============ assert second level ===================
		def secondLevelJProperty = result.secondLevelJProperty

		assert null != secondLevelJProperty

		def secondLevelIterableJProperty = result.secondLevelIterableJProperty

		// @@@@@@ assert second level arrays/iterables annotated with JsonPath
		def secondLevelArrayJPath = result.secondLevelArrayJPath
		def secondLevelIterableJPath = result.secondLevelIterableJPath

		assert null != secondLevelArrayJPath
		assert null != secondLevelIterableJPath

		assert 3 == secondLevelArrayJPath.size()
		assert 3 == secondLevelIterableJPath.asCollection().size()

		assert secondLevelArrayJPath as Collection == secondLevelIterableJPath.asCollection()

		//regardless of annotation, result must be the same
		assert secondLevelIterableJProperty.asCollection() == secondLevelIterableJPath.asCollection()
	}

	@Test
	void 'In multi-level structure, second level fields should be unmarshalled correctly'() {
		def returnObject = new TestMultiLevels()

		given(classInstantiator.newInstance(TestMultiLevels))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestMultiLevels, multiLevelJson)

		// @@@@@@ assert second level arrays/iterables annotated with JsonPath
		def secondLevelArrayJPath = result.secondLevelArrayJPath

		//assert content of one array element
		def secondLevelArraySecondElement = secondLevelArrayJPath[2]

		assert '2nd field1' == secondLevelArraySecondElement.field1
		assert '2nd field2[2]' == secondLevelArraySecondElement.field2
		assert '2nd field3[2]' == secondLevelArraySecondElement.field3
		assert '2nd field4[2]' == secondLevelArraySecondElement.field4

		//doesn't matter how 3rd level var is annotated
		def secondLevelArraySecondElement_3thLevel = secondLevelArraySecondElement.thirdLevelJsonProperty

		assert '3rd field1' == secondLevelArraySecondElement_3thLevel.field1
		assert '3rd field2[2]' == secondLevelArraySecondElement_3thLevel.field2
		assert '3rd field3[2]' == secondLevelArraySecondElement_3thLevel.field3
		assert '3rd field4[2]' == secondLevelArraySecondElement_3thLevel.field4

	}

	@Test
	void 'In multi-level structure, third-level fields should be unmarshalled correctly'() {
		def returnObject = new TestMultiLevels()

		given(classInstantiator.newInstance(TestMultiLevels))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestMultiLevels, multiLevelJson)

		// ============ assert second level ===================
		def secondLevelJProperty = result.secondLevelJProperty

		assert null != secondLevelJProperty

		// ============ assert third level =======================
		def thirdLevelJProperty = secondLevelJProperty.thirdLevelJsonProperty

		assert null != thirdLevelJProperty

		assert '3rd field1' == thirdLevelJProperty.field1
		assert '3rd field2' == thirdLevelJProperty.field2
		assert '3rd field3' == thirdLevelJProperty.field3
		assert '3rd field4' == thirdLevelJProperty.field4

		def thirdLevelAbsoluteJPath = secondLevelJProperty.thirdLevelAbsoluteJPath
		def thirdLevelRelativeJPath = secondLevelJProperty.thirdLevelRelativeJPath

		assert null != thirdLevelAbsoluteJPath
		assert null != thirdLevelRelativeJPath

		assert thirdLevelJProperty == thirdLevelAbsoluteJPath
		assert thirdLevelJProperty == thirdLevelRelativeJPath
	}

	@Test
	void 'In multi-level structure, fourth-level fields should be unmarshalled correctly'() {
		def returnObject = new TestMultiLevels()

		given(classInstantiator.newInstance(TestMultiLevels))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestMultiLevels, multiLevelJson)

		// ============ assert second level ===================
		def secondLevelJProperty = result.secondLevelJProperty

		assert null != secondLevelJProperty

		// ============ assert third level =======================
		def thirdLevelJProperty = secondLevelJProperty.thirdLevelJsonProperty

		assert null != thirdLevelJProperty

		def fourthLevelJPropertyIterable = thirdLevelJProperty.fourthLevelIterable
		def fourthLevelJPropertyArray = thirdLevelJProperty.fourthLevelArray

		assert null != fourthLevelJPropertyIterable
		assert null != fourthLevelJPropertyArray

		assert 2 == fourthLevelJPropertyIterable.asCollection().size()
		assert 2 == fourthLevelJPropertyArray.size()

		assert fourthLevelJPropertyArray as Collection  == fourthLevelJPropertyIterable as Collection

		// ======== assert fourth level from first element of array ==========================
		def fourthLevelArrayFirstElement = fourthLevelJPropertyArray[0]
		assert '4th field1 [0]' == fourthLevelArrayFirstElement.field1
		assert '4th field2' == fourthLevelArrayFirstElement.field2 //field is set using absolute path
		assert '4th field3 [0]' == fourthLevelArrayFirstElement.field3
		assert '4th field4 [0]' == fourthLevelArrayFirstElement.field4
	}

	@Test
	void 'In multi-level structure, annotated and non-annotated fields matching Json node should unmarshall properly'() {
		def returnObject = new TestMultiLevels()

		given(classInstantiator.newInstance(TestMultiLevels))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestMultiLevels, multiLevelJson)

		// ====== assert simple fields ================
		assert '1st field3' == result.field3
		assert '1st field2' == result.field2AbsolutePath
		assert '1st field2' == result.field2RelativePath
		assert 12345 == result.simpleField1
		assert 'last field' == result.simpleField2
	}

	/**
	 * TestNonAnnotatedField class has only one field without Json annotation.
	 * However, the field contains annotated fields using both JsonPath and JsonProperty annotations.
	 *
	 * Expected behavior: Field must be processed and all inner fields must be set.
	 * Practically, every non-annotated, non-primitive and non-Java-lang (or similar package) class must be processed like it is annotated with
	 * Json annotations.
	 */
	@Test
	void 'Non-annotated, non-primitive and non-Java-lang field must be processed and all inner fields must be set'() {
		def returnObject = new TestNonAnnotatedFields()

		given(classInstantiator.newInstance(TestNonAnnotatedFields)).willReturn(returnObject)

		def result = factory.unmarshall(TestNonAnnotatedFields, nonAnnotatedFields)

		assert 0 == result.getPrimitive()
		assert null == result.getInteger()
		assert null == result.getString()
		assert null == result.getStrArray()
		assert null == result.getIntArray()
		assert false == result.isPrimitiveBoolean()
		assert 12345 == result.getFirstInt()
		assert 678901 == result.getLastInt()
		assert 'First String' == result.getFirstString()
		assert 'Last String' == result.getLastString()
		assert true == result.getFirstBoolean()
		assert 3 == result.getStringArray().length
		assert 3 == result.getIntegerArray().length
		assert true == result.isLastBoolean()

		def nonAnnotatedField = result.nonAnnotatedField
		assert null != nonAnnotatedField
		assert 'absolute JSon path' == nonAnnotatedField.getAbsJsonPathField()
		assert 'relative JSon path' == nonAnnotatedField.getRelJsonPathField()
		assert 112233 == nonAnnotatedField.getJsonProp()
		assert 'First String' == nonAnnotatedField.getField1()
		assert null == nonAnnotatedField.getField2()
		assert 'non-annotated, matches Json node' == nonAnnotatedField.getField4()
		assert 'non-annotated, doesn\'t match Json node' == nonAnnotatedField.getField5()
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

	def multiLevelJson = '''

  {
      "field1": "1st field1",
      "field2": "1st field2",
      "field3": "1st field3",
      "field4": "1st field4",
      "field5": 12345,

      "second_level": {
        "field1": "2nd field1",
        "field2": "2nd field2",
        "field3": "2nd field3",
        "field4": "2nd field4",

        "third_level": {
            "field1": "3rd field1",
            "field2": "3rd field2",
            "field3": "3rd field3",
            "field4": "3rd field4",

            "fourth_level":{
                "field1": "4th field1",
                "field2": "4th field2",
                "field3": "4th field3",
                "field4": "4th field4"
            },

            "fourth_level_array":[
              {
                "field1": "4th field1 [0]",
                "field2": "4th field2 [0]",
                "field3": "4th field3 [0]",
                "field4": "4th field4 [0]"
              },
              {
                "field1": "4th field1 [1]",
                "field2": "4th field2 [1]",
                "field3": "4th field3 [1]",
                "field4": "4th field4 [1]"
              }
            ]
         }
      },
      "second_level_array": [
        {
          "field1": "2nd field1[0]",
          "field2": "2nd field2[0]",
          "field3": "2nd field3[0]",
          "field4": "2nd field4[0]",

          "third_level": {
              "field1": "3rd field1[0]",
              "field2": "3rd field2[0]",
              "field3": "3rd field3[0]",
              "field4": "3rd field4[0]",

              "fourth_level":{
                  "field1": "4th field1[0]",
                  "field2": "4th field2[0]",
                  "field3": "4th field3[0]",
                  "field4": "4th field4[0]"
              }
           }
        },
        {
          "field1": "2nd field1[1]",
          "field2": "2nd field2[1]",
          "field3": "2nd field3[1]",
          "field4": "2nd field4[1]",

          "third_level": {
              "field1": "3rd field1[1]",
              "field2": "3rd field2[1]",
              "field3": "3rd field3[1]",
              "field4": "3rd field4[1]",

              "fourth_level":{
                  "field1": "4th field1[1]",
                  "field2": "4th field2[1]",
                  "field3": "4th field3[1]",
                  "field4": "4th field4[1]"
              }
           }
        },
        {
          "field1": "2nd field1[2]",
          "field2": "2nd field2[2]",
          "field3": "2nd field3[2]",
          "field4": "2nd field4[2]",

          "third_level": {
              "field1": "3rd field1[2]",
              "field2": "3rd field2[2]",
              "field3": "3rd field3[2]",
              "field4": "3rd field4[2]",

              "fourth_level":{
                  "field1": "4th field1[2]",
                  "field2": "4th field2[2]",
                  "field3": "4th field3[2]",
                  "field4": "4th field4[2]"
              }
           }
        }
      ],
      "simple-field-1":12345,
      "simple-field-2":"last field"
  }
  '''

	def nonAnnotatedFields = '''
    {
     "firstInt":12345,
     "firstString":"First String",
     "firstBoolean":true,
     "nonAnnotatedField":{
        "field1": "absolute JSon path",
        "field2": "relative JSon path",
        "field3": 112233,
        "field4": "non-annotated, matches Json node",
        "field5": "non-annotated, doesn't match Json node"
      },
      "stringArray":[
        "string1","string2","string3"
      ],
      "integerArray":[
        1,2,3
      ],
      "lastInt":678901,
      "lastString":"Last String",
      "lastBoolean":true
    }

    '''
}