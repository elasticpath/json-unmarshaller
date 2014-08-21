package com.elasticpath.rest.sdk;

import static com.elasticpath.rest.sdk.Debug.trace;
import static groovy.json.JsonOutput.prettyPrint;
import static java.lang.System.out;
import static java.util.Arrays.asList;
import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.client.Entity.form;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.UriBuilder.fromPath;
import static javax.ws.rs.core.UriBuilder.fromUri;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.google.common.base.Joiner;

import com.elasticpath.rest.sdk.model.Auth;
import com.elasticpath.rest.sdk.model.AuthToken;
import com.elasticpath.rest.sdk.model.Link;
import com.elasticpath.rest.sdk.model.Linkable;

public class ClientSdk {

	public static void main(String[] args) {

		String scope = "mobee";
		UriBuilder serverPath = cortexUri();
		AuthToken authToken = auth(scope, serverPath.clone()
				.path("oauth2")
				.path("tokens"));

		UriBuilder href = serverPath.path("root")
				.path(scope);

		Linkable root = getLinkable(href, authToken);
		trace(root);

		Linkable cart = getLinkable(root.links
				.iterator()
				.next(), authToken);
		trace(cart);

		Linkable lineItems = getLinkable(cart.links
				.iterator()
				.next(), authToken);
		trace(lineItems);

		zoom(root, asList("searches", "keywordsearchform"), authToken);
	}

	public static void zoom(Linkable root,
							Iterable<String> zooms,
							AuthToken authToken) {

		UriBuilder target = fromPath(root.self.href)
				.queryParam("zoom", Joiner.on(":")
						.join(zooms));

		String zoom = get(target, authToken)
				.readEntity(String.class);

		out.println(prettyPrint(zoom));
	}

	public static Linkable getLinkable(UriBuilder target,
									   AuthToken authToken) {
		return get(target, authToken)
				.readEntity(Linkable.class);
	}

	public static Linkable getLinkable(Link link,
									   AuthToken authToken) {

		UriBuilder target = fromPath(link.href);

		return getLinkable(target, authToken);
	}

	private static Response get(UriBuilder target,
								AuthToken authToken) {
		return newClient()
				.register(JacksonProvider.class)
				.target(target)
				.request(APPLICATION_JSON_TYPE)
				.header(authToken.getHeaderName(), authToken.getHeaderValue())
				.get();
	}

	public static AuthToken auth(String scope,
								 UriBuilder target) {
		Form auth = new Form()
				.param("grant_type", "password")
				.param("username", "ben.boxer@elasticpath.com")
				.param("password", "password")
				.param("role", "REGISTERED")
				.param("scope", scope);

		Response response = newClient()
				.register(JacksonProvider.class)
				.target(target)
				.request(APPLICATION_JSON_TYPE)
				.post(form(auth));

		String accessToken = response.readEntity(Auth.class)
				.getAccessToken();

		return new AuthToken(accessToken);
	}

	public static UriBuilder cortexUri() {
		return fromUri("")
				.scheme("http")
				.host("localhost")
				.port(9080)
				.path("cortex");
	}
}
