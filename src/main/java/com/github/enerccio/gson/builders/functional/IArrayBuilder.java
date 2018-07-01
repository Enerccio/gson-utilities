package com.github.enerccio.gson.builders.functional;

/**
 * Functional builder alternative interface.
 * <p>
 * Used for functional building of JSON values.
 * 
 * @author pvan
 * @since 1.1.0
 */
public interface IArrayBuilder {

	/**
	 * Override this method to build array
	 * 
	 * @param array
	 * @since 1.1.0
	 */
	public void build(IArrayFacade array);

}
