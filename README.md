[![Build Status](https://api.travis-ci.org/elasticpath/json-unmarshaller.png)](https://travis-ci.org/elasticpath/json-unmarshaller)

# Elastic Path's Json Unmarshaller

Elastic Path's Json Unmarshaller is an extension to the popular [Jackson library](https://github.com/FasterXML/jackson). It provides annotation 
based unmarshalling of deep JSON object graphs into shallow POJOs using JsonPath. JsonPath is a very similar concept to XPath but for JSON. 
This library makes use of [Jway's](https://github.com/jayway/JsonPath) Java implementation of JsonPath. 

This library introduces a field annotation, @JsonPath, which can bused to specify JsonPath unmarshalling instructions in Java classes. @JsonPath
 annotations can be used exclusively for every field in a class hierarchy, or in conjunction with Jackson's field-matching and @JsonProperty annotations. 

The below image shows how Jackson's field-matching and @JsonProperty annotations can be combined with @JsonPath annotations to unmarshal  
a single JSON object graph into two very different POJO arrangements. 
 
![alt text](https://cloud.githubusercontent.com/assets/868640/6225115/ae7be7b2-b63a-11e4-9d76-a23227bf82d3.png "Sample Unmarshalling")

Notice in the above image how the tree hierarchy can be re-arranged by using absolute JsonPath declarations `$.absolute.path.from.root` 
rather than relative `@.relative.path.from.parent`. At Elastic Path we find this flexibility invaluable when interacting with our Commerce 
REST API Cortex. Cortex is a Level 3 REST API which has support for following links server-side. Heavy use of server-side link following often 
results in large JSON trees being returned for a single REST request. Elastic Path's Json Unmarshaller can easily manipulate these trees into 
whatever POJO arrangements are required.

## Usage
Json-unmarshaller is available at the Central Maven Repository. Maven users add this to your POM.

```xml
<dependency>
	<groupId>com.elasticpath.json</groupId>
	<artifactId>json-unmarshaller</artifactId>
    <version>1.0.0</version>
</dependency>
```  
### Configuration
* Build desired Java Class Hierarchy using @JsonPath and @JsonProperty annotations
```
public class Cart {
	@JsonPath("$._lineitems[0]._element")
	private List<LineItem> lineItems;
	@JsonPath("$.total-quantity")
	private int totalQuantity;
	@JsonProperty("postal-code")
    private String postalCode;
}
```
* Instantiate the unmarshaller, either with the default constructor, or using a custom Jackson ObjectMapper.
```
JsonUnmarshaller jsonUnmarshaller = new JsonUnmarshaller();
```
* Unmarshal the Json into the desired class 
``` 
jsonUnmarshaller.unmarshal(Cart.class, jsonString) 
``` 

See the both the test directory of this project for sample use cases, and the [Jway docs](https://github.com/jayway/JsonPath/blob/master/README.md#operators) 
for more complicated JsonPath arrangements.

## Annotation Verification
This project provides a convenience class for testing the validity of all supplied field annotations in a directory. In a maven build, the
exec-maven-plugin can easily be configured to verify all defined annotations at compile time as follows:
```xml
<plugin>
	<groupId>org.codehaus.mojo</groupId>
	<artifactId>exec-maven-plugin</artifactId>
	<version>1.3.2</version>
	<executions>
		<execution>
			<id>check-json-annotations</id>
			<phase>compile</phase>
			<goals>
				<goal>java</goal>
			</goals>
		</execution>
	</executions>
	<configuration>
		<includeProjectDependencies>true</includeProjectDependencies>
		<includePluginDependencies>true</includePluginDependencies>
		<mainClass>com.fasterxml.jackson.contrib.jsonpath.annotation.AnnotationVerifier</mainClass>
		<killAfter>-1</killAfter>
		<arguments>
			<argument>${project.basedir}/src/main/java/com/elasticpath/</argument>
		</arguments>
	</configuration>
</plugin>
```
## Source
Built with Java 7 and Maven:
run `mvn clean install` to build

## Unit Tests
* Test json data can be found in `src/test/resources`
* Test pojo classes can be found in `src/test/java/com/fasterxml/jackson/contrib/jsonpath/data`


