package com.elasticpath.rest.sdk;

import static com.elasticpath.rest.sdk.Debug.trace;
import static java.util.Arrays.asList;
import static javax.ws.rs.core.UriBuilder.fromUri;

import javax.ws.rs.core.UriBuilder;

import com.elasticpath.rest.sdk.model.AuthToken;
import com.elasticpath.rest.sdk.model.Linkable;

public class Main {

	private ClientSdk clientSdk = new ClientSdk();

	private void run() {

		String scope = "mobee";
		UriBuilder serverPath = cortexUri();
		AuthToken authToken = clientSdk.auth(scope, serverPath.clone()
				.path("oauth2")
				.path("tokens"));

		UriBuilder href = serverPath.path("root")
				.path(scope);

		Linkable root = clientSdk.getLinkable(href, authToken);
		trace(root);

		Linkable cart = clientSdk.getLinkable(root.links
				.iterator()
				.next(), authToken);
		trace(cart);

		Linkable lineItems = clientSdk.getLinkable(cart.links
				.iterator()
				.next(), authToken);
		trace(lineItems);

		clientSdk.zoom(root, asList("searches", "keywordsearchform"), authToken);
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
