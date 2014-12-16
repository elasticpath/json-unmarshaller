package com.elasticpath.rest.json.unmarshalling.impl

import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner)
class JsonPathUtilTest {

	@Test
	void 'Json path without @,$ and dot should be resolved as absolute if parent path is empty'() {

		def testedJsonPath = 'jsonPath'
		def result = JsonPathUtil.buildCorrectJsonPath(testedJsonPath, "")

		assert '$.jsonPath' == result
	}

	@Test
	void 'Json path without @,$ and dot should be resolved as relative if parent path is not empty'() {

		def testedJsonPath = 'jsonPath'
		def parentJsonPath = '$.parentJsonPath'
		def result = JsonPathUtil.buildCorrectJsonPath(testedJsonPath, parentJsonPath)

		assert '$.parentJsonPath.jsonPath' == result
	}

	@Test
	void 'Json path starting with @ and without dot should be resolved as absolute if parent path is empty'() {

		def testedJsonPath = '@jsonPath'
		def result = JsonPathUtil.buildCorrectJsonPath(testedJsonPath, "")

		assert '$.jsonPath' == result
	}

	@Test
	void 'Json path starting with @ and without dot should be resolved as relative if parent path is not empty'() {

		def testedJsonPath = '@jsonPath'
		def parentJsonPath = '$.parentJsonPath'
		def result = JsonPathUtil.buildCorrectJsonPath(testedJsonPath, parentJsonPath)

		assert '$.parentJsonPath.jsonPath' == result
	}

	@Test
	void 'Json path starting with $ and without dot should be resolved as absolute if parent path is empty'() {

		def testedJsonPath = '$jsonPath'
		def result = JsonPathUtil.buildCorrectJsonPath(testedJsonPath, "")

		assert '$.jsonPath' == result
	}

	@Test
	void 'Json path starting with $ and without dot should be resolved as absolute if parent path is not empty'() {

		def testedJsonPath = '$jsonPath'
		def parentJsonPath = '$.parentJsonPath'
		def result = JsonPathUtil.buildCorrectJsonPath(testedJsonPath, parentJsonPath)

		assert '$.jsonPath' == result
	}

	@Test
	void 'Json path starting with dot should be resolved as absolute if parent path is empty'() {

		def testedJsonPath = '.jsonPath'
		def result = JsonPathUtil.buildCorrectJsonPath(testedJsonPath, "")

		assert '$.jsonPath' == result
	}

	@Test
	void 'Json path starting dot should be resolved as relative if parent path is not empty'() {

		def testedJsonPath = '.jsonPath'
		def parentJsonPath = '$.parentJsonPath'
		def result = JsonPathUtil.buildCorrectJsonPath(testedJsonPath, parentJsonPath)

		assert '$.parentJsonPath.jsonPath' == result
	}
}
