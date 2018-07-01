package com.github.enerccio.gson.visitors;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.github.enerccio.gson.tree.TreeInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

/**
 * Json Streaming Visitor
 * <p>
 * Streamingly iterate over JSON string.
 * <p>
 * This class is mostly thread safe, the only unsafe component is listener setting.
 * If you need this to be thread safe, use {@link #visit(String, IJsonVisitorListener)} instead.
 * @author pvan
 * @since 1.1.0
 */
public class JsonStreamVisitor implements IJsonVisitor {
	
	private static class StreamingElementCallbackBase {
		
		private LinkedList<CurrentJsonElement> stack;
		private int i;
		
		public StreamingElementCallbackBase(LinkedList<CurrentJsonElement> stack) {
			this.stack = stack;
		}
		
		public void setCurrent(int i) {
			this.i = i;
		}
		
		protected CurrentJsonElement c() {
			return stack.get(i);
		}
		
	}

	private static class StreamingElementEntry extends StreamingElementCallbackBase implements IJsonElementEntry {

		public StreamingElementEntry(LinkedList<CurrentJsonElement> stack) {
			super(stack);
		}

		@Override
		public JsonElementType getType() {
			return c().type;
		}

		@Override
		public Collection<TreeInfo> getTree() {
			return c().treeRO;
		}

		@Override
		public String getPath() {
			return TreeInfo.toPath(getTree());
		}

		@Override
		public void skip() {
			c().skip = true;
		}
		
	}
	
	private static class StreamingElementExit extends StreamingElementCallbackBase implements IJsonElementExit {

		public StreamingElementExit(LinkedList<CurrentJsonElement> stack) {
			super(stack);
		}

		@Override
		public JsonElementType getType() {
			return c().type;
		}

		@Override
		public Collection<TreeInfo> getTree() {
			return c().treeRO;
		}

		@Override
		public String getPath() {
			return TreeInfo.toPath(getTree());
		}

		@Override
		public JsonElement getElement() {
			return c().builtElement;
		}

		@Override
		public String getPropertyName() {
			return c().parentProperty;
		}

		@Override
		public int getArrayPosition() {
			return c().parentArrayPos;
		}

		@Override
		public <X> X getAsObject(Class<X> clazz) {
			throw new UnsupportedOperationException("can't deserialize without gson present in thread-safe context");
		}

		@Override
		public <X> X getAsObject(Type type) {
			throw new UnsupportedOperationException("can't deserialize without gson present in thread-safe context");
		}

		@Override
		public JsonElement detach() {
			c().detach = true;
			return c().builtElement;
		}
		
	}
	
	private static class CurrentJsonElement {
		private boolean detach;
		private boolean skip;
		
		private JsonElementType type;
		private List<TreeInfo> tree;
		private Collection<TreeInfo> treeRO;
		
		private String parentProperty;
		private int parentArrayPos = -1;
		private JsonElement builtElement;
		
		private String selfProperty;

		@Override
		public String toString() {
			return "" + type;
		}		
	}
	
	private IJsonVisitorListener listener = JsonTreeVisitorBase.EMPTY_VISITOR;
	
	@Override
	public void visit(String json) {
		visit(json, listener);
	}
	
	/**
	 * Internal alias to visit that returns built element.
	 * <p>
	 * Practically only useful when testing or when visiting string but wanting to capture it to
	 * JsonElement as well.
	 * @param json
	 * @return built JsonElement
	 */
	public JsonElement visitReturn(String json) {
		return visit(json, listener);
	}
	
	/**
	 * Visits this JSON string with this listener.
	 * <p>
	 * This method is thread safe.
	 * @param json JSON string to visit
	 * @param listener to fire events
	 * @return optionally last built item
	 * @since 1.1.0
	 */
	public JsonElement visit(String json, IJsonVisitorListener listener) {
		try {
			return doVisit(json, listener);
		} catch (Exception e) {
			throw new IllegalArgumentException("malformed JSON", e);
		}
	}
	
