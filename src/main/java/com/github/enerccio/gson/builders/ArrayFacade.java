package com.github.enerccio.gson.builders;

import com.github.enerccio.gson.builders.functional.IArrayBuilder;
import com.github.enerccio.gson.builders.functional.IArrayFacade;
import com.github.enerccio.gson.builders.functional.IObjectBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

class ArrayFacade extends PreviousLevelAccessor implements IArrayFacade {
	
	private JsonArray array;

	public ArrayFacade(PreviousLevelAccessor previousLevelAccessor) {
		super(previousLevelAccessor);
		this.array = new JsonArray();
		previousLevelAccessor.setCurrentLevelElement(array);
	}
	
	public ArrayFacade(PreviousLevelAccessor previousLevelAccessor, String property) {
		super(previousLevelAccessor);
		this.array = new JsonArray();
		previousLevelAccessor.setCurrentLevelElement(array, property);
	}
	
	@Override
	protected void setCurrentLevelElement(JsonElement e) {
		this.array.add(e);
	}

	public void add(String string) {
		this.array.add(new JsonPrimitive(string));
	}

	public void add(Number number) {
		this.array.add(new JsonPrimitive(number));
	}

	public void add(Boolean bool) {
		this.array.add(new JsonPrimitive(bool));
	}

	public void addNil() {
		this.array.add(JsonNull.INSTANCE);
	}

	public void addArray(IArrayBuilder arrayBuilder) {
		arrayBuilder.build(new ArrayFacade(this));
	}

	public void addObject(IObjectBuilder objectBuilder) {
		objectBuilder.build(new ObjectFacade(this));
	}
	
}
