//
//  CachedIncludePlugin.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 16 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.markdown.plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.markdown4j.client.IncludePlugin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.willshex.blogwt.client.Resources;

/**
 * @author William Shakour (billy1380)
 *
 */
public class CachedIncludePlugin extends IncludePlugin {

	private Map<String, String> cache;

	public interface CachedIncludePluginTemplates extends SafeHtmlTemplates {
		CachedIncludePluginTemplates INSTANCE = GWT
				.create(CachedIncludePluginTemplates.class);

		@Template("<div id=\"{0}\"><div style=\"margin:20px 0\" ><img src=\"{1}\" alt=\"Loading {2}\"> <span style=\"font-weight:bold\">Loading include...</span><div class=\"small text-muted\">{2}</div></div></div>")
		SafeHtml loadingButton (String id, SafeUri imgSrc, String includeSrc);
	}

	/**
	 * @param manager
	 */
	public CachedIncludePlugin (HandlerManager manager) {
		super(manager);
	}

	/* (non-Javadoc)
	 * 
	 * @see org.markdown4j.client.IncludePlugin#emit(java.lang.StringBuilder,
	 * java.util.List, java.util.Map) */
	@Override
	public void emit (StringBuilder out, List<String> lines,
			Map<String, String> params) {
		String src = params.get("src");
		try {
			if (src != null && src.length() > 0) {
				String id = HTMLPanel.createUniqueId();
				out.append(CachedIncludePluginTemplates.INSTANCE.loadingButton(
						id, Resources.RES.defaultLoader().getSafeUri(), src)
						.asString());

				getContent(src, id, lines, params);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while rendering "
					+ this.getClass().getName(), e);
		}
	}

	protected Map<String, String> ensureCache () {
		return (cache == null ? cache = new HashMap<String, String>() : cache);
	}

	/* (non-Javadoc)
	 * 
	 * @see org.markdown4j.client.IncludePlugin#getContent(java.lang.String,
	 * java.lang.String, java.util.List, java.util.Map) */
	@Override
	protected void getContent (final String src, final String id,
			final List<String> lines, final Map<String, String> params)
			throws IOException {
		if (src != null && src.length() > 0) {
			if (ensureCache().containsKey(src)) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					@Override
					public void execute () {
						CachedIncludePlugin.super.gotContent(null,
								ensureCache().get(src), src, id, lines, params);
					}
				});
			} else {
				super.getContent(src, id, lines, params);
			}
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see org.markdown4j.client.IncludePlugin#gotContent(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.util.List,
	 * java.util.Map) */
	@Override
	protected void gotContent (String content, String processed, String src,
			String id, List<String> lines, Map<String, String> params) {

		if (processed != null && processed.length() > 0) {
			ensureCache().put(src, processed);
		}

		super.gotContent(content, processed, src, id, lines, params);
	}

	public void flushCache () {
		if (cache != null) {
			cache.clear();
		}
	}

}
