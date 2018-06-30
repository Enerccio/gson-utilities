package com.github.enerccio.gson.visitors;

import java.util.Collection;

import com.github.enerccio.gson.tree.TreeInfo;

public interface IJsonElementEntry {
	
	/**
	 * Type of the element visited
	 * @return
	 */
	public JsonElementType getType();
	
	/**
	 * tree of properties/indexes of this element
	 * @return 
	 */
	public Collection<TreeInfo> getTree();

}
