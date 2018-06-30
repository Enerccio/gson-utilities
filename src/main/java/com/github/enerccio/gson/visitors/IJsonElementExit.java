package com.github.enerccio.gson.visitors;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;

/**
 * 
 * @author pvan
 * @since 1.1.0
 */
public interface IJsonElementExit extends IJsonElementEntry {
	
	/**
	 * Returns element that was visited
	 * @return json element
	 * @throws IllegalStateException when this visit has no element (such as property)
	 * @since 1.1.0
	 */
	public JsonElement getElement();
	
	/**
	 * Returns property name for this visited element (if element is part of object)
	 * @return name of the property that belongs to this visited element
	 * @throws IllegalArgumentException when visited element is not part of object as a property
	 * @since 1.1.0
	 */
	public String getPropertyName();
	
	/**
	 * Returns index of this visited element if this visited element is part of the array
	 * @return index of this visited element in the array
	 * @throws IllegalArgumentException when visited element is not part of array
	 * @since 1.1.0
	 */
	public int getArrayPosition();
	
	/**
	 * 
	 * @param clazz
	 * @return
	 * @since 1.1.0
	 */
	public <X> X getAsObject(Class<X> clazz);
	
	/**
	 * 
	 * @param type
	 * @return
	 * @since 1.1.0
	 */
	public <X> X getAsObject(Type type);
	
}
