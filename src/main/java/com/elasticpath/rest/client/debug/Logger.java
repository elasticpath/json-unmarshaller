package com.elasticpath.rest.client.debug;

import static com.google.common.collect.Iterables.transform;
import static java.lang.String.format;
import static java.lang.System.out;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.common.base.Function;

import com.elasticpath.rest.client.model.Link;
import com.elasticpath.rest.client.model.Linkable;

@Named
@Singleton
public class Logger {

	public void trace(String name,
					  Linkable linkable) {

		out.println(format("%s: %s", name, transform(linkable.getLinks(), new Function<Link, Object>() {
			@Override
			public Object apply(final Link link) {
				return link.getRel();
			}
		})));
	}

	public void trace(String name,
					  Object object) {

		out.println(format("%s: %s", name, object));
	}

	public void trace(String json) {
		out.println(json);
	}
}
