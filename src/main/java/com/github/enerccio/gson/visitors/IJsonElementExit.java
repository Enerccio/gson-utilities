package com.github.enerccio.gson.visitors;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;

public interface IJsonElementExit extends IJsonElementEntry {
	
	/**
	 * Returns element that was visited
	 * @return json element
	 * @throws IllegalStateException when this visit has no element (such as property)
	 */
	public JsonElement getElement();
	
	/**
	 * Returns property name for this visited element (if element is part of object)
	 * @return name of the property that belongs to this visited element
	 * @throws IllegalArgumentException when visited element is not part of object as a property
	 */
	public String getPropertyName();
	
	/**
	 * Returns index of this visited element if this visited element is part of the array
	 * @return index of this visited element in the array
	 * @throws IllegalArgumentException when visited element is not part of array
	 */
	public int getArrayPosition();
	
	public <X> X getAsObject(Class<X> clazz);
	
	public <X> X getAsObject(Type type);
	
}
