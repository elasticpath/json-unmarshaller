package com.elasticpath.rest.json.unmarshalling.impl

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Mockito.when

/**
 * Tests for {@link JsonPathUtil}
 */
@RunWith(MockitoJUnitRunner)
class JsonPathUtilTest {

	@Mock
	CandidateField candidateField

	@InjectMocks
	JsonPathUtil factory

	@Test
	void 'Json path without @,$ and dot should be resolved as absolute if parent path is empty'() {

		def testedJsonPath = 'jsonPath'
		def result = factory.buildCorrectJsonPath(testedJsonPath, "")

		assert '$.jsonPath' == result
	}

	@Test
	void 'Json path without @,$ and dot should be resolved as relative if parent path is not empty'() {

		def testedJsonPath = 'jsonPath'
		def parentJsonPath = '$.parentJsonPath'
		def result = factory.buildCorrectJsonPath(testedJsonPath, parentJsonPath)

		assert '$.parentJsonPath.jsonPath' == result
	}

	@Test
	void 'Json path starting with @ and without dot should be resolved as absolute if parent path is empty'() {

		def testedJsonPath = '@jsonPath'
		def result = factory.buildCorrectJsonPath(testedJsonPath, "")

		assert '$.jsonPath' == result
	}

	@Test
	void 'Json path starting with @ and without dot should be resolved as relative if parent path is not empty'() {

		def testedJsonPath = '@jsonPath'
		def parentJsonPath = '$.parentJsonPath'
		def result = factory.buildCorrectJsonPath(testedJsonPath, parentJsonPath)

		assert '$.parentJsonPath.jsonPath' == result
	}

	@Test
	void 'Json path starting with $ and without dot should be resolved as absolute if parent path is empty'() {

		def testedJsonPath = '$jsonPath'
		def result = factory.buildCorrectJsonPath(testedJsonPath, "")

		assert '$.jsonPath' == result
	}

	@Test
	void 'Json path starting with $ and without dot should be resolved as absolute if parent path is not empty'() {

		def testedJsonPath = '$jsonPath'
		def parentJsonPath = '$.parentJsonPath'
		def result = factory.buildCorrectJsonPath(testedJsonPath, parentJsonPath)

		assert '$.jsonPath' == result
	}

	@Test
	void 'Json path starting with dot should be resolved as absolute if parent path is empty'() {

		def testedJsonPath = '.jsonPath'
		def result = factory.buildCorrectJsonPath(testedJsonPath, "")

		assert '$.jsonPath' == result
	}

	@Test
	void 'Json path starting dot should be resolved as relative if parent path is not empty'() {

		def testedJsonPath = '.jsonPath'
		def parentJsonPath = '$.parentJsonPath'
		def result = factory.buildCorrectJsonPath(testedJsonPath, parentJsonPath)

		assert '$.parentJsonPath.jsonPath' == result
	}

	@Test
	void 'Should return valid json path when list of paths is provided' () {
		def result = factory.getJsonPath(Arrays.asList('$.path1', 'path2'))
		assert '$.path1.path2' == result
	}

	@Test
	void 'Should resolve to absolute path when annotation value contains absolute path and parent path exists' () {

		when(candidateField.getJsonPathFromField()).thenReturn('$.absolute.path')

		final Iterable<String> parentJsonPath = Arrays.asList('$.path1', 'path2')

		def result = factory.resolveRelativeJsonPaths(candidateField, parentJsonPath)
		assert '$.absolute.path' == result.toString().replaceAll('[\\[\\]]','')
	}

	@Test
	void 'Should resolve to relative path when processing JsonProperty annotation and parent path exists' () {

		when(candidateField.getJsonPathFromField()).thenReturn('jsonPropertyValue')

		final Iterable<String> parentJsonPath = Arrays.asList('$.path1', 'path2')

		def result = factory.resolveRelativeJsonPaths(candidateField, parentJsonPath)
		assert '$.path1.path2.jsonPropertyValue' == result.toString().replaceAll('[\\[\\]\\s]','').replaceAll(',','.')
	}

	@Test
	void 'Should resolve to absolute path when processing JsonProperty annotation and parent is empty' () {

		when(candidateField.getJsonPathFromField()).thenReturn('jsonPropertyValue')

		final Iterable<String> parentJsonPath = new ArrayList<>()

		def result = factory.resolveRelativeJsonPaths(candidateField, parentJsonPath)
		assert '$.jsonPropertyValue' == result.toString().replaceAll('[\\[\\]]','')
	}
}
