package com.github.enerccio.gson.visitors;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import com.github.enerccio.gson.GsonProvider;
import com.github.enerccio.gson.tree.TreeInfo;
import com.google.gson.Gson;

/**
 * Base class for tree visitors.
 * 
 * Handles enter/exit trees and basic field management.
 * 
 * @author pvan
 * @since 1.1.0
 */
abstract class JsonTreeVisitorBase implements IJsonVisitor {

	static IJsonVisitorListener EMPTY_VISITOR = new IJsonVisitorListener() {

		@Override
		public void onElementStart(IJsonElementEntry entry) {

		}

		@Override
		public void onElementEnd(IJsonElementExit exit) {

		}

	};

	private LinkedList<TreeInfo> stack = new LinkedList<TreeInfo>();
	private Collection<TreeInfo> roView = Collections.unmodifiableCollection(stack);
	protected IJsonVisitorListener listener = EMPTY_VISITOR;
	protected Gson gson = GsonProvider.sharedGson;

	/**
	 * Override default gson
	 * 
	 * @param gson
	 * @return this visitor
	 * @since 1.1.0
	 * @see {@link GsonProvider#sharedGson}
	 */
	public IJsonVisitor setGson(Gson gson) {
		if (gson == null)
			throw new NullPointerException("gson can't be null");
		this.gson = gson;
		return this;
	}

	@Override
	public IJsonVisitor setListener(IJsonVisitorListener listener) {
		this.listener = listener;
		return this;
	}

	protected void enter(int index) {
		enter(new TreeInfo(index));
	}

	protected void enter(String property) {
		enter(new TreeInfo(property));
	}

	private void enter(TreeInfo treeInfo) {
		stack.addLast(treeInfo);
	}

	protected void exit() {
		stack.removeLast();
	}

	protected Collection<TreeInfo> getVisitorStack() {
		return roView;
	}
}
