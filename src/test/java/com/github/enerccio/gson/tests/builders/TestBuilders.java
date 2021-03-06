package com.github.enerccio.gson.tests.builders;

import com.github.enerccio.gson.builders.JsonBuilder;
import com.github.enerccio.gson.builders.functional.IArrayBuilder;
import com.github.enerccio.gson.builders.functional.IArrayFacade;
import com.github.enerccio.gson.builders.functional.IObjectBuilder;
import com.github.enerccio.gson.builders.functional.IObjectFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestBuilders extends TestCase {
	public TestBuilders(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(TestBuilders.class);
	}

	public void testJsonBuildersPrimitives() {
		String value;

		// @formatter:off
		value = new JsonBuilder()
					.string("test")
				.toJson();
		// @formatter:on

		Assert.assertEquals("\"test\"", value);

		// @formatter:off
		value = new JsonBuilder()
				.number(1)
			.toJson();
		// @formatter:on

		Assert.assertEquals("1", value);

		// @formatter:off
		value = new JsonBuilder()
				.number(1.5)
			.toJson();
		// @formatter:on

		Assert.assertEquals("1.5", value);

		// @formatter:off
		value = new JsonBuilder()
				.character('a')
			.toJson();
		// @formatter:on

		Assert.assertEquals("\"a\"", value);

		// @formatter:off
		value = new JsonBuilder()
				.bool(true)
			.toJson();
		// @formatter:on

		Assert.assertEquals("true", value);

		// @formatter:off
		value = new JsonBuilder()
				.bool(false)
			.toJson();
		// @formatter:on

		Assert.assertEquals("false", value);

		// @formatter:off
		value = new JsonBuilder()
				.nil()
			.toJson();
		// @formatter:on

		Assert.assertEquals("null", value);
	}

	public void testJsonBuildersFlatArray() {
		String value;

		// @formatter:off
		value = new JsonBuilder()
					.array()
					.end()
				.toJson();
		// @formatter:on

		Assert.assertEquals("[]", value);

		// default gson is pretty print, bad for testing
		Gson gson = new GsonBuilder().create();

		// @formatter:off
		value = new JsonBuilder()
				.setGson(gson)
				.array()
					.string("a")
					.string("b")
					.string("c")
					.string("d")
					.string("e")
					.string("f")
				.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals("[\"a\",\"b\",\"c\",\"d\",\"e\",\"f\"]", value);

		// @formatter:off
		value = new JsonBuilder()
				.setGson(gson)
				.array()
					.number(1)
					.number(2)
					.number(3)
					.number(4)
					.number(5)
					.number(6)
				.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals("[1,2,3,4,5,6]", value);

		// @formatter:off
		value = new JsonBuilder()
				.setGson(gson)
				.array()
					.character('a')
					.character('b')
					.character('c')
					.character('d')
					.character('e')
					.character('f')
				.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals("[\"a\",\"b\",\"c\",\"d\",\"e\",\"f\"]", value);

		// @formatter:off
		value = new JsonBuilder()
				.setGson(gson)
				.array()
					.bool(true)
					.bool(false)
					.bool(true)
					.bool(false)
					.bool(true)
					.bool(false)
				.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals("[true,false,true,false,true,false]", value);

		// @formatter:off
		value = new JsonBuilder()
				.setGson(gson)
				.array()
					.nil()
					.nil()
					.nil()
					.nil()
					.nil()
					.nil()
				.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals("[null,null,null,null,null,null]", value);

		// @formatter:off
		value = new JsonBuilder()
				.setGson(gson)
				.array()
					.string("a")
					.number(1)
					.number(1.5)
					.character('x')
					.bool(true)
					.nil()
				.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals("[\"a\",1,1.5,\"x\",true,null]", value);
	}

	public void testJsonBuildersFlatObject() {
		String value;

		// @formatter:off
		value = new JsonBuilder()
				.object()
				.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals("{}", value);

		// default gson is pretty print, bad for testing
		Gson gson = new GsonBuilder().create();

		// @formatter:off
		value = new JsonBuilder()
				.setGson(gson)
				.object()
					.property("1").string("a")
					.property("2").string("b")
					.property("3").string("c")
					.property("4").string("d")
					.property("5").string("e")
					.property("6").string("f")
				.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals("{\"1\":\"a\",\"2\":\"b\",\"3\":\"c\",\"4\":\"d\",\"5\":\"e\",\"6\":\"f\"}", value);

		// @formatter:off
		value = new JsonBuilder()
				.setGson(gson)
				.object()
					.property("1").number(1)
					.property("2").number(2)
					.property("3").number(3)
					.property("4").number(4)
					.property("5").number(5)
					.property("6").number(6)
				.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals("{\"1\":1,\"2\":2,\"3\":3,\"4\":4,\"5\":5,\"6\":6}", value);

		// @formatter:off
		value = new JsonBuilder()
				.setGson(gson)
				.object()
					.property("1").character('a')
					.property("2").character('b')
					.property("3").character('c')
					.property("4").character('d')
					.property("5").character('e')
					.property("6").character('f')
				.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals("{\"1\":\"a\",\"2\":\"b\",\"3\":\"c\",\"4\":\"d\",\"5\":\"e\",\"6\":\"f\"}", value);

		// @formatter:off
		value = new JsonBuilder()
				.setGson(gson)
				.object()
					.property("1").bool(true)
					.property("2").bool(false)
					.property("3").bool(true)
					.property("4").bool(false)
					.property("5").bool(true)
					.property("6").bool(false)
				.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals("{\"1\":true,\"2\":false,\"3\":true,\"4\":false,\"5\":true,\"6\":false}", value);

		// @formatter:off
		value = new JsonBuilder()
				.setGson(gson)
				.object()
					.property("1").nil()
					.property("2").nil()
					.property("3").nil()
					.property("4").nil()
					.property("5").nil()
					.property("6").nil()
				.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals("{}", value);

		// @formatter:off
		value = new JsonBuilder()
				.setGson(gson)
				.object()
					.property("1").string("a")
					.property("2").number(1)
					.property("3").number(1.5)
					.property("4").character('x')
					.property("5").bool(true)
					.property("6").nil()
				.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals("{\"1\":\"a\",\"2\":1,\"3\":1.5,\"4\":\"x\",\"5\":true}", value);
	}

	public void testJsonBuildersComplex() {
		String value;

		// default gson is pretty print, bad for testing
		Gson gson = new GsonBuilder().create();

		// @formatter:off
		value = new JsonBuilder()
				.setGson(gson)
				.object()
					.property("configuration").object()
						.property("ip").string("127.0.0.1")
						.property("port").number(1234)
						.property("user").string("johnWick")
						.property("password").string("doggo")
						.property("isAdmin").bool(true)
						.property("maxResponseTimeSeconds").number(1.5)
						.property("allowedActions").array()
							.number(1)
							.number(2)
							.number(6)
						.end()
					.end()
				.end()
				.toJson();
		// @formatter:on

		Assert.assertEquals(
				"{\"configuration\":{\"ip\":\"127.0.0.1\",\"port\":1234,\"user\":\"johnWick\",\"password\":\"doggo\",\"isAdmin\":true,\"maxResponseTimeSeconds\":1.5,\"allowedActions\":[1,2,6]}}",
				value);

	}

	public void testJsonBuildersTree() {
		// sync jsons for both generations
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// @formatter:off
		String value = new JsonBuilder(gson)
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
		// @formatter:on

		// @formatter:off
		JsonElement value2 = new JsonBuilder(gson)
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
			.toJsonTree();
		// @formatter:on

		Assert.assertEquals(value, gson.toJson(value2));
	}

	public void testJsonBuildersFunctional() {
		// sync jsons for both generations
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String value1, value2;

		// @formatter:off
		value1 = new JsonBuilder()
			.setGson(gson)
			.object()
				.property("foo").string("bar")
			.end()
			.toJson();
		// @formatter:on

		// @formatter:off
		value2 = new JsonBuilder()
			.setGson(gson)
			.object(new IObjectBuilder() {
				
				@Override
				public void build(IObjectFacade object) {
					object.put("foo", "bar");
				}
			})
			.toJson();
		// @formatter:on

		Assert.assertEquals(value1, value2);

		// @formatter:off
		value1 = new JsonBuilder()
			.setGson(gson)
			.array()
				.string("foo")
				.string("bar")
			.end()
			.toJson();
		// @formatter:on

		// @formatter:off
		value2 = new JsonBuilder()
			.setGson(gson)
			.array(new IArrayBuilder() {
				
				@Override
				public void build(IArrayFacade array) {
					array.add("foo");
					array.add("bar");
				}
			})
			.toJson();
		// @formatter:on

		Assert.assertEquals(value1, value2);

		// @formatter:off
		value1 = new JsonBuilder()
			.setGson(gson)
			.array()
				.object()
					.property("foo").array()
						.string("bar")
					.end()
				.end()
			.end()
			.toJson();
		// @formatter:on

		// @formatter:off
		value2 = new JsonBuilder()
			.setGson(gson)
			.array(new IArrayBuilder() {
				
				@Override
				public void build(IArrayFacade array) {
					array.addObject(new IObjectBuilder() {
						
						@Override
						public void build(IObjectFacade object) {
							object.putArray("foo", new IArrayBuilder() {
								
								@Override
								public void build(IArrayFacade array) {
									array.add("bar");
								}
							});
						}
					});
				}
			})
			.toJson();
		// @formatter:on

		Assert.assertEquals(value1, value2);

		// @formatter:off
		value1 = new JsonBuilder(gson)
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
		// @formatter:on

		// @formatter:off
		value2 = new JsonBuilder()
			.setGson(gson)
			.object(new IObjectBuilder() {
				
				@Override
				public void build(IObjectFacade root) {
					root.putArray("texts", new IArrayBuilder() {
						
						@Override
						public void build(IArrayFacade texts) {
							texts.add("Welcome to the hotel");
							texts.add("User %s!");
						}
					});
					root.put("singleton", true);
					root.putObject("configuration", new IObjectBuilder() {
						
						@Override
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
		// @formatter:on

		Assert.assertEquals(value1, value2);

		// @formatter:off
		value1 = new JsonBuilder()
			.setGson(gson)
			.object()
				.property("foo").object()
					.property("bar").number(1)
				.end()
			.end()
			.toJson();
		// @formatter:on

		// @formatter:off
		value2 = new JsonBuilder()
			.setGson(gson)
			.object()
				.property("foo").object(new IObjectBuilder() {
					
					@Override
					public void build(IObjectFacade object) {
						object.put("bar", 1);
					}
					
				})
			.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals(value1, value2);

		// @formatter:off
		value1 = new JsonBuilder()
			.setGson(gson)
			.array()
				.array()
					.number(1)
				.end()
			.end()
			.toJson();
		// @formatter:on

		// @formatter:off
		value2 = new JsonBuilder()
			.setGson(gson)
			.array()
				.array(new IArrayBuilder() {
					
					@Override
					public void build(IArrayFacade array) {
						array.add(1);
					}
				})
			.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals(value1, value2);
	}

	public void testJsonManual() {
		// sync jsons for both generations
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String value1, value2;

		// @formatter:off
		value1 = new JsonBuilder()
				.setGson(gson)
				.object()
					.property("configuration").object()
						.property("allowedIP").array()
							.string("127.0.0.1")
							.string("::1")
							.string("mydomain.com")
						.end()
						.property("login").string("root")
						.property("password").string("password")
						.property("settings").array()
							.object()
								.property("name").string("idle")
								.property("max_download_window").number(10000)
								.property("max_connection_timeout").number(30000)
							.end()
							.object()
								.property("name").string("low")
								.property("max_download_window").number(5000)
								.property("max_connection_timeout").number(15000)
							.end()
							.object()
								.property("name").string("high")
								.property("max_download_window").number(2500)
								.property("max_connection_timeout").number(10000)
							.end()
						.end()
					.end()
				.end()
				.toJson();
		// @formatter:on

		JsonObject root = new JsonObject();
		JsonObject configuration = new JsonObject();
		root.add("configuration", configuration);
		JsonArray allowedIp = new JsonArray();
		configuration.add("allowedIP", allowedIp);
		allowedIp.add(new JsonPrimitive("127.0.0.1"));
		allowedIp.add(new JsonPrimitive("::1"));
		allowedIp.add(new JsonPrimitive("mydomain.com"));
		configuration.add("login", new JsonPrimitive("root"));
		configuration.add("password", new JsonPrimitive("password"));
		JsonArray settings = new JsonArray();
		configuration.add("settings", settings);
		JsonObject idle = new JsonObject();
		settings.add(idle);
		idle.add("name", new JsonPrimitive("idle"));
		idle.add("max_download_window", new JsonPrimitive(10000));
		idle.add("max_connection_timeout", new JsonPrimitive(30000));
		JsonObject low = new JsonObject();
		settings.add(low);
		low.add("name", new JsonPrimitive("low"));
		low.add("max_download_window", new JsonPrimitive(5000));
		low.add("max_connection_timeout", new JsonPrimitive(15000));
		JsonObject high = new JsonObject();
		settings.add(high);
		high.add("name", new JsonPrimitive("high"));
		high.add("max_download_window", new JsonPrimitive(2500));
		high.add("max_connection_timeout", new JsonPrimitive(10000));
		value2 = gson.toJson(root);

		Assert.assertEquals(value1, value2);
	}

	public void testBuildersInsertObjects() {
		// remove pretty printing
		Gson gson = new GsonBuilder().create();

		final JsonElement inner = gson.fromJson("[1,2,3]", JsonArray.class);

		String value;

		// @formatter:off
		value = new JsonBuilder()
			.setGson(gson)
			.object()
				.property("myproperty").element(inner)
			.end()
			.toJson();
		// @formatter:on

		Assert.assertEquals("{\"myproperty\":[1,2,3]}", value);

		// @formatter:off
		value = new JsonBuilder()
			.setGson(gson)
			.object(new IObjectBuilder() {
				
				@Override
				public void build(IObjectFacade object) {
					object.put("myproperty", inner);
				}
			})
			.toJson();
		// @formatter:on

		Assert.assertEquals("{\"myproperty\":[1,2,3]}", value);
	}

	public void testBuilderShortcuts() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String value1, value2;

		// @formatter:off
		value1 = new JsonBuilder()
				.setGson(gson)
				.object()
					.property("foo").string("bar")
					.property("baz").number(1)
					.property("qux").bool(true)
				.end()
				.toJson();
		// @formatter:on

		// @formatter:off
		value2 = new JsonBuilder()
				.setGson(gson)
				.object()
					.string("foo", "bar")
					.number("baz", 1)
					.bool("qux", true)
				.end()
				.toJson();
		// @formatter:on

		Assert.assertEquals(value1, value2);
	}
}
