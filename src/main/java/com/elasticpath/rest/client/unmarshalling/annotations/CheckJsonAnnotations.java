package com.elasticpath.rest.client.unmarshalling.annotations;


import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.internal.PathCompiler;

/**
 * Tests for {@link temp}
 */
public class CheckJsonAnnotations {

	private static final String TEXT = ".*?";
	private static final String REFERABLE_TEXT = "(.*?)";
	private static final String QUOTE = "\"";
	private static final String CLOSING_BRACKET = "\\)";
	private static final String MAKE_PRECEDING_TOKEN_OPTIONAL = "?";

	//private static final String ANNOTATION_PATH_PATTERN = "@Json.*?\"(.*?)\"\\)?";
	private static final String ANNOTATION_PATH_PATTERN =
			"@Json" + TEXT + QUOTE + REFERABLE_TEXT + QUOTE + CLOSING_BRACKET + MAKE_PRECEDING_TOKEN_OPTIONAL;

	public static void main(final String[] args) throws Exception {
		System.out.println("<><><><><><><><><>");
		CheckJsonAnnotations annotationChecker = new CheckJsonAnnotations();
		annotationChecker.checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(args);

		for (String s:args) {
			System.out.println(s);
		}

	}

	private void checkJsonAnnotationsInFile(File file) throws IOException, InvalidPathException {
		Pattern pattern = Pattern.compile(ANNOTATION_PATH_PATTERN);
		BufferedReader br = new BufferedReader(new FileReader(file));
		for(String line; (line = br.readLine()) != null; ) {
			line = line.replaceAll("\t","");
			line = line.trim();
			if (line.startsWith("@JsonProperty")||line.startsWith("@JsonPath")) {
				Matcher m = pattern.matcher(line);
				if (m.find()){
					String path = m.group(1);
					if (path.equals("")) {
						throw new InvalidPathException("Json Annotation cannot be empty");
					}
					PathCompiler.compile(path);
				}
			}
		}
	}

	private void checkJsonAnnotationsRecursivelyFromFileOrDirectory(File fileOrDirectory) throws IOException {
		if (fileOrDirectory.isDirectory()) {
			File[] files = fileOrDirectory.listFiles();
			for (File file : files) {
					checkJsonAnnotationsRecursivelyFromFileOrDirectory(file);
			}
		} else {
			checkJsonAnnotationsInFile(fileOrDirectory);
		}
	}

	public void checkJsonAnnotationsRecursivelyFromFileOrDirectoryNames(String[] fileOrDirectoryNames) throws IOException {
	File directory;
		for (String fileDirectoryName : fileOrDirectoryNames) {
			directory = new File(fileDirectoryName);
			checkJsonAnnotationsRecursivelyFromFileOrDirectory(directory);
		}
	}

}
