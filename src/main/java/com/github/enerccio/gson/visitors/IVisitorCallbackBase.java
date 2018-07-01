package com.github.enerccio.gson.visitors;

import java.util.Collection;

import com.github.enerccio.gson.tree.TreeInfo;

/**
 * Base interface for visitor events
 * 
 * @author pvan
 * @since 1.1.0
 */
public interface IVisitorCallbackBase {

	/**
	 * Type of the element visited
	 * 
	 * @return
	 * @since 1.1.0
	 */
	public JsonElementType getType();

	/**
	 * tree of properties/indexes of this element
	 * 
	 * @return
	 * @since 1.1.0
	 */
	public Collection<TreeInfo> getTree();

	/**
	 * Textual representation of the {@link #getTree()}
	 * 
	 * @return textual representation of the path
	 */
	public String getPath();

}
