package com.github.enerccio.gson.tests.builders;

import com.github.enerccio.gson.builders.JsonBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

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
}
