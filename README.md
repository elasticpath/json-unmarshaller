json-unmarshalling
===============

##Un-Marshalling

@JsonPath is used on top level objects. Specifically on the raw response data that has not had any unmarshalling. JsonPath is similar in concept to xpath but for json. Jsonformatter provides some helpful tools:
* Json Validator
* JsonPath Tester

```
public class Cart {
@JsonPath("$._lineitems[0]._element")
private List<LineItem> lineItems;
@JsonPath("$.total-quantity")
private int totalQuantity;
}
```

The above example extracts two fragments of the response the 'total-quantity' property which is part of the top level data as well as all of the lineitems element objects. This fragment is unmarshalled by the client and for each fragment of the array a LineItem is constructed using the provided data.

@JsonProperty is used on fragments or parts of the response to extract specific properties, it only works on the current object and you cannot call into nested properties like you can with the @JsonPath

```
public class Address {
@JsonProperty("street-address")
private String streetAddress;
@JsonProperty("country-name")
private String countryName;
@JsonProperty("postal-code")
private String postalCode;
}
```

## Building

Checkout
Uses java 7

run `mvn clean install` to build

##Unit Tests
Test json data can be found in `src/test/resources`
Test pojo classes are in `com.elasticpath.rest.json.unmarshalling.data`


