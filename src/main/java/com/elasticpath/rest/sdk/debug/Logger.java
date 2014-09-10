package com.elasticpath.rest.sdk.debug;

import static com.google.common.collect.Iterables.transform;
import static groovy.json.JsonOutput.prettyPrint;
import static java.lang.String.format;
import static java.lang.System.out;

import com.google.common.base.Function;

import com.elasticpath.rest.sdk.model.Link;
import com.elasticpath.rest.sdk.model.Linkable;

public class Logger {

	public void trace(String name,
					  Linkable linkable) {

		out.println(format("%s: %s", name, transform(linkable.links, new Function<Link, Object>() {
			@Override
			public Object apply(final Link link) {
				return link.rel;
			}
		})));
	}

	public void trace(String name,
					  Object object) {

		out.println(format("%s: %s", name, object));
	}

	public void prettyTrace(String json) {
		out.println(prettyPrint(json));
	}
}
