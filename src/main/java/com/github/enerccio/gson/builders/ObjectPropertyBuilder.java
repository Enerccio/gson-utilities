package com.github.enerccio.gson.builders;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

class ObjectPropertyBuilder extends BaseBuilder {

	private String currentName;

	ObjectPropertyBuilder(BaseBuilder valueBuilder, String name) {
		super(valueBuilder);
		this.currentName = name;
	}

	@Override
	public void setCurrentLevelElement(JsonElement e) {
		((JsonObject) prevLevel.getCurrentLevelElement()).add(currentName, e);
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
		return prevLevel;
	}

	public ValueBuilder number(Number value) {
		setCurrentLevelElement(new JsonPrimitive(value));
		return prevLevel;
	}

	public ValueBuilder bool(Boolean value) {
		setCurrentLevelElement(new JsonPrimitive(value));
		return prevLevel;
	}

	public ValueBuilder character(Character value) {
		setCurrentLevelElement(new JsonPrimitive(value));
		return prevLevel;
	}

	public ValueBuilder nil() {
		setCurrentLevelElement(JsonNull.INSTANCE);
		return prevLevel;
	}

	@Override
	public String toJson() {
		throw new IllegalStateException();
	}

	@Override
	public JsonElement toJsonTree() {
		throw new IllegalStateException();
	}

}