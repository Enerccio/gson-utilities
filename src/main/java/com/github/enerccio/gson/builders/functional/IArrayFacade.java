package com.github.enerccio.gson.builders.functional;

/**
 * Array facade.
 * 
 * @author pvan
 * @since 1.1.0
 */
public interface IArrayFacade {
	
	/**
	 * Adds this string to array
	 * @param string
	 * @since 1.1.0
	 */
	public void add(String string);
	
	/**
	 * Adds this number to array
	 * @param number
	 * @since 1.1.0
	 */
	public void add(Number number);
	
	/**
	 * Adds this boolean to array
	 * @param bool
	 * @since 1.1.0
	 */
	public void add(Boolean bool);
	
	/**
	 * Adds null to this array
	 * @since 1.1.0
	 */
	public void addNil();
	
	/**
	 * Adds this array to array
	 * @param arrayBuilder
	 * @since 1.1.0
	 */
	public void addArray(IArrayBuilder arrayBuilder);
	
	/**
	 * Adds this object to array
	 * @param objectBuilder
	 * @since 1.1.0
	 */
	public void addObject(IObjectBuilder objectBuilder);
}
