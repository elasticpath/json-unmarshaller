package com.elasticpath.rest.sdk;

import static com.google.common.collect.Iterables.find;
import static javax.ws.rs.core.UriBuilder.fromUri;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.UriBuilder;

import com.elasticpath.rest.sdk.debug.Logger;
import com.elasticpath.rest.sdk.model.Linkable;
import com.elasticpath.rest.sdk.totals.TotalZoom;

public class Main {

	private ClientSdk clientSdk = new ClientSdk();
	private Logger logger = new Logger();

	private void run() {

		String scope = "mobee";
		UriBuilder serverPath = cortexUri();
		clientSdk.auth(serverPath.clone()
				.path("oauth2")
				.path("tokens"),
				new Form()
						.param("grant_type", "password")
						.param("username", "ben.boxer@elasticpath.com")
						.param("password", "password")
						.param("role", "REGISTERED")
						.param("scope", scope)
		);

		String href = serverPath.path("root")
				.path(scope)
				.toString();

		Linkable root = clientSdk.get(href, Linkable.class);
		logger.trace("root rels", root);

		Linkable cart = clientSdk.get(find(root.links, l -> "defaultcart".equals(l.rel)).href, Linkable.class);
		logger.trace("cart rels", cart);

		Linkable lineItems = clientSdk.get(find(cart.links, l -> "lineitems".equals(l.rel)).href, Linkable.class);
		logger.trace("lineItem rels", lineItems);

		TotalZoom totalZoom = clientSdk.get(root.self.href, TotalZoom.class);
		logger.trace("flattened total zoom output", totalZoom);
	}

	public static UriBuilder cortexUri() {
		return fromUri("")
				.scheme("http")
				.host("localhost")
				.port(9080)
				.path("cortex");
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
