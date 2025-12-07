//
//  PostHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper;

import org.markdown4j.client.IncludePlugin;
import org.markdown4j.client.MarkdownProcessor;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.markdown.Processor;
import com.willshex.blogwt.client.markdown.plugin.FormPlugin;
import com.willshex.blogwt.client.markdown.plugin.MapPlugin;
import com.willshex.blogwt.client.markdown.plugin.PostsPlugin;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostHelper extends com.willshex.blogwt.shared.helper.PostHelper {

	private static MarkdownProcessor processor;

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

	public static HandlerRegistration handlePluginContentReady () {
		return processor().addPluginContentReadyHandler(
				(event, plugin, lines, params, id, content) -> {
					Element el = Document.get().getElementById(id);

					if (plugin instanceof IncludePlugin
							|| plugin instanceof PostsPlugin) {
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

							// FIXME: probably leaking this
							// on unload never seems to get called
							Widget form = FormPlugin.createWidget(lines,
									params);
							RootPanel.get().add(form);
							el.appendChild(form.getElement());
						}
					}
				});
	}

}
