package com.github.enerccio.gson.visitors;

/**
 * Called when JSON element starts
 * 
 * @author pvan
 * @since 1.1.0
 */
public interface IJsonElementEntry extends IVisitorCallbackBase {

	/**
	 * Skips the visiting of this element. No visitors will be called until
	 * current element ends
	 * 
	 * @since 1.1.0
	 */
	public void skip();

}
