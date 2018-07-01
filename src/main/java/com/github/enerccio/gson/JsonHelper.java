package com.github.enerccio.gson;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.github.enerccio.gson.tree.JsonPatternMatcher;
import com.github.enerccio.gson.visitors.IJsonElementEntry;
import com.github.enerccio.gson.visitors.IJsonElementExit;
import com.github.enerccio.gson.visitors.IJsonVisitorListener;
import com.github.enerccio.gson.visitors.JsonElementType;
import com.github.enerccio.gson.visitors.JsonTreeVisitor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * Various helper static methods.
 * 
 * @author pvan
 * @since 1.1.0
 */
public class JsonHelper {

	/**
	 * Test predicate.
	 * 
	 * You can use Predicate JsonElement instead if you are using java 8+
	 * 
	 * @author pvan
	 * @since 1.1.0
	 */
	public interface IJsonPredicate {

		/**
		 * Test whether JsonElement matches or not
		 * 
		 * @param e
		 *            element to test
		 * @return true if matches/false
		 * @since 1.1.0
		 */
		boolean test(JsonElement e);

	}

	private static IJsonPredicate ACCEPT_ALL_PREDICATE = new IJsonPredicate() {

		@Override
		public boolean test(JsonElement e) {
			return true;
		}
	};

	/**
	 * Returns first element that matches the patter in the JSON string or null
	 * if no matches
	 * 
	 * @param json
	 *            string to search
	 * @param pattern
	 *            pattern to match
	 * @return element or null
	 * @throws ParseException
	 *             if pattern is malformed
	 * @since 1.1.0
	 */
	public static JsonElement getFirst(String json, String pattern) throws ParseException {
		return getFirst(json, pattern, ACCEPT_ALL_PREDICATE);
	}

	/**
	 * Returns first element that matches the patter in the JSON string or null
	 * if no matches
	 * 
	 * @param json
	 *            string to search
	 * @param matcher
	 *            matcher to match
	 * @return element or null
	 * @since 1.1.0
	 */
	public static JsonElement getFirst(String json, JsonPatternMatcher matcher) {
		return getFirst(GsonProvider.sharedGson.fromJson(json, JsonElement.class), matcher, ACCEPT_ALL_PREDICATE);
	}

	/**
	 * Returns first element that matches the patter in the JSON string and test
	 * or null if no matches
	 * 
	 * @param json
	 *            string to search
	 * @param pattern
	 *            pattern to match
	 * @param test
	 *            predicate to test
	 * @return element or null
	 * @throws ParseException
	 *             if pattern is malformed
	 * @since 1.1.0
	 */
	public static JsonElement getFirst(String json, String pattern, IJsonPredicate test) throws ParseException {
		return getFirst(GsonProvider.sharedGson.fromJson(json, JsonElement.class), pattern, test);
	}

	/**
	 * Returns first element that matches the pattern in the json element or
	 * null if no matches
	 * 
	 * @param root
	 *            element to search
	 * @param pattern
	 *            pattern to match
	 * @return element or null
	 * @throws ParseException
	 *             if pattern is malformed
	 * @since 1.1.0
	 */
	public static JsonElement getFirst(JsonElement root, String pattern) throws ParseException {
		return getFirst(root, pattern, ACCEPT_ALL_PREDICATE);
	}

	/**
	 * Returns first element that matches the pattern in the json element and
	 * passes the test or null if no matches
	 * 
	 * @param root
	 *            element to search
	 * @param pattern
	 *            pattern to match
	 * @param test
	 *            predicate to test
	 * @return element or null
	 * @throws ParseException
	 *             if pattern is malformed
	 * @since 1.1.0
	 */
	public static JsonElement getFirst(JsonElement root, String pattern, IJsonPredicate test) throws ParseException {
		JsonPatternMatcher matcher = new JsonPatternMatcher(pattern);
		return getFirst(root, matcher, test);
	}

	/**
	 * Returns first element that matches the matcher in the json element and
	 * passes the test or null if no matches
	 * 
	 * @param root
	 *            element to test
	 * @param matcher
	 *            tree matcher to test
	 * @param test
	 *            predicate to test
	 * @return element or null
	 * @since 1.1.0
	 */
	public static JsonElement getFirst(JsonElement root, final JsonPatternMatcher matcher, final IJsonPredicate test) {
		final AtomicReference<JsonElement> target = new AtomicReference<JsonElement>(null);

		JsonTreeVisitor visitor = new JsonTreeVisitor();
		visitor.setListener(new IJsonVisitorListener() {

			@Override
			public void onElementStart(IJsonElementEntry entry) {
				if (target.get() != null)
					entry.skip();
			}

			@Override
			public void onElementEnd(IJsonElementExit exit) {
				if (target.get() != null)
					return;

				if (exit.getType() != JsonElementType.PROPERTY && matcher.matches(exit.getTree())) {
					if (test.test(exit.getElement()))
						target.set(exit.getElement());
				}
			}
		});
		visitor.visit(root);

		return target.get();
	}

