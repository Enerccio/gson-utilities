package com.github.enerccio.gson.builders;

import com.github.enerccio.gson.builders.functional.IArrayBuilder;
import com.github.enerccio.gson.builders.functional.IObjectBuilder;
import com.google.gson.JsonElement;

class BaseBuilder extends PreviousLevelAccessor implements ValueBuilder {

	/**
	 * previous builder in hierarchy
	 */
	protected BaseBuilder prevLevel;

	BaseBuilder(BaseBuilder valueBuilder) {
		super(valueBuilder);
		prevLevel = valueBuilder;
	}

	@Override
	public ValueBuilder end() {
		if (prevLevel instanceof ObjectPropertyBuilder)
			return prevLevel.end();
		return prevLevel;
	}

	@Override
	public ValueBuilder object() {
		throw new IllegalStateException();
	}

	@Override
	public ValueBuilder array() {
		throw new IllegalStateException();
	}

	@Override
	public ValueBuilder property(String name) {
		throw new IllegalStateException();
	}

	@Override
	public ValueBuilder string(String value) {
		throw new IllegalStateException();
	}

	@Override
	public ValueBuilder number(Number number) {
		throw new IllegalStateException();
	}

	@Override
	public ValueBuilder bool(Boolean bool) {
		throw new IllegalStateException();
	}

	@Override
	public ValueBuilder character(Character character) {
		throw new IllegalStateException();
	}

	@Override
	public String toJson() {
		return prevLevel.toJson();
	}

	@Override
	public JsonElement toJsonTree() {
		return prevLevel.toJsonTree();
	}

	@Override
	public ValueBuilder nil() {
		throw new IllegalStateException();
	}

	@Override
	public ValueBuilder object(IObjectBuilder builder) {
		throw new IllegalStateException();
	}

	@Override
	public ValueBuilder array(IArrayBuilder builder) {
		throw new IllegalStateException();
	}

	@Override
	public ValueBuilder element(JsonElement element) {
		throw new IllegalStateException();
	}

	@Override
	public ValueBuilder string(String property, String value) {
		return property(property).string(value);
	}

	@Override
	public ValueBuilder number(String property, Number value) {
		return property(property).number(value);
	}

	@Override
	public ValueBuilder bool(String property, Boolean value) {
		return property(property).bool(value);
	}

	@Override
	public ValueBuilder character(String property, Character value) {
		return property(property).character(value);
	}

	@Override
	public ValueBuilder nil(String property) {
		return property(property).nil();
	}

	@Override
	public ValueBuilder element(String property, JsonElement element) {
		return property(property).element(element);
	}

	@Override
	public ValueBuilder object(String property, IObjectBuilder builder) {
		return property(property).object(builder);
	}

	@Override
	public ValueBuilder array(String property, IArrayBuilder builder) {
		return property(property).array(builder);
	}
}
