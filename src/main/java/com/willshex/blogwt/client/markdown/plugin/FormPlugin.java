//
//  FormPlugin.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 4 Nov 2015.
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

/**
 * @author William Shakour (billy1380)
 *
 */
public class FormPlugin extends AbstractAsyncPlugin {

	public FormPlugin (HandlerManager manager) {
		super("form", manager);
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
		out.append("\"> Loading form...</div>");

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute () {
				if (manager != null) {
					manager.fireEvent(new PluginContentReadyEvent(
							FormPlugin.this, lines, params, id, "None"));
				}
			}
		});
	}

}
