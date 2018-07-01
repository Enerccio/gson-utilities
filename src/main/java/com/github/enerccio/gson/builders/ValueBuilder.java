package com.github.enerccio.gson.builders;

import com.github.enerccio.gson.builders.functional.IArrayBuilder;
import com.github.enerccio.gson.builders.functional.IObjectBuilder;
import com.google.gson.JsonElement;

/**
 * ValueBuilder provides unified interface for creating values.
 * <p>
 * Methods of this interface may throw {@link IllegalStateException} when
 * accessed at incorrect times.
 * <p>
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
	 * @throws {@link
	 *             IllegalStateException} when there is nothing to end
	 */
	ValueBuilder end();

	/**
	 * Opens object declaration.
	 * 
	 * @return object builder
	 * @throws {@link
	 *             IllegalStateException} when creating object is not possible
	 */
	ValueBuilder object();

	/**
	 * Opens array declaration.
	 * 
	 * @return array builder
	 * @throws {@link
	 *             IllegalStateException} when creating array is not possible
	 */
	ValueBuilder array();

	/**
	 * Opens object property.
	 * 
	 * @return object property builder
	 * @throws {@link
	 *             IllegalStateException} when creating object property is not
	 *             possible
	 */
	ValueBuilder property(String name);

	/**
	 * Creates string primitive value.
	 * 
	 * @return this builder
	 * @throws {@link
	 *             IllegalStateException} when creating string value is not
	 *             possible
	 */
	ValueBuilder string(String value);

	/**
	 * Creates number primitive value.
	 * 
	 * @return this builder
	 * @throws {@link
	 *             IllegalStateException} when creating number value is not
	 *             possible
	 */
	ValueBuilder number(Number value);

	/**
	 * Creates boolean primitive value.
	 * 
	 * @return this builder
	 * @throws {@link
	 *             IllegalStateException} when creating boolean value is not
	 *             possible
	 */
	ValueBuilder bool(Boolean value);

	/**
	 * Creates character primitive value.
	 * 
	 * @return this builder
	 * @throws {@link
	 *             IllegalStateException} when creating character value is not
	 *             possible
	 */
	ValueBuilder character(Character value);

	/**
	 * Creates null primitive value.
	 * 
	 * @return this builder
	 * @throws {@link
	 *             IllegalStateException} when creating null value is not
	 *             possible
	 */
	ValueBuilder nil();

	/**
	 * Instantly closes the generation and returns what was build so far.
	 * 
	 * @return generated json
	 * @throws {@link
	 *             IllegalStateException} when returning would leave inconsisted
	 *             state
	 */
	String toJson();

	/**
	 * Instantly closes the generation and returns what was build so far.
	 * 
	 * @return generated json as JsonElement
	 * @throws {@link
	 *             IllegalStateException} when returning would leave inconsisted
	 *             state
	 */
	JsonElement toJsonTree();

	/**
	 * Creates object via calling the provider
	 * 
	 * @return self
	 * @throws {@link
	 *             IllegalStateException} when creating object is not possible
	 * @since 1.1.0
	 */
	ValueBuilder object(IObjectBuilder builder);

	/**
	 * Creates array via calling the provider
	 * 
	 * @return self
	 * @throws {@link
	 *             IllegalStateException} when creating array is not possible
	 * @since 1.1.0
	 */
	ValueBuilder array(IArrayBuilder builder);

	/**
	 * Appends this element to the builder
	 * 
	 * @param element
	 *            to be appended
	 * @return this builder
	 * @throws {@link
	 *             IllegalStateException} when appending element is not possible
	 * @since 1.1.0
	 */
	ValueBuilder element(JsonElement element);

	/**
	 * Creates string primitive value with property.
	 * <p>
	 * Shortcut to property().string()
	 * 
	 * @return this builder
	 * @throws {@link
	 *             IllegalStateException} when creating string value is not
	 *             possible
	 */
	ValueBuilder string(String property, String value);

	/**
	 * Creates number primitive value with property.
	 * <p>
	 * Shortcut to property().number()
	 * 
	 * @return this builder
	 * @throws {@link
	 *             IllegalStateException} when creating number value is not
	 *             possible
	 * @since 1.1.0
	 */
	ValueBuilder number(String property, Number value);

	/**
	 * Creates boolean primitive value with property.
	 * <p>
	 * Shortcut to property().character()
	 * 
	 * @return this builder
	 * @throws {@link
	 *             IllegalStateException} when creating boolean value is not
	 *             possible
	 * @since 1.1.0
	 */
	ValueBuilder bool(String property, Boolean value);

	/**
	 * Creates character primitive value with property.
	 * <p>
	 * Shortcut to property().character()
	 * 
	 * {@literal null} value on property erases whole property.
	 * 
	 * @return this builder
	 * @throws {@link
	 *             IllegalStateException} when creating character value is not
	 *             possible
	 * @since 1.1.0
	 */
	ValueBuilder character(String property, Character value);

	/**
	 * Creates null primitive value with property.
	 * <p>
	 * Shortcut to property().nil()
	 * 
	 * Null value on property erases whole property depending on how gson is
	 * configured.
	 * 
	 * @return this builder
	 * @throws {@link
	 *             IllegalStateException} when creating null value is not
	 *             possible
	 * @since 1.1.0
	 */
	ValueBuilder nil(String property);

	/**
	 * Appends this element to the builder with property.
	 * <p>
	 * Shortcut to property().string()
	 * 
	 * @param element
	 *            to be appended
	 * @return this builder
	 * @throws {@link
	 *             IllegalStateException} when appending element is not possible
	 * @since 1.1.0
	 */
	ValueBuilder element(String property, JsonElement element);

	/**
	 * Creates object via calling the provider and property
	 * <p>
	 * Shortcut to property().array()
	 * 
	 * @return self
	 * @throws {@link
	 *             IllegalStateException} when creating object is not possible
	 * @since 1.1.0
	 */
	ValueBuilder object(String property, IObjectBuilder builder);

	/**
	 * Creates array via calling the provider and property
	 * <p>
	 * Shortcut to property().array()
	 * 
	 * @return self
	 * @throws {@link
	 *             IllegalStateException} when creating array is not possible
	 * @since 1.1.0
	 */
	ValueBuilder array(String property, IArrayBuilder builder);
}
