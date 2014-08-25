package com.elasticpath.rest.sdk;

import static com.google.common.collect.Iterables.find;
import static javax.ws.rs.core.UriBuilder.fromUri;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.UriBuilder;

import com.elasticpath.rest.sdk.debug.Logger;
import com.elasticpath.rest.sdk.model.Linkable;
import com.elasticpath.rest.sdk.oauth.OAuth2AuthorizationService;
import com.elasticpath.rest.sdk.totals.TotalZoom;

public class Main {

	private Logger logger = new Logger();

	private OAuth2AuthorizationService oAuth2AuthorizationService = new OAuth2AuthorizationService();
	private CortexUrlBuilder cortexUrlBuilder = new CortexUrlBuilder();
	private CortexClient cortexClient = new CortexClient();

	private void run() {

		String scope = "mobee";
		UriBuilder serverUrl = serverUrl();
		oAuth2AuthorizationService.auth(serverUrl.clone()
				.path("oauth2")
				.path("tokens"),
				new Form()
						.param("grant_type", "password")
						.param("username", "ben.boxer@elasticpath.com")
						.param("password", "password")
						.param("role", "REGISTERED")
						.param("scope", scope)
		);

		String rootResourceUrl = serverUrl.path("root")
				.path(scope)
				.toString();

		String rootUrl = cortexUrlBuilder.get(rootResourceUrl, Linkable.class);
		Linkable root = getLinkable(rootUrl, Linkable.class);
		logger.trace("root rels", root);

		String cartUrl = cortexUrlBuilder.get(find(root.links, l -> "defaultcart".equals(l.rel)).href, Linkable.class);
		Linkable cart = getLinkable(cartUrl, Linkable.class);
		logger.trace("cart rels", cart);

		String lineItemsUrl = cortexUrlBuilder.get(find(cart.links, l -> "lineitems".equals(l.rel)).href, Linkable.class);
		Linkable lineItems = getLinkable(lineItemsUrl, Linkable.class);
		logger.trace("lineItem rels", lineItems);

		String totalZoomUrl = cortexUrlBuilder.get(root.self.href, TotalZoom.class);
		TotalZoom totalZoom = getLinkable(totalZoomUrl, TotalZoom.class);
		logger.trace("flattened total zoom output", totalZoom);
	}

	private <T> T getLinkable(String targetUrl,
							  Class<T> entityType) {
		return cortexClient.newCortexClient()
				.target(targetUrl)
				.request()
				.get()
				.readEntity(entityType);
	}

	public static UriBuilder serverUrl() {
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
