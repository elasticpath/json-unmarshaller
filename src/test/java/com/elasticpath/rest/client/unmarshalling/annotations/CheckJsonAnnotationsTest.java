package com.elasticpath.rest.client.unmarshalling.annotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import com.jayway.jsonpath.InvalidPathException;

/**
 * Tests for {@link CheckJsonAnnotations}.
 */
public class CheckJsonAnnotationsTest {

	private String malformedDataDir = "src/test/java/com/elasticpath/rest/client/unmarshalling/annotations/malformed/";
	private String validDataDir = "src/test/java/com/elasticpath/rest/json/unmarshalling/data/";

	private CheckJsonAnnotations testCheckJsonAnnotations = new CheckJsonAnnotations();

	@Test
	public void whenAllFilesInDirHaveValidJsonAnnotationsCheckJsonAnnotationsShouldPass() throws IOException {
		Boolean checkPassed = true;
		String[] directoryNames = {validDataDir};

		try {
			testCheckJsonAnnotations.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(directoryNames);
		} catch (InvalidPathException e) {
			checkPassed = false;
		}

		assertTrue("CheckJsonAnnotations tests should pass when all annotations in dir are correct", checkPassed);
	}

	@Test
	public void whenFilesInDirHaveInvalidJsonAnnotationsCheckJsonAnnotationsShouldFail() throws IOException {
		Boolean checkPassed = true;
		String[] directoryNames = {malformedDataDir};

		try {
			testCheckJsonAnnotations.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(directoryNames);
		} catch (InvalidPathException e) {
			checkPassed = false;
		}

		assertFalse("CheckJsonAnnotations should throw exception when an annotation in dir is invalid", checkPassed);
	}

	@Test
	public void whenJsonAnnotationIsEmptyCheckJsonAnnotationsShouldFail() throws IOException {
		Boolean checkPassed = true;
		String[] fileNames = {malformedDataDir + "Empty.java"};

		try {
			testCheckJsonAnnotations.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(fileNames);
		} catch (InvalidPathException e) {
			checkPassed = false;
		}

		assertFalse("tests should throw exception when an annotation is empty", checkPassed);
	}

	@Test
	public void whenJsonAnnotationIsBrokenJsonAnnotationsShouldFail() throws IOException {
		Boolean checkPassed = true;
		String[] fileNames = {malformedDataDir + "BrokenNonAnnotatedField.java"};

		try {
			testCheckJsonAnnotations.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(fileNames);
		} catch (InvalidPathException e) {
			checkPassed = false;
		}

		assertFalse("tests should throw exception when an annotation is empty", checkPassed);
	}

	@Test
	public void whenDirectoryIsEmptyJsonAnnotationsShouldPass() throws IOException {
		Boolean checkPassed = true;
		// make a temp dir because git doesn't like storing empty dirs
		Path emptyDIR = Files.createTempDirectory("emptyDIR");
		String fileName = emptyDIR.toString();
		String[] fileNames = {fileName};
		try {
			testCheckJsonAnnotations.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(fileNames);
		} catch (InvalidPathException e) {
			checkPassed = false;
		}

		assertTrue("CheckJsonAnnotations tests should pass when dir is empty", checkPassed);
	}

}