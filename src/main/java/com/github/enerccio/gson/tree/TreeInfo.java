package com.github.enerccio.gson.tree;

import java.text.ParseException;
import java.util.List;

public class TreeInfo {
	
	public enum TreeInfoType {
		PROPERTY, INDEX
	}
	
	public static final int NOT_AN_INDEX = -1;
	
	private final TreeInfoType type;
	private final int index;
	private final String property;
	
	public TreeInfo(int index) {
		this(TreeInfoType.INDEX, index, null);
	}
	
	public TreeInfo(String property) {
		this(TreeInfoType.PROPERTY, NOT_AN_INDEX, property);
	}

	private TreeInfo(TreeInfoType type, int index, String property) {
		this.type = type;
		this.index = index;
		this.property = property;
	}

	public TreeInfoType getType() {
		return type;
	}

	public int getIndex() {
		return index;
	}

	public String getProperty() {
		return property;
	}

	/**
	 * Determines whether pattern matches the tree path provided.
	 * 
	 * @see {@link JsonPatternMatcher}
	 * @param path
	 * @param pattern to match 
	 * @return true if matches, false otherwise
	 */
	public static boolean matches(List<TreeInfo> path, String pattern) throws ParseException {
		JsonPatternMatcher matcher = new JsonPatternMatcher(pattern);
		return matcher.matches(path);
	}
}
