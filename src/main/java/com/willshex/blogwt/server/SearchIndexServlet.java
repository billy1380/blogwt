//
//  SearchIndexServlet.java
//  blogwt
//
//  Created by billy1380 on 30 Dec 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import com.willshex.blogwt.server.service.page.IPageService;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.post.IPostService;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.user.IUserService;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.service.ContextAwareServlet;

/**
 * @author billy1380
 *
 */
public class SearchIndexServlet extends ContextAwareServlet {

	private static final long serialVersionUID = 7829996840917475240L;
	private static final Logger LOG = Logger
			.getLogger(SearchIndexServlet.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.service.ContextAwareServlet#doGet() */
	@Override
	protected void doGet () throws ServletException, IOException {
		super.doGet();

		String appEngineQueue = REQUEST.get()
				.getHeader("X-AppEngine-QueueName");

		if (LOG.isLoggable(Level.FINE)) {
			LOG.log(Level.FINE,
					String.format("appEngineQueue is [%s]", appEngineQueue));
		}

		boolean isQueue = appEngineQueue != null
				&& "default".toLowerCase().equals(appEngineQueue.toLowerCase());

		if (!isQueue) {
			RESPONSE.get().setStatus(401);
			RESPONSE.get().getOutputStream().print("failure");
			LOG.log(Level.WARNING,
					"Attempt to run script directly, this is not permitted");
			return;
		}

		if (LOG.isLoggable(Level.FINE)) {
			LOG.log(Level.FINE, String.format(
					"Call from [%s] allowed to proceed", appEngineQueue));
		}

		String nameParam = REQUEST.get().getParameter("name");
		String idParam = REQUEST.get().getParameter("id");

		if (nameParam != null && idParam != null) {
			Long id = Long.valueOf(idParam);

			switch (nameParam) {
			case IUserService.NAME:
				UserServiceProvider.provide().indexUser(id);
				break;
			case IPostService.NAME:
				PostServiceProvider.provide().indexPost(id);
				break;
			case IPageService.NAME:
				PageServiceProvider.provide().indexPage(id);
				break;
			default:
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.service.ContextAwareServlet#doPost() */
	@Override
	protected void doPost () throws ServletException, IOException {
		doGet();
	}

}
