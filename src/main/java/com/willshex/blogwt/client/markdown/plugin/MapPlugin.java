//
//  MapPlugin.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 19 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.markdown.plugin;

import java.util.List;
import java.util.Map;

import org.markdown4j.Plugin;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.core.shared.GWT;

/**
 * @author William Shakour (billy1380)
 *
 */
public class MapPlugin extends Plugin {

	private String apiKey;

	public MapPlugin (String apiKey) {
		super("map");
		this.apiKey = apiKey;
	}

	/* (non-Javadoc)
	 * 
	 * @see org.markdown4j.Plugin#emit(java.lang.StringBuilder, java.util.List,
	 * java.util.Map) */
	@Override
	public void emit (StringBuilder out, List<String> lines,
			Map<String, String> params) {

		ScriptInjector
				.fromUrl(
						"https://maps.googleapis.com/maps/api/js?key=" + apiKey)
				.setCallback(new Callback<Void, Exception>() {
					public void onFailure (Exception reason) {
						GWT.log("Error loading google maps api (key: " + apiKey
								+ ")", reason);
					}

					public void onSuccess (Void result) {

					}
				}).inject();
	}
}
