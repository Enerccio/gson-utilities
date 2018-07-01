package com.github.enerccio.gson.visitors;

/**
 * Visitor listener.
 * 
 * Implementation should not store entries, exits since they can be reused and
 * would be overwritten.
 * 
 * @author pvan
 * @since 1.1.0
 */
public interface IJsonVisitorListener {

	/**
	 * Called when JSON element is entried
	 * 
	 * @param entry
	 *            information about the entry
	 * @since 1.1.0
	 */
	public void onElementStart(IJsonElementEntry entry);

	/**
	 * Called when JSON element is exited and is fully built.
	 * 
	 * @param exit
	 *            information about exit
	 * @since 1.1.0
	 */
	public void onElementEnd(IJsonElementExit exit);

}
