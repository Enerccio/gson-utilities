package com.github.enerccio.gson.builders;

import com.github.enerccio.gson.builders.functional.IArrayBuilder;
import com.github.enerccio.gson.builders.functional.IObjectBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

/**
 * Creates JsonBuilder to create JSON values via builder pattern.
 * 
 * This builder is NOT thread safe!
 * 
 * @author pvan
 *
 */
public class JsonBuilder extends BaseBuilder {

	/**
	 * Default shared gson is pretty printing gson instance
	 */
	private static final Gson sharedGson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Serializer used
	 */
	private Gson gson = sharedGson;
	/**
	 * Root element
	 */
	private JsonElement root;

	/**
	 * Default Json builder
	 */
	public JsonBuilder() {
		super(null);
	}
	
	/**
	 * Creates JsonBuilder with provided gson serializer.
	 * @param gson what gson to use
	 */
	public JsonBuilder(Gson gson) {
		super(null);
		setGson(gson);
	}

	/**
	 * Change gson serializer to this gson
	 * @param gson
	 * @return this builder
	 * @throws {@link NullPointerException} when gson instance is null
	 */
	public JsonBuilder setGson(Gson gson) {
		if (gson == null)
			throw new NullPointerException("gson can't be null");
		this.gson = gson;
		return this;
	}

	public String toJson() {
		if (root == null)
			throw new IllegalStateException();
		return gson.toJson(root);
	}

	public JsonElement toJsonTree() {
		if (root == null)
			throw new IllegalStateException();
		return root;
	}

	@Override
	protected void setCurrentLevelElement(JsonElement e) {
		root = e;
	}

	@Override
	protected JsonElement getCurrentLevelElement() {
		return root;
	}

	public ValueBuilder end() {
		throw new IllegalStateException();
	}

	public ValueBuilder object() {
		return new ObjectBuilder(this);
	}

	public ValueBuilder array() {
		return new ArrayBuilder(this);
	}

	public ValueBuilder property(String name) {
		throw new IllegalArgumentException();
	}

	public ValueBuilder string(String value) {
		setCurrentLevelElement(new JsonPrimitive(value));
		return this;
	}

	public ValueBuilder number(Number value) {
		setCurrentLevelElement(new JsonPrimitive(value));
		return this;
	}

	public ValueBuilder bool(Boolean value) {
		setCurrentLevelElement(new JsonPrimitive(value));
		return this;
	}

	public ValueBuilder character(Character value) {
		setCurrentLevelElement(new JsonPrimitive(value));
		return this;
	}

	public ValueBuilder nil() {
		setCurrentLevelElement(JsonNull.INSTANCE);
		return this;
	}

	@Override
	public ValueBuilder object(IObjectBuilder builder) {
		builder.build(new ObjectFacade(this));
		return this;
	}

	@Override
	public ValueBuilder array(IArrayBuilder builder) {
		builder.build(new ArrayFacade(this));
		return this;
	}
}
