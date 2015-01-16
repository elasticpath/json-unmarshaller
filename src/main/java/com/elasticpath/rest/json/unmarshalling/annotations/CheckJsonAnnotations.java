package com.elasticpath.rest.json.unmarshalling.annotations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.internal.PathCompiler;

/**
 * Checks that Json annotations are compilable in the provided files, and recursively
 * through the provided directories.
 */
public class CheckJsonAnnotations {

	private static final String PATH_OR_PROPERTY = "(?:Path|Property)";
	private static final String REFERABLE_TEXT = "(.*?)";
	private static final String QUOTE = "\"";
	private static final String OPENING_BRACKET = "\\(";
	private static final String CLOSING_BRACKET = "\\)";
	private static final String MAKE_PRECEDING_TOKEN_OPTIONAL = "?";

	private static final String ANNOTATION_PATH_PATTERN =
			"@Json" + PATH_OR_PROPERTY + OPENING_BRACKET
					+ QUOTE + REFERABLE_TEXT + QUOTE
					+ CLOSING_BRACKET + MAKE_PRECEDING_TOKEN_OPTIONAL;

	/**
	 * Checks that all {@link JsonPath} and {@link JsonProperty} annotations are compilable
	 * by {@link PathCompiler} in the provided files, and recursively through the provided
	 * directories.
	 *
	 * @param args the files or directories to be checked
	 * @throws IOException if file is not found or annotation is invalid
	 */
	public static void main(final String[] args) throws IOException {
		CheckJsonAnnotations annotationChecker = new CheckJsonAnnotations();
		annotationChecker.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(args);
	}

	/**
	 * Main entry point
	 * @param fileOrDirectoryNames the files or directories to checked
	 * @throws IOException if file is not found or annotation is invalid
	 */
	void checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(final String[] fileOrDirectoryNames) throws IOException {
		File fileOrDirectory;
		for (String fileDirectoryName : fileOrDirectoryNames) {
			fileOrDirectory = new File(fileDirectoryName);
			checkJsonAnnotationsRecursivelyFromFileOrDirectory(fileOrDirectory);
		}
	}

	private void checkJsonAnnotationsInFile(final File file) throws IOException, InvalidPathException {
		Pattern pattern = Pattern.compile(ANNOTATION_PATH_PATTERN);
		try (InputStream fileInputStream = new FileInputStream(file)) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				checkJsonAnnotationInLine(pattern, line);
			}
		}
	}

	private void checkJsonAnnotationInLine(final Pattern pattern, final String line) {
		String formattedLine = line.replaceAll("\t", "").trim();
		Matcher annotationPatternMatcher = pattern.matcher(formattedLine);
		if (!annotationPatternMatcher.find()) {
			return;
		}
		String path = annotationPatternMatcher.group(1);
		String emptyString = "";
		if (path.equals(emptyString)) {
			throw new InvalidPathException("Json Annotation cannot be empty");
		}
		PathCompiler.compile(path);
	}

	private void checkJsonAnnotationsRecursivelyFromFileOrDirectory(final File fileOrDirectory) throws IOException {
		if (!fileOrDirectory.isDirectory()) {
			checkJsonAnnotationsInFile(fileOrDirectory);
			return;
		}
		File[] files = fileOrDirectory.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			checkJsonAnnotationsRecursivelyFromFileOrDirectory(file);
		}
	}
}
