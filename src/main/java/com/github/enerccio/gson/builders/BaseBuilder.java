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

	public ValueBuilder end() {
		if (prevLevel instanceof ObjectPropertyBuilder)
			return prevLevel.end();
		return prevLevel;
	}

	public ValueBuilder object() {
		throw new IllegalStateException();
	}

	public ValueBuilder array() {
		throw new IllegalStateException();
	}

	public ValueBuilder property(String name) {
		throw new IllegalStateException();
	}

	public ValueBuilder string(String value) {
		throw new IllegalStateException();
	}

	public ValueBuilder number(Number number) {
		throw new IllegalStateException();
	}

	public ValueBuilder bool(Boolean bool) {
		throw new IllegalStateException();
	}

	public ValueBuilder character(Character character) {
		throw new IllegalStateException();
	}

	public String toJson() {
		return prevLevel.toJson();
	}

	public JsonElement toJsonTree() {
		return prevLevel.toJsonTree();
	}

	public ValueBuilder nil() {
		throw new IllegalStateException();
	}

	public ValueBuilder object(IObjectBuilder builder) {
		throw new IllegalStateException();
	}

	public ValueBuilder array(IArrayBuilder builder) {
		throw new IllegalStateException();
	}
}
