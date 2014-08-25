java-client-sdk
===============

(a spike of) the new Java client sdk, utilizing JAX-RS and Jackson

Primary api is the ClientSdk class.

There is a frontend Java console app in the Main class which can be run from an ide.

The classes under /model are used for Jackson deserialization examples. They are basic, automatic, and done quickly/hackily. For instance, using
public fields. Can just use getters/setters in the future.

#Zoom
Under /totals, there is a TotalZoom class, showing an example of flattening a zoom object using JsonPath on the string returned by JAX-RS, and some
annotation processing.
* Each @Zoom annotation contains a list, where each element represents a different step of a single zoom
* Multiple @Zoom annotations can be present, representing multiple zooms executed in one query