	private JsonElement doVisit(String json, IJsonVisitorListener listener) throws IOException {
		StringReader r = new StringReader(json);
		@SuppressWarnings("resource")
		JsonReader jsr = new JsonReader(r);
		
		LinkedList<CurrentJsonElement> stack = new LinkedList<CurrentJsonElement>();
		
		StreamingElementEntry enter = new StreamingElementEntry(stack);
		StreamingElementExit exit = new StreamingElementExit(stack);
		CurrentJsonElement lastPoppedElement = null; 
		
		try {
			while (true) {
				JsonToken t = jsr.peek();
				
				CurrentJsonElement current = stack.isEmpty() ? null : stack.getLast();
				
				switch (t) {
				case END_DOCUMENT:
					return byStack(stack, lastPoppedElement);
					
				case BEGIN_OBJECT:
				case BEGIN_ARRAY: {
					if (current != null && !(current.type == JsonElementType.PROPERTY || 
							current.type == JsonElementType.ARRAY)) {
						throw new IllegalArgumentException("malformed json string input");
					}
					CurrentJsonElement ne = new CurrentJsonElement();
					ne.type = t == JsonToken.BEGIN_ARRAY ? JsonElementType.ARRAY : JsonElementType.OBJECT;
					processTree(current, ne);
					ne.builtElement = t ==  JsonToken.BEGIN_ARRAY ? new JsonArray() : new JsonObject();
					stack.addLast(ne);
					if (current == null || !current.skip) {
						enter.setCurrent(stack.size() - 1);
						listener.onElementStart(enter);
					}
					if (ne.skip == false && current != null) {
						ne.skip = current.skip;
					}					
					
					if (t == JsonToken.BEGIN_ARRAY) 
						jsr.beginArray();
					else
						jsr.beginObject();
				} break;			

				case NULL:
				case NUMBER:
				case STRING:
				case BOOLEAN: {
					if (current != null && current.type == JsonElementType.OBJECT) {
						throw new IllegalArgumentException("malformed json string input");
					}
					CurrentJsonElement ne = new CurrentJsonElement();
					switch (t) {
					case NULL:
						ne.type = JsonElementType.NULL;
						break;
					case NUMBER:
						ne.type = JsonElementType.NUMBER;
						break;
					case STRING:
						ne.type = JsonElementType.STRING;
						break;
					case BOOLEAN:
						ne.type = JsonElementType.BOOLEAN;
						break;
					default:
						break;
					}
					processTree(current, ne);
					
					stack.addLast(ne);
					
					if (current == null || !current.skip) {
						enter.setCurrent(stack.size() - 1);
						listener.onElementStart(enter);
					}
					if (ne.skip == false && current != null) {
						ne.skip = current.skip;
					}
					
					switch (t) {
					case NULL:
						jsr.nextNull();
						ne.builtElement = JsonNull.INSTANCE;
						break;
					case NUMBER:
						ne.builtElement = new JsonPrimitive(jsr.nextDouble());
						break;
					case STRING:
						ne.builtElement = new JsonPrimitive(jsr.nextString());
						break;
					case BOOLEAN:
						ne.builtElement = new JsonPrimitive(jsr.nextBoolean());
						break;
					default:
						break;
					}
					
					if (!ne.skip) {
						exit.setCurrent(stack.size() - 1);
						listener.onElementEnd(exit);
					}
					
					lastPoppedElement = stack.removeLast();
				} break;

				case NAME: {
					if (current == null || current.type != JsonElementType.OBJECT) {
						throw new IllegalArgumentException("malformed json string input");
					}
					
					CurrentJsonElement ne = new CurrentJsonElement();
					ne.type = JsonElementType.PROPERTY;
					processTree(current, ne);
					ne.selfProperty = jsr.nextName();
					ne.parentProperty = ne.selfProperty;
					ne.tree.add(new TreeInfo(ne.selfProperty));
					
					stack.addLast(ne);
					
					if (!current.skip) {
						enter.setCurrent(stack.size() - 1);
						listener.onElementStart(enter);
					}
					
					if (ne.skip == false && current != null) {
						ne.skip = current.skip;
					}
					
				} break;
				
				case END_ARRAY:		
				case END_OBJECT: {
					if (t == JsonToken.END_ARRAY) 
						jsr.endArray();
					else
						jsr.endObject();
					
					if (!current.skip) {
						exit.setCurrent(stack.size() - 1);
						listener.onElementEnd(exit);
					}
					
					lastPoppedElement = stack.removeLast();
					current = stack.size() == 0 ? null : stack.getLast();
				} break;				
				}
				
				if (current != null && current.type == JsonElementType.ARRAY) {
					switch (t) {						
					case END_ARRAY:
					case END_OBJECT:
					case NULL:
					case NUMBER:
					case STRING:
					case BOOLEAN:
						if (lastPoppedElement.detach) {
							((JsonArray) current.builtElement).add(JsonNull.INSTANCE);
						} else {
							((JsonArray) current.builtElement).add(lastPoppedElement.builtElement);
						}
						break;						
					default:
						break;					
					}
				}
				
				boolean propertyEnded = false;
				
				if (current != null && current.type == JsonElementType.PROPERTY) {
					switch (t) {						
					case NULL:
					case NUMBER:
					case STRING:
					case BOOLEAN:
					case END_OBJECT:
					case END_ARRAY:
						JsonElement lastBuilt;
						if (lastPoppedElement.detach) {
							lastBuilt = JsonNull.INSTANCE;
						}  else {
							lastBuilt = lastPoppedElement.builtElement;
						}
						if (!current.skip) {
							current.builtElement = new JsonObject();
							((JsonObject) current.builtElement).add(current.selfProperty, lastBuilt);
							exit.setCurrent(stack.size() - 1);
							listener.onElementEnd(exit);
						}
						current.builtElement = lastBuilt;
						lastPoppedElement = stack.removeLast();
						current = stack.getLast();
						propertyEnded = true;
						break;						
					default:
						break;					
					}
					
				}
				
				if (current != null && current.type == JsonElementType.OBJECT && propertyEnded) {
					if (!lastPoppedElement.detach) {
						((JsonObject) current.builtElement).add(lastPoppedElement.selfProperty,
								lastPoppedElement.builtElement);
					}
				}
			}
		} catch (IOException e) {
			throw e;
		}
	}

