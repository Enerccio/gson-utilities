package com.github.enerccio.gson.builders;

import com.google.gson.JsonElement;

class BaseBuilder implements ValueBuilder {

	/**
	 * previous builder in hierarchy
	 */
	protected BaseBuilder prevLevel;

	BaseBuilder(BaseBuilder valueBuilder) {
		prevLevel = valueBuilder;
	}

	public ValueBuilder end() {
		if (prevLevel instanceof ObjectPropertyBuilder)
			return prevLevel.end();
		return prevLevel;
	}

	/**
	 * Sets the json element of this level to e.
	 * @param e
	 * @throws {@link IllegalStateException} when this builder does not support setting elements
	 */
	protected void setCurrentLevelElement(JsonElement e) {
		throw new IllegalStateException();
	}

	/**
	 * Returns element that belongs to this builder.
	 * @return element
	 * @throws {@link IllegalStateException} when this builder does not have any element
	 */
	protected JsonElement getCurrentLevelElement() {
		throw new IllegalStateException();
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
}
