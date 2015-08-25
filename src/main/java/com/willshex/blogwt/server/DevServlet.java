//
//  DevServlet.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server;

import java.io.IOException;

import javax.servlet.ServletException;

import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.tag.TagServiceProvider;
import com.willshex.service.ContextAwareServlet;

/**
 * @author William Shakour (billy1380)
 *
 */
public class DevServlet extends ContextAwareServlet {

	private static final long serialVersionUID = 8911904038164388255L;

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.service.ContextAwareServlet#doGet() */
	@Override
	protected void doGet () throws ServletException, IOException {
		super.doGet();

		String action = REQUEST.get().getParameter("action");

		if (action != null) {
			action = action.toLowerCase();
		}

		if ("gentags".equals(action)) {
			TagServiceProvider.provide().generateTags();
		} else if ("indexall".equals(action)) {
			PostServiceProvider.provide().indexAll();
		} else if ("archiveall".equals(action)) {
			PostServiceProvider.provide().archiveAll();
		}
	}
}
