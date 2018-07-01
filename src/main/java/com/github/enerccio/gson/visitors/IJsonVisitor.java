package com.github.enerccio.gson.visitors;

/**
 * JSON visitor interface. Allows visiting JSON content.
 * 
 * By default, JSON visitors are NOT thread safe unless specified.
 * 
 * @author pvan
 * @since 1.1.0
 */
public interface IJsonVisitor {

	/**
	 * Sets the listener to this visitor
	 * 
	 * @param listener
	 * @return this visitor
	 * @since 1.1.0
	 */
	public IJsonVisitor setListener(IJsonVisitorListener listener);

	/**
	 * Visits the string provided, calling previously set listener in the
	 * process
	 * 
	 * @param json
	 *            to visit
	 * @since 1.1.0
	 */
	public void visit(String json);

}
