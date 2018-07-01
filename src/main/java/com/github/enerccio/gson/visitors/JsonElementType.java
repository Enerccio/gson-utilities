package com.github.enerccio.gson.visitors;

/**
 * Type of the underline JSON element
 * 
 * @author pvan
 * @since 1.1.0
 */
public enum JsonElementType {
	// standard types
	/**
	 * JSON element is string
	 * 
	 * @since 1.1.0
	 */
	STRING,
	/**
	 * JSON element is number
	 * 
	 * @since 1.1.0
	 */
	NUMBER,
	/**
	 * JSON element is boolean
	 * 
	 * @since 1.1.0
	 */
	BOOLEAN,
	/**
	 * JSON element is null
	 * 
	 * @since 1.1.0
	 */
	NULL,
	// specials
	/**
	 * JSON element is array
	 * 
	 * @since 1.1.0
	 */
	ARRAY,
	/**
	 * JSON element is property.
	 * 
	 * Property does not exist in GSON, so retrieved element will be object with
	 * one property and value
	 * 
	 * @since 1.1.0
	 */
	PROPERTY,
	/**
	 * JSON element is object
	 * 
	 * @since 1.1.0
	 */
	OBJECT

}
