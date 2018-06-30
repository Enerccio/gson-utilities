package com.github.enerccio.gson.builders;

import com.google.gson.JsonElement;

abstract class PreviousLevelAccessor {

	protected PreviousLevelAccessor prevLevelAccessor;
	
	PreviousLevelAccessor(PreviousLevelAccessor prevLevelAccessor) {
		this.prevLevelAccessor = prevLevelAccessor;
	}
	
	/**
	 * Sets the json element of this level to e.
	 * @param e
	 * @throws {@link IllegalStateException} when this builder does not support setting elements
	 */
	protected void setCurrentLevelElement(JsonElement e) {
		throw new IllegalStateException();
	}

	/**
	 * Returns element that belongs to this builder.
	 * @return element
	 * @throws {@link IllegalStateException} when this builder does not have any element
	 */
	protected JsonElement getCurrentLevelElement() {
		throw new IllegalStateException();
	}
	
	/**
	 * Sets the json element with property name
	 * @param e json element
	 * @param p property name 
	 * @since 1.1.0
	 */
	protected void setCurrentLevelElement(JsonElement e, String p) {
		throw new IllegalStateException();
	}
}
