java-client-sdk
===============

EP Java Client sdk, utilizing JAX-RS and Jackson

The [Main](src/main/java/com/elasticpath/rest/sdk/Main.java) class is a Java console executable (runnable from an IDE) that represents a sample client of the JavaSDK.

Commit e66cadd22477cffc191e9a5c291ca601ac080d8a onwards represents a shift from using our own SDK interface (which was basically a facade on JAX-RS) to requiring our clients to call JAX-RS directly. This is supported by our custom filters/interceptors for OAuth and Zoom listed below. This may seem like a step backwards in lines of code, but means that we as EP do not have to mirror every change in the JAX-RS api in our facade. Instead, we are just extending the existing client framework provided by JAX-RS and enriching it with Cortex-specific functionality.

The classes under /model are used for Jackson deserialization examples. They are basic, automatic, and done quickly/hackily. For instance, using public fields. Can just use getters/setters in the future.

#Todo
* Error handling (e.g. what happens if a JPath zoom does not exist?)
* Tests
* Better Jackson/JPath integration
* Ensure the below docs are up to date

#Zoom
The [TotalZoom](src/main/java/com/elasticpath/rest/sdk/totals/TotalZoom.java) class shows an example of flattening a zoom object using [JsonPath](http://code.google.com/p/json-path/) on the string returned by JAX-RS, and some annotation processing.
* Each @[Zoom](src/main/java/com/elasticpath/rest/sdk/annotations/Zoom.java) contains a list, where each element represents a different step of a single zoom
* Multiple @[Zoom](src/main/java/com/elasticpath/rest/sdk/annotations/Zoom.java) can be present, representing multiple zooms executed in one query
* Each field can be flattened from the zoom result by using @[JPath](src/main/java/com/elasticpath/rest/sdk/annotations/JPath.java). This uses JsonPath [syntax](http://goessner.net/articles/JsonPath/)
* Zoom deserialization is handled automagically by a [ZoomReaderInterceptor](src/main/java/com/elasticpath/rest/sdk/zoom/ZoomReaderInterceptor.java), which plugs into JAX-RS. We can tell JAX-RS we are requesting a class that is annotated with @[Zoom](src/main/java/com/elasticpath/rest/sdk/annotations/Zoom.java), and it handles the rest

#Authentication
##OAuth2
* Post username/password to oauth service
* Receive an access token, that gets added to the header of every subsequent request
This may differ from other authentication implementations. This can be less manual and more configurable through JAX-RS filters/interceptors
* [OAuthReaderInterceptor](src/main/java/com/elasticpath/rest/sdk/oauth/OAuthReaderInterceptor.java) reads and stores the user's access token
* [OAuthTokenService](src/main/java/com/elasticpath/rest/sdk/oauth/OAuthTokenService.java) stores the token with a thread local. Presently in AEM, we use cookies, so this service would be replaced by the storage/retrieval of a cookie
* [OAuthRequestFilter](src/main/java/com/elasticpath/rest/sdk/oauth/OAuthRequestFilter.java) uses the same service to read the token and add it to the header of every request
* With an alternate authentication mechanism, we can provide different features that can be registered with JAX-RS, making the client SDK solution agnostic
* The interface is also simplified by not passing tokens around manually

#JAX-RS
Some notes.

While using the fluent pattern, the actual remote call is done in two (broadly speaking) stages.
* First the read to the remote url, which can be preceded by ClientRequest/ResponseFilters
  * This has only url data and POST data
  * The ability to modify or read the target oject type is not available here
* Second the deserialization given the raw streams which can be surrounded by Reader/WriterInterceptors

Useful links
* https://jersey.java.net/documentation/latest/user-guide.html#client
* https://jersey.java.net/documentation/latest/filters-and-interceptors.html