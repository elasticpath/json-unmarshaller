package com.elasticpath.rest.json.unmarshalling.impl

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES

import org.junit.BeforeClass

import com.elasticpath.rest.json.unmarshalling.data.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.BDDMockito.given
import static org.mockito.Mockito.never
import static org.mockito.Mockito.verify

/**
 * Tests for {@link DefaultJsonUnmarshaller}
 */
@RunWith(MockitoJUnitRunner)
class DefaultJsonUnmarshallerTest {
	public static final String TEST_DATA_FILE_PATH = 'src' + File.separator + 'test' + File.separator + 'resources' + File.separator

	@Mock
	ClassInstantiator classInstantiator

	@Spy
	ReflectionUtil reflectionUtil;

	@Spy
	JsonPathUtil jsonPathUtil

	@Spy
	ObjectMapper objectMapper = new ObjectMapper().disable(FAIL_ON_UNKNOWN_PROPERTIES)

	@InjectMocks
	DefaultJsonUnmarshaller factory

	static String multiLevelJson
	static String nonAnnotatedFields
	static String ignoredClasses
	static String jsonWithMap
	static String mixedContent

	@BeforeClass
	public static void readData() {
		multiLevelJson = new File(TEST_DATA_FILE_PATH + 'multiLevel.json').text
		nonAnnotatedFields = new File(TEST_DATA_FILE_PATH + 'nonAnnotatedFields.json').text
		ignoredClasses = new File(TEST_DATA_FILE_PATH + 'ignoredClasses.json').text
		jsonWithMap = new File(TEST_DATA_FILE_PATH + 'jsonWithMap.json').text
		mixedContent = new File(TEST_DATA_FILE_PATH + 'mixedContent.json').text
	}

	@Test
	void 'Given object with complex structure (multiple levels) should unmarshall fields without any Json annotation to type default values'() {
		def returnObject = new TestMultiLevels()

		given(classInstantiator.newInstance(TestMultiLevels))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestMultiLevels, multiLevelJson)

		assert 0 == result.int1;
		assert 0 == result.char5
		assert null == result.integer2
		assert null == result.string3
		assert null == result.byteArray4
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

		assert secondLevelArrayJProperty as Collection == secondLevelIterableJProperty as Collection
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

		assert secondLevelArrayJPath as Collection == secondLevelIterableJPath as Collection

		//regardless of annotation, result must be the same
		assert secondLevelIterableJProperty as Collection == secondLevelIterableJPath as Collection
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

		assert fourthLevelJPropertyArray as Collection == fourthLevelJPropertyIterable as Collection

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

		assert 0 == result.primitive
		assert null == result.integer
		assert null == result.string
		assert null == result.strArray
		assert null == result.intArray
		assert false == result.primitiveBoolean
		assert 12345 == result.firstInt
		assert 678901 == result.lastInt
		assert 'First String' == result.firstString
		assert 'Last String' == result.lastString
		assert true == result.firstBoolean
		assert 3 == result.stringArray.length
		assert 3 == result.integerArray.length
		assert true == result.lastBoolean

		def nonAnnotatedField = result.nonAnnotatedField
		assert null != nonAnnotatedField
		assert 'absolute JSon path' == nonAnnotatedField.absJsonPathField
		assert 'relative JSon path' == nonAnnotatedField.relJsonPathField
		assert 112233 == nonAnnotatedField.jsonProp
		assert 'First String' == nonAnnotatedField.field1
		assert null == nonAnnotatedField.field2
		assert 'non-annotated, matches Json node' == nonAnnotatedField.field4
		assert 'will never be set' == nonAnnotatedField.anythingElse
	}

	@Test
	void 'In multi-level structure, field annotated with JsonProperty on second and JsonPath on third level must be unmarshalled correctly'() {
		def returnObject = new TestMultiLevelsWithJsonPropertyOnly()

		given(classInstantiator.newInstance(TestMultiLevelsWithJsonPropertyOnly))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestMultiLevelsWithJsonPropertyOnly, multiLevelJson)

		def secondLevelJProperty = result.secondLevelJProperty
		assert null != secondLevelJProperty
		// ============ assert third level =======================
		def thirdLevelJProperty = secondLevelJProperty.thirdLevelWithJsonPath

		assert null != thirdLevelJProperty

		assert '3rd field1[1]' == thirdLevelJProperty.absoluteJsonPath
	}

	@Test
	void 'In multi-level structure, non-annotated field on second and annotated with JsonPath on third level must be unmarshalled correctly'() {
		def returnObject = new TestMultiLevelsWithNonAnnotatedField()

		given(classInstantiator.newInstance(TestMultiLevelsWithNonAnnotatedField))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestMultiLevelsWithNonAnnotatedField, multiLevelJson)

		def secondLevelNonAnnotated = result.secondLevelNonAnnotated
		assert null != secondLevelNonAnnotated
		// ============ assert third level =======================
		def thirdLevelJProperty = secondLevelNonAnnotated.third_level

		assert null != thirdLevelJProperty

		assert '3rd field1[1]' == thirdLevelJProperty.absoluteJsonPath
	}

	@Test
	void 'Classes from ignored packages must not trigger recursive calls'() {

		def returnObject = new TestUnmarshallingClassesFromIgnoredPackages()

		given(classInstantiator.newInstance(TestUnmarshallingClassesFromIgnoredPackages))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestUnmarshallingClassesFromIgnoredPackages, ignoredClasses)

		assert result.price == 789.45
		assert result.items != null
		assert result.items.size() == 3
		assert result.aDate.equals(new Date(1419028953000))

		verify(reflectionUtil).canUnmarshallClass(String.class)

		/*
		 verifies that ReflectionUtil.isFieldArrayOrList method
		 works correctly; here, verification is against
		 long serialVersionID field in List class, which shouldn't
		 be processed ever.
		 */
		verify(reflectionUtil, never()).canUnmarshallClass(long.class)
	}

	@Test//if map/table is unmarshalled, an exception would be thrown
	void 'Maps and tables shouldn\'t be further unmarshalled'(){
		def returnObject = new TestMaps()

		given(classInstantiator.newInstance(TestMaps)).willReturn(returnObject)

		def result = factory.unmarshall(TestMaps.class, jsonWithMap)
		assert result.countryMap != null
		assert result.countryMap.size() == 2
		assert result.mapOfCountries != null
		assert result.mapOfCountries.size() == 2
	}

	@Test
	void 'Should handle UTF8 characters correctly'() {

		def returnObject = new TestUTF8()

		given(classInstantiator.newInstance(TestUTF8))
				.willReturn(returnObject)

		def result = factory.unmarshall(TestUTF8, mixedContent)

		assert result.utf8Greek == 'Γρεεκ'
		assert result.utf8Cyrillic == 'Српски'
		assert result.utf8Chinese == '中國'
	}
}