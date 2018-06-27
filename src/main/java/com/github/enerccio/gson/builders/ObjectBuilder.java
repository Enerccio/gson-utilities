package com.github.enerccio.gson.builders;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

class ObjectBuilder extends BaseBuilder {

	private JsonObject currentObject;

	ObjectBuilder(BaseBuilder valueBuilder) {
		super(valueBuilder);
		prevLevel.setCurrentLevelElement(currentObject = new JsonObject());
	}

	@Override
	public JsonElement getCurrentLevelElement() {
		return currentObject;
	}

	@Override
	public ValueBuilder property(String name) {
		return new ObjectPropertyBuilder(this, name);
	}

}
