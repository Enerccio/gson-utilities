package com.github.enerccio.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Shared gson for all utilities.
 * 
 * Gson is thread safe and therefore can be shared across unless specific gson
 * is needed.
 * 
 * @author pvan
 * @since 1.1.0
 */
public class GsonProvider {

	/**
	 * Shared Gson instance
	 * 
	 * @since 1.1.0
	 */
	public static final Gson sharedGson = new GsonBuilder().setPrettyPrinting().create();

}
