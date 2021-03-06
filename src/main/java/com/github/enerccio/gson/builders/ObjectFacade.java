package com.github.enerccio.gson.builders;

import com.github.enerccio.gson.builders.functional.IArrayBuilder;
import com.github.enerccio.gson.builders.functional.IObjectBuilder;
import com.github.enerccio.gson.builders.functional.IObjectFacade;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

class ObjectFacade extends PreviousLevelAccessor implements IObjectFacade {

	private JsonObject object;

	ObjectFacade(PreviousLevelAccessor prevLevelAccessor) {
		super(prevLevelAccessor);
		this.object = new JsonObject();
		prevLevelAccessor.setCurrentLevelElement(object);
	}

	ObjectFacade(PreviousLevelAccessor prevLevelAccessor, String property) {
		super(prevLevelAccessor);
		this.object = new JsonObject();
		prevLevelAccessor.setCurrentLevelElement(object, property);
	}

	@Override
	protected void setCurrentLevelElement(JsonElement e, String property) {
		this.object.add(property, e);
	}

	@Override
	public void put(String property, String string) {
		this.object.add(property, new JsonPrimitive(string));
	}

	@Override
	public void put(String property, Number number) {
		this.object.add(property, new JsonPrimitive(number));
	}

	@Override
	public void put(String property, Boolean bool) {
		this.object.add(property, new JsonPrimitive(bool));
	}

	@Override
	public void putNil(String property) {
		this.object.add(property, JsonNull.INSTANCE);
	}

	@Override
	public void putArray(String property, IArrayBuilder arrayBuilder) {
		arrayBuilder.build(new ArrayFacade(this, property));
	}

	@Override
	public void putObject(String property, IObjectBuilder objectBuilder) {
		objectBuilder.build(new ObjectFacade(this, property));
	}

	@Override
	public void put(String property, JsonElement element) {
		this.object.add(property, element);
	}

}
