package com.github.enerccio.gson.builders.functional;

/**
 * Object facade.
 * 
 * @author pvan
 * @since 1.1.0
 */
public interface IObjectFacade {

	/**
	 * Sets this property to this string
	 * @param property
	 * @param string
	 * @since 1.1.0
	 */
	public void put(String property, String string);
	
	/**
	 * Sets this property to this number
	 * @param property
	 * @param number
	 * @since 1.1.0
	 */
	public void put(String property, Number number);
	
	/**
	 * Sets this property to this boolean
	 * @param property
	 * @param bool
	 * @since 1.1.0
	 */
	public void put(String property, Boolean bool);
	
	/**
	 * Sets this property to this string
	 * @param property
	 * @since 1.1.0
	 */
	public void putNil(String property);
	
	/**
	 * Sets this property to this array created via this builder
	 * @param property
	 * @param arrayBuilder
	 * @since 1.1.0
	 */
	public void putArray(String property, IArrayBuilder arrayBuilder);
	
	/**
	 * Sets this property to this object created via this builder 
	 * @param property
	 * @param objectBuilder
	 * @since 1.1.0
	 */
	public void putObject(String property, IObjectBuilder objectBuilder);
	
}
