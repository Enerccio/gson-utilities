package com.github.enerccio.gson.visitors;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;

/**
 * Called when JSON element terminates and is fully built
 * 
 * @author pvan
 * @since 1.1.0
 */
public interface IJsonElementExit extends IVisitorCallbackBase {

	/**
	 * Returns element that was visited
	 * 
	 * @return json element
	 * @throws IllegalStateException
	 *             when this visit has no element (such as property)
	 * @since 1.1.0
	 */
	public JsonElement getElement();

	/**
	 * Returns property name for this visited element (if element is part of
	 * object)
	 * 
	 * @return name of the property that belongs to this visited element
	 * @throws IllegalArgumentException
	 *             when visited element is not part of object as a property
	 * @since 1.1.0
	 */
	public String getPropertyName();

	/**
	 * Returns index of this visited element if this visited element is part of
	 * the array
	 * 
	 * @return index of this visited element in the array
	 * @throws IllegalArgumentException
	 *             when visited element is not part of array
	 * @since 1.1.0
	 */
	public int getArrayPosition();

	/**
	 * Deserialize object from current element and provided class
	 * <p>
	 * Optional operation.
	 * @param clazz
	 * @return deserialized object
	 * @since 1.1.0
	 * @throws UnsupportedOperationException if operation is not supported by visitor
	 */
	public <X> X getAsObject(Class<X> clazz);

	/**
	 * Deserialize object from current element and provided type
	 * <p>
	 * Optional operation.
	 * @param type
	 * @return deserialized object
	 * @since 1.1.0
	 * @throws UnsupportedOperationException if operation is not supported by visitor
	 */
	public <X> X getAsObject(Type type);
	
	/**
	 * Removes this element from parent gson element.
	 * <p>
	 * If parent of this element is a PROPERTY, property will be set to null.
	 * <p>
	 * If parent of this element is array, null will be inserted into the array to not affect the tree!
	 * <p>
	 * If parent of this element is object, this property will be removed.
	 * @return removed element or null if it was property
	 */
	public JsonElement detach();

}
