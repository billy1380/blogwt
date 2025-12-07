//
//  GalleryPlugin.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.markdown.plugin;

import java.util.List;
import java.util.Map;

import org.markdown4j.client.AbstractAsyncPlugin;
import org.markdown4j.client.event.PluginContentReadyEventHandler.PluginContentReadyEvent;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.markdown.plugin.part.GalleryPart;

/**
 * @author William Shakour (billy1380)
 *
 */
public class GalleryPlugin extends AbstractAsyncPlugin {

	public GalleryPlugin (HandlerManager manager) {
		super("gallery", manager);
	}
	@Override
	public void emit (StringBuilder out, final List<String> lines,
			final Map<String, String> params) {
		final String id = HTMLPanel.createUniqueId();
		out.append("<div id=\"");
		out.append(id);
		out.append("\"> Loading gallery...</div>");

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute () {
				if (manager != null) {
					manager.fireEvent(new PluginContentReadyEvent(
							GalleryPlugin.this, lines, params, id, "None"));
				}
			}
		});
	}

	public Widget createWidget (List<String> lines,
			final Map<String, String> params) {
		GalleryPart gallery = new GalleryPart();

		gallery.setParams(params);

		for (String line : lines) {
			if (line.length() > 0) {
				gallery.addImageWithLine(line);
			}
		}

		return gallery;
	}

}
