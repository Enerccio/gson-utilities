package com.github.enerccio.gson.builders.functional;

/**
 * Functional builder alternative interface for objects.
 * <p>
 * Used for functional building of JSON values.
 * 
 * @author pvan
 * @since 1.1.0
 */
public interface IObjectBuilder {

	/**
	 * Override this method to build object
	 * 
	 * @param object
	 * @since 1.1.0
	 */
	public void build(IObjectFacade object);

}
