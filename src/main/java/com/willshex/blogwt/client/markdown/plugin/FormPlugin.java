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

import org.markdown4j.Plugin;

/**
 * @author William Shakour (billy1380)
 *
 */
public class FormPlugin extends Plugin {

	public FormPlugin () {
		super("form");
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
