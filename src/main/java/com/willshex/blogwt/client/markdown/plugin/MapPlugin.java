//
//  MapPlugin.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 19 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.markdown.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.markdown4j.client.AbstractAsyncPlugin;
import org.markdown4j.client.event.PluginContentReadyEventHandler.PluginContentReadyEvent;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.helper.MapHelper;
import com.willshex.blogwt.client.helper.MapHelper.MapsReadyEventHandler;

/**
 * @author William Shakour (billy1380)
 *
 */
public class MapPlugin extends AbstractAsyncPlugin {

	private static Map<String, MapPluginItem> items = new HashMap<String, MapPlugin.MapPluginItem>();

	private static class MapPluginItem {
		public String id;
		public List<String> lines;
		public Map<String, String> params;
		public HandlerManager mapManager;
		public MapPlugin instance;
	}

	public MapPlugin (String apiKey, HandlerManager manager) {
		super("map", manager);

		DefaultEventBus.get().addHandler(MapsReadyEventHandler.TYPE,
				this::mapsInitialised);
	}

	/* (non-Javadoc)
	 * 
	 * @see org.markdown4j.Plugin#emit(java.lang.StringBuilder, java.util.List,
	 * java.util.Map) */
	@Override
	public void emit (StringBuilder out, final List<String> lines,
			final Map<String, String> params) {

		MapPluginItem item = new MapPluginItem();
		item.mapManager = manager;
		item.instance = this;
		item.id = HTMLPanel.createUniqueId();
		item.lines = lines;
		item.params = params;

		items.put(item.id, item);

		out.append("<div id=\"");
		out.append(item.id);
		out.append("\">Loading maps API...</div>");

		MapHelper.inject();
	}

	public void mapsInitialised () {
		List<String> remove = new ArrayList<String>();

		MapPluginItem item;
		for (String id : items.keySet()) {
			item = items.get(id);
			readyToLoad(item.mapManager, item.instance, item.id, item.lines,
					item.params);
			remove.add(id);
		}

		for (String id : remove) {
			items.remove(id);
		}
	}

	private static void readyToLoad (final HandlerManager manager,
			final MapPlugin instance, final String id, final List<String> lines,
			final Map<String, String> params) {
		Scheduler.get().scheduleDeferred( () -> {
			if (manager != null) {
				manager.fireEvent(new PluginContentReadyEvent(instance, lines,
						params, id, null));
			}
		});
	}
}
