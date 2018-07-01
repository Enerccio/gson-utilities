package com.github.enerccio.gson.tests;

import java.text.ParseException;
import java.util.List;

import com.github.enerccio.gson.GsonProvider;
import com.github.enerccio.gson.JsonHelper;
import com.github.enerccio.gson.JsonHelper.IJsonPredicate;
import com.github.enerccio.gson.builders.JsonBuilder;
import com.github.enerccio.gson.tree.JsonPatternMatcher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestJsonHelper extends TestCase {

	public TestJsonHelper(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(TestJsonHelper.class);
	}

	public void testGetFirst() throws ParseException {
		String value;
		JsonElement e;

		// @formatter:off
		value = new JsonBuilder()
			.object()
				.property("a").object()
					.property("b").object()
						.property("c").object()
							.property("d").string("x")
						.end()
					.end()
				.end()
			.end()
			.toJson();
		// @formatter:on

		e = JsonHelper.getFirst(value, "a.b.c.d");
		Assert.assertNotNull(e);
		Assert.assertNotSame(JsonNull.INSTANCE, e);
		Assert.assertTrue(e.isJsonPrimitive());
		Assert.assertTrue(e.getAsJsonPrimitive().isString());
		Assert.assertEquals("x", e.getAsJsonPrimitive().getAsString());

		// @formatter:off
		value = new JsonBuilder()
			.object()
				.property("a").object()
					.property("b").object()
						.property("c").object()
							.property("d").string("x")
							.property("e").number(1)
							.property("f").number(1)
						.end()
					.end()
				.end()
			.end()
			.toJson();
		// @formatter:on

		e = JsonHelper.getFirst(value, "a.**.e");
		Assert.assertNotNull(e);
		Assert.assertNotSame(JsonNull.INSTANCE, e);
		Assert.assertTrue(e.isJsonPrimitive());
		Assert.assertTrue(e.getAsJsonPrimitive().isNumber());
		Assert.assertEquals(1, e.getAsJsonPrimitive().getAsInt());

		// @formatter:off
		value = new JsonBuilder()
			.object()
				.property("a").array()
					.string("foo")
					.object()
						.property("bar").string("baz")
					.end()
					.object()
						.property("bar").string("qux")
					.end()
				.end()
			.end()
			.toJson();
		// @formatter:on

		e = JsonHelper.getFirst(value, "a.@2.bar");
		Assert.assertNotNull(e);
		Assert.assertNotSame(JsonNull.INSTANCE, e);
		Assert.assertTrue(e.isJsonPrimitive());
		Assert.assertTrue(e.getAsJsonPrimitive().isString());
		Assert.assertEquals("qux", e.getAsJsonPrimitive().getAsString());
	}

	public void testGetFirstPredicate() throws ParseException {
		String value;
		JsonElement e;

		// @formatter:off
		value = new JsonBuilder()
			.object()
				.property("a").array()
					.string("foo")
					.object()
						.property("bar").number(32)
					.end()
					.object()
						.property("bar").string("qux")
					.end()
				.end()
			.end()
			.toJson();
		// @formatter:on

		e = JsonHelper.getFirst(value, "a.**.bar", new IJsonPredicate() {

			@Override
			public boolean test(JsonElement e) {
				return e.isJsonPrimitive() && e.getAsJsonPrimitive().isString();
			}
		});
		Assert.assertNotNull(e);
		Assert.assertNotSame(JsonNull.INSTANCE, e);
		Assert.assertTrue(e.isJsonPrimitive());
		Assert.assertTrue(e.getAsJsonPrimitive().isString());
		Assert.assertEquals("qux", e.getAsJsonPrimitive().getAsString());

		// @formatter:off
		value = new JsonBuilder()
			.object()
				.property("a").array()
					.string("foo")
					.object()
						.property("bar").string("qux")
					.end()
					.object()
						.property("bar").number(32)
					.end()
					.object()
						.property("bar").number(-110)
					.end()
				.end()
			.end()
			.toJson();
		// @formatter:on

		e = JsonHelper.getFirst(value, "a.**.bar", new IJsonPredicate() {

			@Override
			public boolean test(JsonElement e) {
				return e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber() && e.getAsInt() <= 0;
			}
		});
		Assert.assertNotNull(e);
		Assert.assertNotSame(JsonNull.INSTANCE, e);
		Assert.assertTrue(e.isJsonPrimitive());
		Assert.assertTrue(e.getAsJsonPrimitive().isNumber());
		Assert.assertEquals(-110, e.getAsJsonPrimitive().getAsInt());
	}

	public void testGetAll() throws ParseException {
		String value;
		List<JsonElement> el;

		// @formatter:off
		value = new JsonBuilder()
			.object()
				.property("a").object()
					.property("b").object()
						.property("c").object()
							.property("d").string("x")
						.end()
						.property("d").string("x")
					.end()
					.property("d").string("x")
				.end()
				.property("d").string("x")
			.end()
			.toJson();
		// @formatter:on

		el = JsonHelper.getAll(value, "**.d");
		Assert.assertEquals(4, el.size());
		for (JsonElement e : el) {
			Assert.assertTrue(e.isJsonPrimitive());
			Assert.assertTrue(e.getAsJsonPrimitive().isString());
			Assert.assertEquals("x", e.getAsString());
		}

		// @formatter:off
		value = new JsonBuilder()
			.array()
				.array()
					.string("fail")
					.string("hit")
					.string("fail")
					.array()
						.string("fail")
					.end()
					.array()
						.string("fail")
						.string("hit")
					.end()
				.end()
				.string("hit")
				.string("fail")
			.end()
			.toJson();
		// @formatter:on

		el = JsonHelper.getAll(value, "**.@1"); // grab all that is second in
												// every array in json
		Assert.assertEquals(3, el.size());
		for (JsonElement e : el) {
			Assert.assertTrue(e.isJsonPrimitive());
			Assert.assertTrue(e.getAsJsonPrimitive().isString());
			Assert.assertEquals("hit", e.getAsString());
		}
	}

	public void testGetN() throws ParseException {
		String value;
		List<JsonElement> el;

		// @formatter:off
		value = new JsonBuilder()
			.object()
				.property("a").object()
					.property("b").object()
						.property("c").object()
							.property("d").string("x")
						.end()
						.property("d").string("x")
					.end()
					.property("d").string("x")
				.end()
				.property("d").string("x")
			.end()
			.toJson();
		// @formatter:on

		el = JsonHelper.getN(value, "**.d", 3);
		Assert.assertEquals(3, el.size());
		for (JsonElement e : el) {
			Assert.assertTrue(e.isJsonPrimitive());
			Assert.assertTrue(e.getAsJsonPrimitive().isString());
			Assert.assertEquals("x", e.getAsString());
		}

		// @formatter:off
		value = new JsonBuilder()
			.array()
				.array()
					.string("fail")
					.string("hit")
					.string("fail")
					.array()
						.string("fail")
					.end()
					.array()
						.string("fail")
						.string("hit")
					.end()
				.end()
				.string("hit")
				.string("fail")
			.end()
			.toJson();
		// @formatter:on

		el = JsonHelper.getN(value, "**.@1", 2); // grab 2 that are second in
													// every array in JSON
		Assert.assertEquals(2, el.size());
		for (JsonElement e : el) {
			Assert.assertTrue(e.isJsonPrimitive());
			Assert.assertTrue(e.getAsJsonPrimitive().isString());
			Assert.assertEquals("hit", e.getAsString());
		}
	}

	public void testGetAllPredicate() throws ParseException {
		String value;
		List<JsonElement> el;

		// @formatter:off
		value = new JsonBuilder()
			.object()
				.property("a").object()
					.property("b").object()
						.property("c").object()
							.property("d").string("x")
						.end()
						.property("d").bool(false)
					.end()
					.property("d").string("x")
				.end()
				.property("d").bool(false)
			.end()
			.toJson();
		// @formatter:on

		el = JsonHelper.getAll(value, "**.d", new IJsonPredicate() {

			@Override
			public boolean test(JsonElement e) {
				return e.isJsonPrimitive() && e.getAsJsonPrimitive().isString();
			}
		});
		Assert.assertEquals(2, el.size());
		for (JsonElement e : el) {
			Assert.assertTrue(e.isJsonPrimitive());
			Assert.assertTrue(e.getAsJsonPrimitive().isString());
			Assert.assertEquals("x", e.getAsString());
		}

		// @formatter:off
		value = new JsonBuilder()
			.array()
				.array()
					.string("fail")
					.number(-10)
					.string("fail")
					.array()
						.string("fail")
					.end()
					.array()
						.string("fail")
						.number(10)
					.end()
				.end()
				.string("fail")
				.string("fail")
			.end()
			.toJson();
		// @formatter:on

		el = JsonHelper.getAll(value, "**.@1", new IJsonPredicate() {

			@Override
			public boolean test(JsonElement e) {
				return e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber() && e.getAsInt() <= 0;
			}
		});
		Assert.assertEquals(1, el.size());
		for (JsonElement e : el) {
			Assert.assertTrue(e.isJsonPrimitive());
			Assert.assertTrue(e.getAsJsonPrimitive().isNumber());
			Assert.assertEquals(-10, e.getAsInt());
		}
	}

	public void testMethodEquivalence() throws ParseException {
		String value;
		List<JsonElement> el1;
		List<JsonElement> el2;
		List<JsonElement> el3;
		List<JsonElement> el4;
		List<JsonElement> el5;
		List<JsonElement> el6;
		JsonElement e1;
		JsonElement e2;
		JsonElement e3;
		JsonElement e4;
		JsonElement e5;
		JsonElement e6;

		// @formatter:off
		value = new JsonBuilder()
			.array()
				.array()
					.string("fail")
					.string("hit")
					.string("fail")
					.array()
						.string("fail")
					.end()
					.array()
						.string("fail")
						.string("hit")
					.end()
				.end()
				.string("hit")
				.string("fail")
			.end()
			.toJson();
		// @formatter:on

		JsonPatternMatcher matcher = new JsonPatternMatcher("**.@1");
		JsonElement e = GsonProvider.sharedGson.fromJson(value, JsonElement.class);
		IJsonPredicate p = new IJsonPredicate() {

			@Override
			public boolean test(JsonElement e) {
				return true;
			}
		};

		e1 = JsonHelper.getFirst(value, "**.@1");
		e2 = JsonHelper.getFirst(value, matcher);
		e3 = JsonHelper.getFirst(e, "**.@1");
		e4 = JsonHelper.getFirst(value, "**.@1", p);
		e5 = JsonHelper.getFirst(e, matcher, p);
		e6 = JsonHelper.getFirst(e, "**.@1", p);

		Assert.assertEquals(e1, e2);
		Assert.assertEquals(e1, e3);
		Assert.assertEquals(e1, e4);
		Assert.assertEquals(e1, e5);
		Assert.assertEquals(e1, e6);

		el1 = JsonHelper.getAll(value, "**.@1");
		el2 = JsonHelper.getAll(value, matcher);
		el3 = JsonHelper.getAll(e, "**.@1");
		el4 = JsonHelper.getAll(value, "**.@1", p);
		el5 = JsonHelper.getAll(e, matcher, p);
		el6 = JsonHelper.getAll(e, "**.@1", p);

		Assert.assertEquals(el1, el2);
		Assert.assertEquals(el1, el3);
		Assert.assertEquals(el1, el4);
		Assert.assertEquals(el1, el5);
		Assert.assertEquals(el1, el6);

		el1 = JsonHelper.getN(value, "**.@1", 1);
		el2 = JsonHelper.getN(value, matcher, 1);
		el3 = JsonHelper.getN(e, "**.@1", 1);
		el4 = JsonHelper.getN(value, "**.@1", p, 1);
		el5 = JsonHelper.getN(e, matcher, p, 1);
		el6 = JsonHelper.getN(e, "**.@1", p, 1);

		Assert.assertEquals(el1, el2);
		Assert.assertEquals(el1, el3);
		Assert.assertEquals(el1, el4);
		Assert.assertEquals(el1, el5);
		Assert.assertEquals(el1, el6);
	}
	
	public void testArraySet() {
		Gson gson = new GsonBuilder().create();
		JsonElement e;
		JsonArray a;
		
		// @formatter:off
		e = new JsonBuilder()
			.array()
				.number(1)
				.number(2)
				.number(3)
			.end()
			.toJsonTree();
		// @formatter:on
		
		Assert.assertTrue(e.isJsonArray());
		Assert.assertEquals("[1,2,3]", gson.toJson(e));
		
		a = e.getAsJsonArray();
		Assert.assertEquals("[1,2,3]", gson.toJson(a));
		
		JsonHelper.arraySet(a, 0, new JsonPrimitive(4));
		JsonHelper.arraySet(a, 1, new JsonPrimitive(5));
		JsonHelper.arraySet(a, 2, new JsonPrimitive(6));
		
		Assert.assertEquals("[4,5,6]", gson.toJson(a));
		Assert.assertEquals("[4,5,6]", gson.toJson(e));
	}
}
