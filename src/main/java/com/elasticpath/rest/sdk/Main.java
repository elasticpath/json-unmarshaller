package com.elasticpath.rest.sdk;

import static com.elasticpath.rest.sdk.debug.Debug.trace;
import static com.google.common.collect.Iterables.find;
import static javax.ws.rs.core.UriBuilder.fromUri;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.UriBuilder;

import com.elasticpath.rest.sdk.model.AuthToken;
import com.elasticpath.rest.sdk.model.Linkable;
import com.elasticpath.rest.sdk.totals.TotalZoom;

public class Main {

	private ClientSdk clientSdk = new ClientSdk();

	private void run() {

		String scope = "mobee";
		UriBuilder serverPath = cortexUri();
		AuthToken authToken = clientSdk.auth(serverPath.clone()
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

		Linkable root = clientSdk.get(href, authToken, Linkable.class);
		trace(root);

		Linkable cart = clientSdk.get(find(root.links, l -> "defaultcart".equals(l.rel)).href, authToken, Linkable.class);
		trace(cart);

		Linkable lineItems = clientSdk.get(find(cart.links, l -> "lineitems".equals(l.rel)).href, authToken, Linkable.class);
		trace(lineItems);

		TotalZoom totalZoom = clientSdk.get(root.self.href, authToken, TotalZoom.class);
		System.out.println(totalZoom);
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
