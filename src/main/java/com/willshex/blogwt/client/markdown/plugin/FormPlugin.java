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
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.markdown.plugin.part.FormPart;

/**
 * @author William Shakour (billy1380)
 *
 */
public class FormPlugin extends AbstractAsyncPlugin {

	public final static String HEADING_PARAM_KEY = "heading";
	public final static String SUBHEADING_PARAM_KEY = "subheading";
	private static final Object PRETEXT_PARAM_KEY = "pretext";
	private static final Object POSTTEXT_PARAM_KEY = "posttext";

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

	public Widget createWidget (List<String> lines,
			final Map<String, String> params) {
		FormPart form = new FormPart();

		form.setHeading(params.get(HEADING_PARAM_KEY));
		form.setSubHeading(params.get(SUBHEADING_PARAM_KEY));
		form.setPreText(params.get(PRETEXT_PARAM_KEY));
		form.setPostText(params.get(POSTTEXT_PARAM_KEY));

		for (String line : lines) {
			if (line.length() > 0) {
				form.addFieldWithLine(line);
			}
		}

		return form;
	}

}
