package com.elasticpath.rest.sdk.debug;

import static com.google.common.collect.Iterables.transform;
import static groovy.json.JsonOutput.prettyPrint;
import static java.lang.System.out;

import com.elasticpath.rest.sdk.model.Linkable;

public class Logger {

	public void trace(Linkable linkable) {

		out.println(transform(linkable.links, link -> link.rel));
	}

	public void prettyTrace(String zoom) {
		out.println(prettyPrint(zoom));
	}
}
