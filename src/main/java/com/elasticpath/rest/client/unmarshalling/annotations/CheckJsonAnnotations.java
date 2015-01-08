package com.elasticpath.rest.client.unmarshalling.annotations;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.internal.PathCompiler;

public class CheckJsonAnnotations {

	private static final String TEXT = ".*?";
	private static final String REFERABLE_TEXT = "(.*?)";
	private static final String QUOTE = "\"";
	private static final String CLOSING_BRACKET = "\\)";
	private static final String MAKE_PRECEDING_TOKEN_OPTIONAL = "?";

	private static final String ANNOTATION_PATH_PATTERN =
			"@Json" + TEXT + QUOTE + REFERABLE_TEXT + QUOTE + CLOSING_BRACKET + MAKE_PRECEDING_TOKEN_OPTIONAL;

	public static void main(final String[] args) throws Exception {
		CheckJsonAnnotations annotationChecker = new CheckJsonAnnotations();
		annotationChecker.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(args);
	}

	//visible for testing
	protected void checkJsonAnnotationsInFile(File file) throws IOException, InvalidPathException {
		Pattern pattern = Pattern.compile(ANNOTATION_PATH_PATTERN);
		try(InputStream fileIs = new FileInputStream(file)) {
			BufferedReader br = new BufferedReader(new InputStreamReader(fileIs));
			for (String line; (line = br.readLine()) != null; ) {
				checkJsonAnnotationInLine(pattern, line);
			}
		}
	}

	private void checkJsonAnnotationInLine(Pattern pattern, String line) {
		line = line.replaceAll("\t", "");
		line = line.trim();
		if (!line.startsWith("@JsonProperty") && !line.startsWith("@JsonPath")) {
			return;
		}
		Matcher m = pattern.matcher(line);
		if (!m.find()) {
			return;
		}
		String path = m.group(1);
		if (path.equals("")) {
			throw new InvalidPathException("Json Annotation cannot be empty");
		}
		PathCompiler.compile(path);
	}

	//visible for testing
	protected void checkJsonAnnotationsRecursivelyFromFileOrDirectory(File fileOrDirectory) throws IOException {
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
	public void checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(String[] fileOrDirectoryNames) throws IOException {
		File fileOrDirectory;
		for (String fileDirectoryName : fileOrDirectoryNames) {
			fileOrDirectory = new File(fileDirectoryName);
			checkJsonAnnotationsRecursivelyFromFileOrDirectory(fileOrDirectory);
		}
	}

}
