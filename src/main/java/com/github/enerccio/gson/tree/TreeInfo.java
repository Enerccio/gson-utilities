package com.github.enerccio.gson.tree;

import java.text.ParseException;
import java.util.Collection;

/**
 * Represents level of nesting in JSON
 * 
 * Internal class but exposed publicly. Use at your own risk.
 * 
 * @author pvan
 * @since 1.1.0
 */
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

	@Override
	public String toString() {
		if (type == TreeInfoType.INDEX)
			return "TreeInfo [index=" + index + "]";
		else
			return "TreeInfo [property=" + property + "]";
	}

	/**
	 * Determines whether pattern matches the tree path provided.
	 * 
	 * @see {@link JsonPatternMatcher}
	 * @param path
	 * @param pattern
	 *            to match
	 * @return true if matches, false otherwise
	 * @since 1.1.0
	 */
	public static boolean matches(Collection<TreeInfo> path, String pattern) throws ParseException {
		JsonPatternMatcher matcher = new JsonPatternMatcher(pattern);
		return matcher.matches(path);
	}

	/**
	 * Transforms list of tree info into a single path
	 * 
	 * @param tree
	 * @return path
	 * @since 1.1.0
	 */
	public static String toPath(Collection<TreeInfo> tree) {
		String path = "";
		for (TreeInfo nfo : tree) {
			switch (nfo.getType()) {
			case INDEX:
				path += "@" + nfo.getIndex();
				break;
			case PROPERTY:
				path += nfo.getProperty();
				break;
			}
			path += ".";
		}
		if (path.endsWith("."))
			path = path.substring(0, path.length() - 1);
		return path;
	}
}