	/**
	 * Returns all elements that matches the patter in the JSON string
	 * <p>
	 * Returns max {@link Integer.MAX_VALUE} elements.
	 * 
	 * @param json
	 *            string to search
	 * @param pattern
	 *            pattern to match
	 * @return elements that match
	 * @throws ParseException
	 *             if pattern is malformed
	 * @since 1.1.0
	 */
	public static List<JsonElement> getAll(String json, String pattern) throws ParseException {
		return getAll(json, pattern, ACCEPT_ALL_PREDICATE);
	}

	/**
	 * Returns all elements that matches the matcher in the JSON string
	 * <p>
	 * Returns max {@link Integer.MAX_VALUE} elements.
	 * 
	 * @param json
	 *            string to search
	 * @param matcher
	 *            matcher to match
	 * @return elements that match
	 * @since 1.1.0
	 */
	public static List<JsonElement> getAll(String json, JsonPatternMatcher matcher) {
		return getAll(GsonProvider.sharedGson.fromJson(json, JsonElement.class), matcher, ACCEPT_ALL_PREDICATE);
	}

	/**
	 * Returns all elements that matches the patter in the JSON string and
	 * provided predicate
	 * <p>
	 * Returns max {@link Integer.MAX_VALUE} elements.
	 * 
	 * @param json
	 *            string to search
	 * @param pattern
	 *            pattern to match
	 * @param test
	 *            predicate to test
	 * @return elements that match
	 * @throws ParseException
	 *             if pattern is malformed
	 * @since 1.1.0
	 */
	public static List<JsonElement> getAll(String json, String pattern, IJsonPredicate test) throws ParseException {
		return getAll(GsonProvider.sharedGson.fromJson(json, JsonElement.class), pattern, test);
	}

	/**
	 * Returns all elements that matches the patter in the json element
	 * <p>
	 * Returns max {@link Integer.MAX_VALUE} elements.
	 * 
	 * @param root
	 *            element to search
	 * @param pattern
	 *            pattern to match
	 * @return elements that match
	 * @throws ParseException
	 *             if pattern is malformed
	 * @since 1.1.0
	 */
	public static List<JsonElement> getAll(JsonElement root, String pattern) throws ParseException {
		return getAll(root, pattern, ACCEPT_ALL_PREDICATE);
	}

	/**
	 * Returns all elements that matches the matcher in the json element
	 * <p>
	 * Returns max {@link Integer.MAX_VALUE} elements.
	 * 
	 * @param root
	 *            element to search
	 * @param matcher
	 *            matcher to match
	 * @return elements that match
	 * @since 1.1.0
	 */
	public static List<JsonElement> getAll(JsonElement root, JsonPatternMatcher matcher) {
		return getAll(root, matcher, ACCEPT_ALL_PREDICATE);
	}

	/**
	 * Returns all elements that matches the patter in the json element and
	 * passes test on predicate
	 * 
	 * @param root
	 *            element to search
	 * @param pattern
	 *            pattern to match
	 * @param test
	 *            test
	 * @return elements that match
	 * @throws ParseException
	 *             if pattern is malformed
	 * @since 1.1.0
	 */
	public static List<JsonElement> getAll(JsonElement root, String pattern, IJsonPredicate test)
			throws ParseException {
		JsonPatternMatcher matcher = new JsonPatternMatcher(pattern);
		return getAll(root, matcher, test);
	}

	/**
	 * Returns all elements that matches the matcher in the json element and
	 * passes test on predicate
	 * <p>
	 * Returns max {@link Integer.MAX_VALUE} elements.
	 * 
	 * @param root
	 *            element to search
	 * @param match
	 *            matcher to match
	 * @param test
	 *            test
	 * @return elements that match
	 * @since 1.1.0
	 */
	public static List<JsonElement> getAll(JsonElement root, JsonPatternMatcher matcher, IJsonPredicate test) {
		return getN(root, matcher, test, Integer.MAX_VALUE);
	}

	/**
	 * Returns up to n elements that matches the patter in the JSON string
	 * <p>
	 * Returns max n elements.
	 * 
	 * @param json
	 *            string to search
	 * @param pattern
	 *            pattern to match
	 * @param n
	 *            number of returned elements
	 * @return elements that match
	 * @throws ParseException
	 *             if pattern is malformed
	 * @since 1.1.0
	 */
	public static List<JsonElement> getN(String json, String pattern, int n) throws ParseException {
		return getN(json, pattern, ACCEPT_ALL_PREDICATE, n);
	}

