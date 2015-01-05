package com.elasticpath.rest.json.unmarshalling.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.junit.Test;
import com.jayway.jsonpath.InvalidPathException;
import com.elasticpath.rest.client.unmarshalling.annotations.CheckJsonAnnotations;

/**
 * Created by pmatich on 29/12/14.
 */
public class CheckJsonAnnotationsTest {

	private String testDataDir = "src/test/java/com/elasticpath/rest/json/unmarshalling/data/";
	private CheckJsonAnnotations testCheckJsonAnnotations = new CheckJsonAnnotations();

	@Test
	public void whenAllFilesInDirHaveValidJsonAnnotationsCheckJsonAnnotationsShouldPass() throws IOException {
		//Arrange
		Boolean checkPassed = true;
		String[] directoryNames = {testDataDir + "/multi_level/"};
		//Action
		try {
			testCheckJsonAnnotations.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(directoryNames);
		} catch (InvalidPathException e) {
			checkPassed = false;
		}

		//Assert
		assertTrue("CheckJsonAnnotations tests should pass when all annotations in dir are correct", checkPassed);
	}

	@Test
	public void whenAFileInDirHasAnInvalidJsonAnnotationCheckJsonAnnotationsShouldFail() throws IOException {
		//Arrange
		Boolean checkPassed = true;
		String[] directoryNames = {testDataDir + "malformed/"};
		//Action
		try {
			testCheckJsonAnnotations.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(directoryNames);
		} catch (InvalidPathException e) {
			checkPassed = false;
		}
		//Assert
		assertFalse("CheckJsonAnnotations should throw exception when an annotation in dir is invalid", checkPassed);
	}

	@Test
	public void whenJsonAnnotationIsEmptyCheckJsonAnnotationsShouldFail() throws IOException {
		//Arrange
		Boolean checkPassed = true;
		String[] fileNames = {testDataDir + "malformed/Empty.java"};
		//Action
		try {
			testCheckJsonAnnotations.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(fileNames);
		} catch (InvalidPathException e) {
			checkPassed = false;
		}
		//Assert
		assertFalse("tests should throw exception when an annotation is empty", checkPassed);
	}

	@Test
	public void whenJsonAnnotationIsBrokenJsonAnnotationsShouldFail() throws IOException {
		//Arrange
		Boolean checkPassed = true;
		String[] fileNames = {testDataDir + "malformed/BrokenNonAnnotatedField.java"};
		//Action
		try {
			testCheckJsonAnnotations.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(fileNames);
		} catch (InvalidPathException e) {
			checkPassed = false;
		}
		//Assert
		assertFalse("tests should throw exception when an annotation is broken", checkPassed);
	}

}