	private JsonElement byStack(LinkedList<CurrentJsonElement> stack, CurrentJsonElement lastPoppedElement) {
		if (!stack.isEmpty()) {
			if (stack.size() == 1) {
				if (stack.getFirst().type != JsonElementType.ARRAY && 
						stack.getFirst().type != JsonElementType.OBJECT) {
					return stack.getFirst().builtElement;
				}
			}
			throw new IllegalArgumentException("malformed JSON");
		}
		return lastPoppedElement.builtElement;
	}

	private void processTree(CurrentJsonElement current, CurrentJsonElement ne) {
		if (current != null) {
			ne.tree = new ArrayList<TreeInfo>(current.tree);
			if (current.type == JsonElementType.PROPERTY) {
				ne.parentProperty = current.selfProperty;
			} else if (current.type == JsonElementType.ARRAY){
				ne.parentArrayPos = ((JsonArray) current.builtElement).size();
				ne.tree.add(new TreeInfo(ne.parentArrayPos));
			}						
		} else {
			ne.tree = new ArrayList<TreeInfo>();
		}
		ne.treeRO = Collections.unmodifiableCollection(ne.tree);
	}

	@Override
	public IJsonVisitor setListener(IJsonVisitorListener listener) {
		this.listener = listener;
		return this;
	}

}