	/**
	 * Returns up to n elements that matches the matcher in the JSON string
	 * <p>
	 * Returns max n elements.
	 * 
	 * @param json
	 *            string to search
	 * @param matcher
	 *            matcher to match
	 * @param n
	 *            number of returned elements
	 * @return elements that match
	 * @since 1.1.0
	 */
	public static List<JsonElement> getN(String json, JsonPatternMatcher matcher, int n) {
		return getN(GsonProvider.sharedGson.fromJson(json, JsonElement.class), matcher, ACCEPT_ALL_PREDICATE, n);
	}

	/**
	 * Returns up to n elements that matches the patter in the JSON string and
	 * provided predicate
	 * <p>
	 * Returns max n elements.
	 * 
	 * @param json
	 *            string to search
	 * @param pattern
	 *            pattern to match
	 * @param test
	 *            predicate to test
	 * @param n
	 *            number of returned elements
	 * @return elements that match
	 * @throws ParseException
	 *             if pattern is malformed
	 * @since 1.1.0
	 */
	public static List<JsonElement> getN(String json, String pattern, IJsonPredicate test, int n)
			throws ParseException {
		return getN(GsonProvider.sharedGson.fromJson(json, JsonElement.class), pattern, test, n);
	}

	/**
	 * Returns up to n elements that matches the patter in the json element
	 * <p>
	 * Returns max n elements.
	 * 
	 * @param root
	 *            element to search
	 * @param pattern
	 *            pattern to match
	 * @param n
	 *            number of returned elements
	 * @return elements that match
	 * @throws ParseException
	 *             if pattern is malformed
	 * @since 1.1.0
	 */
	public static List<JsonElement> getN(JsonElement root, String pattern, int n) throws ParseException {
		return getN(root, pattern, ACCEPT_ALL_PREDICATE, n);
	}

	/**
	 * Returns up to n elements that matches the matcher in the json element
	 * <p>
	 * Returns max n elements.
	 * 
	 * @param root
	 *            element to search
	 * @param matcher
	 *            matcher to match
	 * @param n
	 *            number of returned elements
	 * @return elements that match
	 * @since 1.1.0
	 */
	public static List<JsonElement> getN(JsonElement root, JsonPatternMatcher matcher, int n) {
		return getN(root, matcher, ACCEPT_ALL_PREDICATE, n);
	}

	/**
	 * Returns up to n elements that matches the patter in the json element and
	 * passes test on predicate
	 * <p>
	 * Returns max n elements.
	 * 
	 * @param root
	 *            element to search
	 * @param pattern
	 *            pattern to match
	 * @param test
	 *            test
	 * @param n
	 *            number of returned elements
	 * @return elements that match
	 * @throws ParseException
	 *             if pattern is malformed
	 * @since 1.1.0
	 */
	public static List<JsonElement> getN(JsonElement root, String pattern, IJsonPredicate test, int n)
			throws ParseException {
		JsonPatternMatcher matcher = new JsonPatternMatcher(pattern);
		return getN(root, matcher, test, n);
	}

	/**
	 * Returns up to n elements that matches the matcher in the json element and
	 * passes test on predicate
	 * <p>
	 * Returns max n elements.
	 * 
	 * @param root
	 *            element to search
	 * @param match
	 *            matcher to match
	 * @param test
	 *            test
	 * @param n
	 *            maximum number of returned elements
	 * @return elements that match
	 * @since 1.1.0
	 */
	public static List<JsonElement> getN(JsonElement root, final JsonPatternMatcher matcher, final IJsonPredicate test,
			final int n) {
		final List<JsonElement> targets = new ArrayList<JsonElement>();

		JsonTreeVisitor visitor = new JsonTreeVisitor();
		visitor.setListener(new IJsonVisitorListener() {

			@Override
			public void onElementStart(IJsonElementEntry entry) {
				if (targets.size() >= n) {
					entry.skip();
				}
			}

			@Override
			public void onElementEnd(IJsonElementExit exit) {
				if (targets.size() >= n) {
					return;
				}

				if (exit.getType() != JsonElementType.PROPERTY && matcher.matches(exit.getTree())) {
					if (test.test(exit.getElement()))
						targets.add(exit.getElement());
				}
			}
		});
		visitor.visit(root);

		return targets;
	}
	
	private static Field arrayElements;

	/**
	 * Sets the JsonElement to position i in JsonArray
	 * <p>
	 * Uses reflection to access inner list.
	 * 
	 * @param a JsonArray to change
	 * @param i position
	 * @param jsonValue value to set
	 * @return true if successful, false if not
	 */
	public static boolean arraySet(JsonArray a, int i, JsonElement jsonValue) {
		try {
			if (arrayElements == null) {
				synchronized (JsonHelper.class) {
					if (arrayElements == null) {
						arrayElements = JsonArray.class.getDeclaredField("elements");
						arrayElements.setAccessible(true);
					}
				}
			}
			if (arrayElements == null)
				return false;
			@SuppressWarnings("unchecked")
			List<JsonElement> elements = (List<JsonElement>) arrayElements.get(a);
			elements.set(i, jsonValue);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
