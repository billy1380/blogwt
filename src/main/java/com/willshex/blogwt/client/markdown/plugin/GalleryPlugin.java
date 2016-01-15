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

import org.markdown4j.Plugin;

import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.markdown.plugin.part.GalleryPart;

/**
 * @author William Shakour (billy1380)
 *
 */
public class GalleryPlugin extends Plugin {

	public GalleryPlugin () {
		super("gallery");
	}

	/* (non-Javadoc)
	 * 
	 * @see org.markdown4j.Plugin#emit(java.lang.StringBuilder, java.util.List,
	 * java.util.Map) */
	@Override
	public void emit (StringBuilder out, List<String> lines,
			Map<String, String> params) {
		out.append("Gallery goes here!");
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
