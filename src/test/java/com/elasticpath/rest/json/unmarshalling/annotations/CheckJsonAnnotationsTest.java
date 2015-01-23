package com.elasticpath.rest.json.unmarshalling.annotations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import com.jayway.jsonpath.InvalidPathException;

/**
 * Tests for {@link CheckJsonAnnotations}.
 */
public class CheckJsonAnnotationsTest {
	private static final String TEST_DATA_DIR = "src" + File.pathSeparator +
			"test" + File.pathSeparator +
			"java" + File.pathSeparator +
			"com" + File.pathSeparator +
			"elasticpath" + File.pathSeparator +
			"rest" + File.pathSeparator +
			"json" + File.pathSeparator +
			"unmarshalling" + File.pathSeparator +
			"data" + File.pathSeparator;
	private static final String MALFORMED_DATA_DIR = TEST_DATA_DIR + "malformed" + File.pathSeparator;
	private static final String VALID_DATA_DIR = TEST_DATA_DIR + "multilevel" + File.pathSeparator + "levels" + File.pathSeparator;

	private static final CheckJsonAnnotations TEST_CHECK_JSON_ANNOTATIONS = new CheckJsonAnnotations();

	@Test
	public void whenAllFilesInDirHaveValidJsonAnnotationsCheckJsonAnnotationsShouldPass() throws IOException {
		String[] directoryNames = {VALID_DATA_DIR};

		TEST_CHECK_JSON_ANNOTATIONS.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(directoryNames);
	}

	@Test
	public void whenDirectoryIsEmptyJsonAnnotationsShouldPass() throws IOException {
		// make a temp dir because git doesn't like storing empty dirs
		Path emptyDIR = Files.createTempDirectory("emptyDIR");
		String fileName = emptyDIR.toString();
		String[] fileNames = {fileName};
		TEST_CHECK_JSON_ANNOTATIONS.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(fileNames);
	}

	@Test(expected = InvalidPathException.class)
	public void whenFilesInDirHaveInvalidJsonAnnotationsCheckJsonAnnotationsShouldFail() throws IOException {
		String[] directoryNames = {MALFORMED_DATA_DIR};

		TEST_CHECK_JSON_ANNOTATIONS.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(directoryNames);
	}

	@Test(expected = InvalidPathException.class)
	public void whenJsonAnnotationIsEmptyCheckJsonAnnotationsShouldFail() throws IOException {
		String[] fileNames = {MALFORMED_DATA_DIR + "EmptyAnnotation.java"};

		TEST_CHECK_JSON_ANNOTATIONS.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(fileNames);
	}

	@Test(expected = InvalidPathException.class)
	public void whenJsonAnnotationIsBrokenJsonAnnotationsShouldFail() throws IOException {
		String[] fileNames = {MALFORMED_DATA_DIR + "BrokenAnnotation.java"};

		TEST_CHECK_JSON_ANNOTATIONS.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(fileNames);
	}

	@Test(expected = FileNotFoundException.class)
	public void whenFilePathIsInvalidJsonAnnotationsShouldFail() throws IOException {
		String[] fileNames = {MALFORMED_DATA_DIR + "noSuchFile"};

		TEST_CHECK_JSON_ANNOTATIONS.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(fileNames);
	}

}