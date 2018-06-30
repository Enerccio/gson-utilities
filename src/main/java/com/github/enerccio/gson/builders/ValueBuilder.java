package com.github.enerccio.gson.builders;

import com.github.enerccio.gson.builders.functional.IArrayBuilder;
import com.github.enerccio.gson.builders.functional.IObjectBuilder;
import com.google.gson.JsonElement;

/**
 * ValueBuilder provides unified interface for creating values.
 * 
 * Methods of this interface may throw {@link IllegalStateException} when
 * accessed at incorrect times.
 * 
 * Methods of this interface are NOT thread safe.
 * 
 * @author pvan
 *
 */
public interface ValueBuilder {

	/**
	 * Ends current structured element.
	 * 
	 * @return previous element
	 * @throws {@link IllegalStateException} when there is nothing to end
	 */
	ValueBuilder end();

	/**
	 * Opens object declaration.
	 * 
	 * @return object builder
	 * @throws {@link IllegalStateException} when creating object is not possible
	 */
	ValueBuilder object();

	/**
	 * Opens array declaration.
	 * 
	 * @return array builder
	 * @throws {@link IllegalStateException} when creating array is not possible
	 */
	ValueBuilder array();

	/**
	 * Opens object property.
	 * 
	 * @return object property builder
	 * @throws {@link IllegalStateException} when creating object property is not
	 *        possible
	 */
	ValueBuilder property(String name);

	/**
	 * Creates string primitive value.
	 * 
	 * {@literal null} value on property erases whole property.
	 * 
	 * @return this builder
	 * @throws {@link IllegalStateException} when creating string value is not
	 *        possible
	 */
	ValueBuilder string(String value);

	/**
	 * Creates number primitive value.
	 * 
	 * {@literal null} value on property erases whole property.
	 * 
	 * @return this builder
	 * @throws {@link IllegalStateException} when creating number value is not
	 *        possible
	 */
	ValueBuilder number(Number value);

	/**
	 * Creates boolean primitive value.
	 * 
	 * {@literal null} value on property erases whole property.
	 * 
	 * @return this builder
	 * @throws {@link IllegalStateException} when creating boolean value is not
	 *        possible
	 */
	ValueBuilder bool(Boolean value);

	/**
	 * Creates character primitive value.
	 * 
	 * {@literal null} value on property erases whole property.
	 * 
	 * @return this builder
	 * @throws {@link IllegalStateException} when creating character value is not
	 *        possible
	 */
	ValueBuilder character(Character value);

	/**
	 * Creates null primitive value. 
	 * 
	 * Null value on property erases whole property.
	 * 
	 * @return this builder
	 * @throws {@link IllegalStateException} when creating null value is not
	 *        possible
	 */
	ValueBuilder nil();

	/**
	 * Instantly closes the generation and returns what was build so far.
	 * 
	 * @return generated json
	 * @throws {@link IllegalStateException} when returning would leave
	 *        inconsisted state
	 */
	String toJson();

	/**
	 * Instantly closes the generation and returns what was build so far.
	 * 
	 * @return generated json as JsonElement
	 * @throws {@link IllegalStateException} when returning would leave
	 *        inconsisted state
	 */
	JsonElement toJsonTree();
	
	/**
	 * Creates object via calling the provider
	 * 
	 * @return self
	 * @throws {@link IllegalStateException} when creating object is not possible
	 * @since 1.1.0
	 */
	ValueBuilder object(IObjectBuilder builder);
	
	/**
	 * Creates array via calling the provider
	 * 
	 * @return self
	 * @throws {@link IllegalStateException} when creating array is not possible
	 * @since 1.1.0
	 */
	ValueBuilder array(IArrayBuilder builder);

}
