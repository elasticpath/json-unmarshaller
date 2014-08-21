package com.elasticpath.rest.sdk;

import static com.google.common.collect.Iterables.transform;
import static groovy.json.JsonOutput.prettyPrint;
import static java.lang.System.out;

import com.elasticpath.rest.sdk.model.Linkable;

public class Debug {

	public static void trace(Linkable linkable) {

		out.println(transform(linkable.links, link -> link.rel));
	}

	static void prettyTrace(String zoom) {
		out.println(prettyPrint(zoom));
	}
}
