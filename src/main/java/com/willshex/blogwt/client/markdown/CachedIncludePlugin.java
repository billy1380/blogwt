//
//  CachedIncludePlugin.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 16 Jun 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.markdown;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.markdown4j.client.IncludePlugin;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.HandlerManager;

/**
 * @author William Shakour (billy1380)
 *
 */
public class CachedIncludePlugin extends IncludePlugin {

	private Map<String, String> cache;

	/**
	 * @param manager
	 */
	public CachedIncludePlugin (HandlerManager manager) {
		super(manager);
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
