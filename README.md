java-client-sdk
===============

(a spike of) a Java client sdk, utilizing JAX-RS and Jackson

Primary api is the [ClientSdk](src/main/java/com/elasticpath/rest/sdk/ClientSdk.java) class.

There is a frontend Java console app in the [Main](src/main/java/com/elasticpath/rest/sdk/Main.java)  class which can be run from an ide.

The classes under /model are used for Jackson deserialization examples. They are basic, automatic, and done quickly/hackily. For instance, using public fields. Can just use getters/setters in the future.

#Zoom
The [TotalZoom](src/main/java/com/elasticpath/rest/sdk/totals/TotalZoom.java) class shows an example of flattening a zoom object using [JsonPath](http://code.google.com/p/json-path/) on the string returned by JAX-RS, and some annotation processing.
* Each @[Zoom](src/main/java/com/elasticpath/rest/sdk/annotations/Zoom.java) contains a list, where each element represents a different step of a single zoom
* Multiple @[Zoom](src/main/java/com/elasticpath/rest/sdk/annotations/Zoom.java) can be present, representing multiple zooms executed in one query
* Each field can be flattened from the zoom result by using @[JPath](src/main/java/com/elasticpath/rest/sdk/annotations/JPath.java). This uses JsonPath [syntax](http://goessner.net/articles/JsonPath/).
