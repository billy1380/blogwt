//
//  PostHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper;

import java.util.List;
import java.util.Map;

import org.markdown4j.Plugin;
import org.markdown4j.client.IncludePlugin;
import org.markdown4j.client.MarkdownProcessor;
import org.markdown4j.client.event.PluginContentReadyEventHandler;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.markdown.Processor;
import com.willshex.blogwt.client.markdown.plugin.FormPlugin;
import com.willshex.blogwt.client.markdown.plugin.MapPlugin;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostHelper extends com.willshex.blogwt.shared.helper.PostHelper {

	private static MarkdownProcessor processor;
	private static final PluginContentReadyEventHandler DEFAULT_PLUGIN_READY_HANDLER = new PluginContentReadyEventHandler() {

		@Override
		public void ready (PluginContentReadyEvent event, Plugin plugin,
				List<String> lines, Map<String, String> params, String id,
				String content) {
			Element el = Document.get().getElementById(id);

			if (plugin instanceof IncludePlugin) {
				if (el != null && content != null) {
					el.setInnerHTML(content);
				}
			} else if (plugin instanceof MapPlugin) {

				if (el != null) {
					MapHelper.showMap(el, lines, params);
				}
			} else if (plugin instanceof FormPlugin) {
				if (el != null && content != null) {
					el.removeAllChildren();

					Widget form = ((FormPlugin) plugin).createWidget(lines,
							params);
					RootPanel.get().add(form);
					el.appendChild(form.getElement());
				}
			}
		}
	};

	private static MarkdownProcessor processor () {
		if (processor == null) {
			processor = new Processor();
		}

		return processor;
	}

	public static String makeMarkup (String value) {
		return processor().process(value);
	}

	public static String makeHeading (String value) {
		if (!value.startsWith("#")) {
			value = "#" + value;
		}

		return makeMarkup(value);
	}

	public static String makeHeading2 (String value) {
		if (!value.startsWith("##")) {
			value = "##" + value;
		}

		return makeMarkup(value);
	}

	/**
	 * @return
	 */
	public static HandlerRegistration handlePluginContentReady () {
		return processor().addPluginContentReadyHandler(
				DEFAULT_PLUGIN_READY_HANDLER);
	}

	public static void resetEmojiTheme () {
		((Processor) processor()).resetEmojiTheme();
	}

}
