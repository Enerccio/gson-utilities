package com.github.enerccio.gson.visitors;

/**
 * 
 * @author pvan
 * @since 1.1.0
 */
public interface IJsonVisitor {

	/**
	 * @since 1.1.0
	 */
	public void onElementStart();
	
	/**
	 * @since 1.1.0
	 */
	public void onElementEnd();

}
