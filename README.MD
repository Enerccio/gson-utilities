# Gson Utilities

Collection of utility classes to be used with google gson.
These utilities are licensed under MIT license. 

[![Build Status](https://travis-ci.org/Enerccio/gson-utilities.svg?branch=master)](https://travis-ci.org/Enerccio/gson-utilities)[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.enerccio/gson-utilities/badge.png)](https://maven-badges.herokuapp.com/maven-central/com.github.enerccio/gson-utilities)[![Javadocs](http://www.javadoc.io/badge/com.github.enerccio/gson-utilities.png?color=red)](http://www.javadoc.io/doc/com.github.enerccio/gson-utilities)

## Using gson-utilities

To use this library, simply add this to your maven dependencies:

```xml
<dependency>
  <groupId>com.github.enerccio</groupId>
  <artifactId>gson-utilities</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Manual install

Clone the repository and run `mvn package`. 

## List of utilities

### JsonBuilder

This utility builder provides a way to create JSON strings/values structurally but still using google gson as backing serializer.

#### Examples

##### Standard JSON builder

This is standard Java builder implementation that uses methods to create values.

```java
String value = new JsonBuilder()
				.object()
					.property("texts").array()
						.string("Welcome to the hotel")
						.string("User %s!")
					.end()
					.property("singleton").bool(true)
					.property("configuration").object()
						.property("ip").string("127.0.0.1")
						.property("port").number(1234)
						.property("user").string("johnWick")
						.property("password").string("doggo")
						.property("isAdmin").bool(true)
						.property("maxResponseTimeSeconds").number(1.5)
					.end()
				.end()
				.toJson();
```

value will contain: 

```json
{
  "texts": [
    "Welcome to the hotel",
    "User %s!"
  ],
  "singleton": true,
  "configuration": {
    "ip": "127.0.0.1",
    "port": 1234,
    "user": "johnWick",
    "password": "doggo",
    "isAdmin": true,
    "maxResponseTimeSeconds": 1.5
  }
}
```

##### Functional JSON builder

You can use functional interface builders for objects/arrays in JsonBuilder.

###### Java <1.8
```java
String value = new JsonBuilder()
			.setGson(gson)
			.object(new IObjectBuilder() {
				
				public void build(IObjectFacade root) {
					root.putArray("texts", new IArrayBuilder() {
						
						public void build(IArrayFacade texts) {
							texts.add("Welcome to the hotel");
							texts.add("User %s!");
						}
					});
					root.put("singleton", true);
					root.putObject("configuration", new IObjectBuilder() {
						
						public void build(IObjectFacade configuration) {
							configuration.put("ip", "127.0.0.1");
							configuration.put("port", 1234);
							configuration.put("user", "johnWick");
							configuration.put("password", "doggo");
							configuration.put("isAdmin", true);
							configuration.put("maxResponseTimeSeconds", 1.5);
						}
					});
				}
			})
			.toJson();
```

###### Java >=1.8
```java
String value = new JsonBuilder()
			.object(root -> {
					root.putArray("texts", texts -> {
						texts.add("Welcome to the hotel");
						texts.add("User %s!");
					});
					root.put("singleton", true);
					root.putObject("configuration", configuration -> {
						configuration.put("ip", "127.0.0.1");
						configuration.put("port", 1234);
						configuration.put("user", "johnWick");
						configuration.put("password", "doggo");
						configuration.put("isAdmin", true);
						configuration.put("maxResponseTimeSeconds", 1.5);
					});
				})
			.toJson();
```
 
In both cases the value will be:

```json
{
  "texts": [
    "Welcome to the hotel",
    "User %s!"
  ],
  "singleton": true,
  "configuration": {
    "ip": "127.0.0.1",
    "port": 1234,
    "user": "johnWick",
    "password": "doggo",
    "isAdmin": true,
    "maxResponseTimeSeconds": 1.5
  }
}
```

### JSON Tree Visitors

These visitors allows to visit each element of JSON in the JSON string.
In version 1.1.0, only `JsonTreeVisitor` is available.

#### JsonTreeVisitor

This in memory visitor that deserializes JSON into `JsonElement`s and then visits them. Supports also visiting already created `JsonElement`.

#### Examples

```java
IJsonVisitor visitor = new JsonTreeVisitor();
visitor.setListener(new IJsonVisitorListener() {
	
	public void onElementStart(IJsonElementEntry entry) {
		// this is checked when element is about to be entered
	}
	
	public void onElementEnd(IJsonElementExit exit) {
		// This is checked when element is fully available
	}
});
visitor.visit(jsonString);
```

#### JsonStreamVisitor

Visits the JSON string stream like, ie JSON is not first deserialized then visited but it is being visited as it is being deserialized.

Optionally can return deserialized JsonElement.

#### Examples

```java
IJsonVisitor visitor = new JsonStreamVisitor();
visitor.setListener(new IJsonVisitorListener() {
	
	public void onElementStart(IJsonElementEntry entry) {
		// this is checked when element is about to be entered
	}
	
	public void onElementEnd(IJsonElementExit exit) {
		// This is checked when element is fully available
	}
});
visitor.visit(jsonString);
```

For more available properties or visitors, see api doc.

### Json Tree Matcher

When using visitors, both `IJsonElementEntry` and `IJsonElementExit` provide method `getTree()` which returns current nesting of properties and array indexes all up to current element. You can use `JsonPatternMatcher` to match against a pattern. For pattern syntax, see `JsonPatternMatcher`. 

### Json Helper

Collection of utilities related to work with JSON.

#### getFirst

Retrieves first `JsonElement` that matches the pattern provided (see Json Tree Matcher) and optionally predicate.

##### Examples

```json
{ 
	"a":
	{ 
		"b" : 
		{
			"c": 
			{ 
				"d" : "x"
			}
		}
	}
}
```

```java
JsonElement e = JsonHelper.getFirst(value, "a.b.c.d");
// e will contain JsonPrimitive with string value "x"
```

```json
{ 
	"a":
	{ 
		"b" : 
		{
			"c": 
			{ 
				"d" : 4
			},
			"e":
			{
				"d" : "x"
			}
		}
	}
}
``` 

```java
JsonElement e = JsonHelper.getFirst(value, "a.**.d", e -> {
	return e.isJsonPrimitive() && e.getAsJsonPrimitive().isString();
});
// e will contain JsonPrimitive with string value "x"
```

#### getAll

Same as with `getFirst` but retrieves list of all matching elements that optionally have to pass predicate.