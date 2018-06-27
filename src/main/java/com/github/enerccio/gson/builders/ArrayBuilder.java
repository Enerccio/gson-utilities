package com.github.enerccio.gson.builders;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

class ArrayBuilder extends BaseBuilder {

	private JsonArray currentArray;

	ArrayBuilder(BaseBuilder valueBuilder) {
		super(valueBuilder);
		prevLevel.setCurrentLevelElement(currentArray = new JsonArray());
	}

	@Override
	public JsonElement getCurrentLevelElement() {
		return currentArray;
	}

	@Override
	public void setCurrentLevelElement(JsonElement e) {
		currentArray.add(e);
	}

	@Override
	public ObjectBuilder object() {
		return new ObjectBuilder(this);
	}

	@Override
	public ArrayBuilder array() {
		return new ArrayBuilder(this);
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
}
