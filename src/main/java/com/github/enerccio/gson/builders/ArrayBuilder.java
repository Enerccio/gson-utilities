package com.github.enerccio.gson.builders;

import com.github.enerccio.gson.builders.functional.IArrayBuilder;
import com.github.enerccio.gson.builders.functional.IObjectBuilder;
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
	
	@Override
	public ValueBuilder object(IObjectBuilder builder) {
		builder.build(new ObjectFacade(this));
		return prevLevel;
	}

	@Override
	public ValueBuilder array(IArrayBuilder builder) {
		builder.build(new ArrayFacade(this));
		return prevLevel;
	}
}
