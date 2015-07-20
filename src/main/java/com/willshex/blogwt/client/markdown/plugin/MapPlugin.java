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

import org.markdown4j.client.AbstractAsyncPlugin;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * @author William Shakour (billy1380)
 *
 */
public class MapPlugin extends AbstractAsyncPlugin {

	private String apiKey;

	public MapPlugin (String apiKey, HandlerManager manager) {
		super("map", manager);
		this.apiKey = apiKey;
	}

	/* (non-Javadoc)
	 * 
	 * @see org.markdown4j.Plugin#emit(java.lang.StringBuilder, java.util.List,
	 * java.util.Map) */
	@Override
	public void emit (StringBuilder out, final List<String> lines,
			final Map<String, String> params) {

		final String id = HTMLPanel.createUniqueId();
		out.append("<div id=\"");
		out.append(id);
		out.append("\">Loading maps API...</div>");

		ScriptInjector
				.fromUrl(
						"https://maps.googleapis.com/maps/api/js?key=" + apiKey)
				.setCallback(new Callback<Void, Exception>() {
					public void onFailure (Exception reason) {
						GWT.log("Error loading google maps api (key: " + apiKey
								+ ")", reason);
					}

					public void onSuccess (Void result) {
						GWT.log("Injected...");
					}
				}).inject();
	}
}
