//
//  GalleryPlugin.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Jun 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.markdown.plugin;

import java.util.List;
import java.util.Map;

import org.markdown4j.Plugin;

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

	}

}
