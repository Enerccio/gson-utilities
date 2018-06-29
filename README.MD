# Gson Utilities

Collection of utility classes to be used with google gson.
These utilites are licensed under MIT license. 

[![Build Status](https://travis-ci.org/Enerccio/gson-utilities.svg?branch=master)](https://travis-ci.org/Enerccio/gson-utilities)[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.enerccio/gson-utilities/badge.png)](https://maven-badges.herokuapp.com/maven-central/com.github.enerccio/gson-utilities)[![Javadocs](http://www.javadoc.io/badge/com.github.enerccio/gson-utilities.png?color=red)](http://www.javadoc.io/doc/com.github.enerccio/gson-utilities)

## Using gson-utilities

To use this library, simply add this to your maven dependencies:

```xml
<dependency>
  <groupId>com.github.enerccio</groupId>
  <artifactId>gson-utilities</artifactId>
  <version>1.0.1</version>
</dependency>
```

## Manual install

Clone the repository and run `mvn package`. 

## List of utilities

### JsonBuilder

This utility builder provides a way to create json strings/values structurally but still using google gson as backing serializer.

#### Examples

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
