package com.github.enerccio.gson.visitors;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;

import com.github.enerccio.gson.JsonHelper;
import com.github.enerccio.gson.tree.TreeInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Json Tree Visitor.
 * 
 * This visitor first deserializes the string fully into tree of json values and
 * then visits the fully deserialized tree.
 * 
 * @author pvan
 * @since 1.1.0
 */
public class JsonTreeVisitor extends JsonTreeVisitorBase {

	@Override
	public void visit(String json) {
		JsonElement parsed = gson.fromJson(json, JsonElement.class);
		visit(parsed);
	}

	/**
	 * Visit the json element provided, calling previously set listener in the
	 * process
	 * 
	 * @param tree
	 *            tree to visit
	 * @since 1.1.0
	 */
	public void visit(JsonElement element) {
		String previousPropery = lastProperty;
		int previousArrayPosition = lastArrayPosition;
		JsonElementType previousType = lastType;
		Collection<TreeInfo> previousTree = lastTree;
		JsonElement previousElement = lastElement;

		try {
			if (element == null || element.isJsonNull()) {
				lastType = JsonElementType.NULL;
				lastTree = getVisitorStack();
				lastElement = JsonNull.INSTANCE;
				lastSkip = false;

				listener.onElementStart(enter);

				if (lastSkip) {
					return;
				}

				listener.onElementEnd(exit);
			} else if (element.isJsonObject()) {
				JsonObject o = element.getAsJsonObject();
				lastType = JsonElementType.OBJECT;
				lastTree = getVisitorStack();
				lastElement = o;
				lastSkip = false;

				listener.onElementStart(enter);

				if (lastSkip) {
					return;
				}

				if (!o.entrySet().isEmpty())
					for (Entry<String, JsonElement> entry : new HashSet<Entry<String, JsonElement>>(o.entrySet())) {
						visitProperty(entry);
						if (detach) {
							detach = false;
							o.entrySet().remove(entry);
						}
					}

				listener.onElementEnd(exit);
			} else if (element.isJsonArray()) {
				JsonArray a = element.getAsJsonArray();
				lastType = JsonElementType.ARRAY;
				lastTree = getVisitorStack();
				lastElement = a;
				lastSkip = false;

				listener.onElementStart(enter);

				if (lastSkip) {
					return;
				}
				
				if (a.size() != 0) {
					for (int i = 0; i < a.size(); i++) {
						lastArrayPosition = i;
						lastProperty = null;
						enter(i);
						visit(a.get(i));
						if (detach) {
							detach = false;
							JsonHelper.arraySet(a, i, JsonNull.INSTANCE);
						}
						exit();
						lastProperty = previousPropery;
						lastArrayPosition = previousArrayPosition;
					}
				}

				listener.onElementEnd(exit);
			} else {
				JsonPrimitive p = element.getAsJsonPrimitive();
				lastTree = getVisitorStack();
				lastElement = p;
				lastSkip = false;

				if (p.isBoolean()) {
					lastType = JsonElementType.BOOLEAN;
				} else if (p.isNumber()) {
					lastType = JsonElementType.NUMBER;
				} else {
					lastType = JsonElementType.STRING;
				}

				listener.onElementStart(enter);

				if (lastSkip) {
					return;
				}

				listener.onElementEnd(exit);
			}
		} finally {
			lastType = previousType;
			lastTree = previousTree;
			lastProperty = previousPropery;
			lastElement = previousElement;
			lastArrayPosition = previousArrayPosition;
		}
	}

	private void visitProperty(Entry<String, JsonElement> entry) {
		String previousPropery = lastProperty;
		int previousArrayPosition = lastArrayPosition;
		JsonElementType previousType = lastType;
		Collection<TreeInfo> previousTree = lastTree;
		JsonElement previousElement = lastElement;

		try {
			JsonElement e = entry.getValue();
			enter(entry.getKey());
			lastType = JsonElementType.PROPERTY;
			lastSkip = false;
			lastProperty = entry.getKey();
			lastArrayPosition = -1;
			lastTree = getVisitorStack();
			JsonObject wrap = new JsonObject();
			wrap.add(lastProperty, e);
			lastElement = e;
			listener.onElementStart(enter);
			if (lastSkip) {
				exit();
				return;
			}
			visit(e);
			if (detach) {
				detach = false;
				entry.setValue(JsonNull.INSTANCE);
			}
			listener.onElementEnd(exit);
			exit();
		} finally {
			lastType = previousType;
			lastTree = previousTree;
			lastProperty = previousPropery;
			lastElement = previousElement;
			lastArrayPosition = previousArrayPosition;
		}
	}

	private boolean lastSkip = false;
	private JsonElementType lastType = null;
	private Collection<TreeInfo> lastTree = null;
	private String lastProperty = null;
	private JsonElement lastElement = null;
	private int lastArrayPosition = -1;
	private boolean detach = false;

	// shared so they are bound to the visitor and cannot be stored/reused!
	private IJsonElementEntry enter = new IJsonElementEntry() {

		@Override
		public JsonElementType getType() {
			return lastType;
		}

		@Override
		public Collection<TreeInfo> getTree() {
			return lastTree;
		}

		@Override
		public void skip() {
			lastSkip = true;
		}

		@Override
		public String getPath() {
			return TreeInfo.toPath(getTree());
		}
	};

	private IJsonElementExit exit = new IJsonElementExit() {

		@Override
		public JsonElementType getType() {
			return lastType;
		}

		@Override
		public Collection<TreeInfo> getTree() {
			return lastTree;
		}

		@Override
		public String getPropertyName() {
			return lastProperty;
		}

		@Override
		public JsonElement getElement() {
			return lastElement;
		}

		@Override
		public <X> X getAsObject(Type type) {
			return gson.fromJson(getElement(), type);
		}

		@Override
		public <X> X getAsObject(Class<X> clazz) {
			return gson.fromJson(getElement(), clazz);
		}

		@Override
		public int getArrayPosition() {
			return lastArrayPosition;
		}

		@Override
		public String getPath() {
			return TreeInfo.toPath(getTree());
		}

		@Override
		public JsonElement detach() {
			// detach is kinda useless here but can be done when used with JsonElement and not String
			// there is no memory benefit as it is with detaching from streaming version though
			detach = true;
			if (getType() == JsonElementType.PROPERTY) {
				return null;
			}
			return getElement();
		}
	};
}
