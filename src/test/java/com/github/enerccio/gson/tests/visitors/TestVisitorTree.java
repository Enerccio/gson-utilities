package com.github.enerccio.gson.tests.visitors;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.enerccio.gson.builders.JsonBuilder;
import com.github.enerccio.gson.tree.TreeInfo;
import com.github.enerccio.gson.visitors.IJsonElementEntry;
import com.github.enerccio.gson.visitors.IJsonElementExit;
import com.github.enerccio.gson.visitors.IJsonVisitor;
import com.github.enerccio.gson.visitors.IJsonVisitorListener;
import com.github.enerccio.gson.visitors.JsonElementType;
import com.github.enerccio.gson.visitors.JsonTreeVisitor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestVisitorTree extends TestCase {

	public TestVisitorTree(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(TestVisitorTree.class);
	}

	public void testVisitorTreeObjectVisit() {
		String value;

		final AtomicInteger treeTestIndex = new AtomicInteger(0);
		final List<String> treeTests = new ArrayList<String>();
		final List<Boolean> trees = new ArrayList<Boolean>();
		final List<String> properties = new ArrayList<String>();
		final List<Object> v = new ArrayList<Object>();
		final List<JsonElementType> typesEntry = new ArrayList<JsonElementType>();
		final List<JsonElementType> typesExit = new ArrayList<JsonElementType>();
		final List<Integer> arrayPositions = new ArrayList<Integer>();

		IJsonVisitor visitor = new JsonTreeVisitor();
		visitor.setListener(new IJsonVisitorListener() {

			@Override
			public void onElementStart(IJsonElementEntry entry) {
				Collection<TreeInfo> tree = entry.getTree();
				JsonElementType type = entry.getType();

				try {
					trees.add(TreeInfo.matches(tree, treeTests.get(treeTestIndex.getAndIncrement())));
				} catch (ParseException e) {
					Assert.fail(e.getMessage());
				}

				typesEntry.add(type);
			}

			@Override
			public void onElementEnd(IJsonElementExit exit) {
				Collection<TreeInfo> tree = exit.getTree();
				JsonElementType type = exit.getType();
				JsonElement element = exit.getElement();
				String property = exit.getPropertyName();
				int pos = exit.getArrayPosition();

				try {
					trees.add(TreeInfo.matches(tree, treeTests.get(treeTestIndex.getAndIncrement())));
				} catch (ParseException e) {
					Assert.fail(e.getMessage());
				}
				typesExit.add(type);
				properties.add(property);
				arrayPositions.add(pos);

				if (element != null && element.isJsonPrimitive()) {
					switch (type) {
					case BOOLEAN:
						v.add(element.getAsBoolean());
						break;
					case NULL:
						v.add(null);
						break;
					case NUMBER:
						v.add(element.getAsNumber().intValue());
						break;
					case STRING:
						v.add(element.getAsString());
						break;
					default:
						break;
					}
				}
			}
		});

		/* test 1 */

		treeTestIndex.set(0);
		treeTests.clear();
		trees.clear();
		properties.clear();
		v.clear();
		typesEntry.clear();
		typesExit.clear();
		arrayPositions.clear();
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
		treeTests.addAll(Arrays.asList("", "a", "a", "a.b", "a.b", "a.b.c", "a.b.c", "a.b.c.d", "a.b.c.d", "a.b.c.d",
				"a.b.c.d", "a.b.c", "a.b.c", "a.b", "a.b", "a", "a", ""));
		visitor.visit(value);
		Assert.assertEquals(trees, Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true,
				true, true, true, true, true, true));
		Assert.assertEquals(properties, Arrays.asList("d", "d", "c", "c", "b", "b", "a", "a", null));
		Assert.assertEquals(arrayPositions, Arrays.asList(-1, -1, -1, -1, -1, -1, -1, -1, -1));
		Assert.assertEquals(typesEntry,
				Arrays.asList(JsonElementType.OBJECT, JsonElementType.PROPERTY, JsonElementType.OBJECT,
						JsonElementType.PROPERTY, JsonElementType.OBJECT, JsonElementType.PROPERTY,
						JsonElementType.OBJECT, JsonElementType.PROPERTY, JsonElementType.STRING));
		Assert.assertEquals(typesExit,
				Arrays.asList(JsonElementType.STRING, JsonElementType.PROPERTY, JsonElementType.OBJECT,
						JsonElementType.PROPERTY, JsonElementType.OBJECT, JsonElementType.PROPERTY,
						JsonElementType.OBJECT, JsonElementType.PROPERTY, JsonElementType.OBJECT));
		Assert.assertEquals(v, Arrays.asList("x"));

		/* test 2 */
		treeTestIndex.set(0);
		treeTests.clear();
		trees.clear();
		properties.clear();
		v.clear();
		typesEntry.clear();
		typesExit.clear();
		arrayPositions.clear();
		// @formatter:off
		value = new JsonBuilder()
				.object()
					.property("a").array()
						.object()
							.property("c").object()
								.property("d").number(1)
							.end()
						.end()
					.end()
				.end()
				.toJson();
		// @formatter:on
		treeTests.addAll(Arrays.asList("", "a", "a", "a.@0", "a.@0.c", "a.@0.c", "a.@0.c.d", "a.@0.c.d", "a.@0.c.d",
				"a.@0.c.d", "a.@0.c", "a.@0.c", "a.@0", "a", "a", ""));
		visitor.visit(value);
		Assert.assertEquals(trees, Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true,
				true, true, true, true));
		Assert.assertEquals(properties, Arrays.asList("d", "d", "c", "c", null, "a", "a", null));
		Assert.assertEquals(arrayPositions, Arrays.asList(-1, -1, -1, -1, 0, -1, -1, -1));
		Assert.assertEquals(typesEntry,
				Arrays.asList(JsonElementType.OBJECT, JsonElementType.PROPERTY, JsonElementType.ARRAY,
						JsonElementType.OBJECT, JsonElementType.PROPERTY, JsonElementType.OBJECT,
						JsonElementType.PROPERTY, JsonElementType.NUMBER));
		Assert.assertEquals(typesExit,
				Arrays.asList(JsonElementType.NUMBER, JsonElementType.PROPERTY, JsonElementType.OBJECT,
						JsonElementType.PROPERTY, JsonElementType.OBJECT, JsonElementType.ARRAY,
						JsonElementType.PROPERTY, JsonElementType.OBJECT));
		Assert.assertEquals(v, Arrays.asList(1));
	}

	public void testVisitorTreeObjectSkip() {
		String value;

		final AtomicInteger treeTestIndex = new AtomicInteger(0);
		final List<String> treeTests = new ArrayList<String>();
		final List<Boolean> trees = new ArrayList<Boolean>();
		final List<String> properties = new ArrayList<String>();
		final List<Object> v = new ArrayList<Object>();
		final List<JsonElementType> typesEntry = new ArrayList<JsonElementType>();
		final List<JsonElementType> typesExit = new ArrayList<JsonElementType>();
		final List<Integer> arrayPositions = new ArrayList<Integer>();

		IJsonVisitor visitor = new JsonTreeVisitor();
		visitor.setListener(new IJsonVisitorListener() {

			@Override
			public void onElementStart(IJsonElementEntry entry) {
				Collection<TreeInfo> tree = entry.getTree();
				JsonElementType type = entry.getType();

				try {
					trees.add(TreeInfo.matches(tree, treeTests.get(treeTestIndex.getAndIncrement())));
				} catch (ParseException e) {
					Assert.fail(e.getMessage());
				}

				typesEntry.add(type);

				entry.skip();
			}

			@Override
			public void onElementEnd(IJsonElementExit exit) {
				Collection<TreeInfo> tree = exit.getTree();
				JsonElementType type = exit.getType();
				JsonElement element = exit.getElement();
				String property = exit.getPropertyName();
				int pos = exit.getArrayPosition();

				try {
					trees.add(TreeInfo.matches(tree, treeTests.get(treeTestIndex.getAndIncrement())));
				} catch (ParseException e) {
					Assert.fail(e.getMessage());
				}
				typesExit.add(type);
				properties.add(property);
				arrayPositions.add(pos);

				if (element != null && element.isJsonPrimitive()) {
					switch (type) {
					case BOOLEAN:
						v.add(element.getAsBoolean());
						break;
					case NULL:
						v.add(null);
						break;
					case NUMBER:
						v.add(element.getAsNumber().intValue());
						break;
					case STRING:
						v.add(element.getAsString());
						break;
					default:
						break;
					}
				}
			}
		});

		/* test 1 */

		treeTestIndex.set(0);
		treeTests.clear();
		trees.clear();
		properties.clear();
		v.clear();
		typesEntry.clear();
		typesExit.clear();
		arrayPositions.clear();
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
		treeTests.addAll(Arrays.asList("", "a", "a", "a.b", "a.b", "a.b.c", "a.b.c", "a.b.c.d", "a.b.c.d"));
		visitor.visit(value);
		Assert.assertEquals(trees, Arrays.asList(true));
		Assert.assertEquals(properties, Arrays.asList());
		Assert.assertEquals(arrayPositions, Arrays.asList());
		Assert.assertEquals(typesEntry, Arrays.asList(JsonElementType.OBJECT));
		Assert.assertEquals(typesExit, Arrays.asList());
		Assert.assertEquals(v, Arrays.asList());

		/* test 2 */
		treeTestIndex.set(0);
		treeTests.clear();
		trees.clear();
		properties.clear();
		v.clear();
		typesEntry.clear();
		typesExit.clear();
		arrayPositions.clear();
		// @formatter:off
		value = new JsonBuilder()
				.object()
					.property("a").array()
						.object()
							.property("c").object()
								.property("d").number(1)
							.end()
						.end()
					.end()
				.end()
				.toJson();
		// @formatter:on
		treeTests.addAll(Arrays.asList("", "a", "a", "a.@0", "a.@0.c", "a.@0.c", "a.@0.c.d", "a.@0.c.d"));
		visitor.visit(value);
		Assert.assertEquals(trees, Arrays.asList(true));
		Assert.assertEquals(properties, Arrays.asList());
		Assert.assertEquals(arrayPositions, Arrays.asList());
		Assert.assertEquals(typesEntry, Arrays.asList(JsonElementType.OBJECT));
		Assert.assertEquals(typesExit, Arrays.asList());
		Assert.assertEquals(v, Arrays.asList());
	}

	public void testPath() {
		final List<String> path = new ArrayList<String>();
		String value;

		// @formatter:off
		value = new JsonBuilder()
				.object()
					.property("a").array()
						.object()
							.property("c").string("d")
						.end()
					.end()
				.end()
				.toJson();
		// @formatter:on

		IJsonVisitor visitor = new JsonTreeVisitor();
		visitor.setListener(new IJsonVisitorListener() {

			@Override
			public void onElementStart(IJsonElementEntry entry) {
				path.add(entry.getPath());
			}

			@Override
			public void onElementEnd(IJsonElementExit exit) {
				path.add(exit.getPath());
			}
		});
		visitor.visit(value);

		List<String> expectedPaths = Arrays.asList("", "a", "a", "a.@0", "a.@0.c", "a.@0.c", "a.@0.c", "a.@0.c", "a.@0",
				"a", "a", "");
		Assert.assertEquals(expectedPaths, path);
	}
	
	public void testDetach() {
		Gson gson = new GsonBuilder().serializeNulls().create();
		
		JsonTreeVisitor v = new JsonTreeVisitor();
		v.setListener(new IJsonVisitorListener() {
			
			@Override
			public void onElementStart(IJsonElementEntry entry) {
				
			}
			
			@Override
			public void onElementEnd(IJsonElementExit exit) {
				if (exit.getType() == JsonElementType.NUMBER) {
					exit.detach();
				}
			}
		});
		JsonElement e;
		
		// @formatter:off
		e = new JsonBuilder()
			.array()
				.number(1)
				.number(2)
				.number(3)
			.end()
			.toJsonTree();
		// @formatter:on
		
		Assert.assertEquals("[1,2,3]", gson.toJson(e));
		
		v.visit(e);
		
		Assert.assertEquals("[null,null,null]", gson.toJson(e));
		
		// @formatter:off
		e = new JsonBuilder()
			.object()
				.number("a", 1)
				.number("b", 1)
				.number("c", 1)
			.end()
			.toJsonTree();
		// @formatter:on
		
		Assert.assertEquals("{\"a\":1,\"b\":1,\"c\":1}", gson.toJson(e));
		
		v.visit(e);
		
		Assert.assertEquals("{\"a\":null,\"b\":null,\"c\":null}", gson.toJson(e));
		
		// @formatter:off
		e = new JsonBuilder()
			.object()
				.number("a", 1)
				.number("b", 1)
				.number("c", 1)
			.end()
			.toJsonTree();
		// @formatter:on
		
		Assert.assertEquals("{\"a\":1,\"b\":1,\"c\":1}", gson.toJson(e));
		
		v.setListener(new IJsonVisitorListener() {
			
			@Override
			public void onElementStart(IJsonElementEntry entry) {
				
			}
			
			@Override
			public void onElementEnd(IJsonElementExit exit) {
				if (exit.getType() == JsonElementType.PROPERTY) {
					exit.detach();
				}
			}
		});
		v.visit(e);
		
		Assert.assertEquals("{}", gson.toJson(e));
	}
}
