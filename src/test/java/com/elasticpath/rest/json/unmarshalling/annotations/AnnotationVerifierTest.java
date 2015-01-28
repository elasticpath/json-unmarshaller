package com.elasticpath.rest.json.unmarshalling.annotations;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import com.jayway.jsonpath.InvalidPathException;

/**
 * Tests for {@link AnnotationVerifier}.
 */
public class AnnotationVerifierTest {

	private static final String BASE_DATA_DIR = "src/test/java/com/elasticpath/rest/json/unmarshalling/data/";
	private static final String MALFORMED_DATA_DIR = BASE_DATA_DIR + "malformed/";
	private static final String VALID_DATA_DIR = BASE_DATA_DIR + "multilevel/levels/";

	private final AnnotationVerifier annotationVerifier = new AnnotationVerifier();

	@Test
	public void whenAllFilesInDirHaveValidJsonAnnotationsCheckJsonAnnotationsShouldPass() throws IOException {
		String[] directoryNames = {VALID_DATA_DIR};

		annotationVerifier.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(directoryNames);
	}

	@Test
	public void whenDirectoryIsEmptyJsonAnnotationsShouldPass() throws IOException {
		// make a temp dir because git doesn't like storing empty dirs
		Path emptyDIR = Files.createTempDirectory("emptyDIR");
		String fileName = emptyDIR.toString();
		String[] fileNames = {fileName};
		annotationVerifier.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(fileNames);
	}

	@Test(expected = InvalidPathException.class)
	public void whenFilesInDirHaveInvalidJsonAnnotationsCheckJsonAnnotationsShouldFail() throws IOException {
		String[] directoryNames = {MALFORMED_DATA_DIR};

		annotationVerifier.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(directoryNames);
	}

	@Test(expected = InvalidPathException.class)
	public void whenJsonAnnotationIsEmptyCheckJsonAnnotationsShouldFail() throws IOException {
		String[] fileNames = {MALFORMED_DATA_DIR + "EmptyAnnotation.java"};

		annotationVerifier.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(fileNames);
	}

	@Test(expected = InvalidPathException.class)
	public void whenJsonAnnotationIsBrokenJsonAnnotationsShouldFail() throws IOException {
		String[] fileNames = {MALFORMED_DATA_DIR + "BrokenAnnotation.java"};

		annotationVerifier.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(fileNames);
	}

	@Test(expected = FileNotFoundException.class)
	public void whenFilePathIsInvalidJsonAnnotationsShouldFail() throws IOException {
		String[] fileNames = {MALFORMED_DATA_DIR + "noSuchFile"};

		annotationVerifier.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(fileNames);
	}

}