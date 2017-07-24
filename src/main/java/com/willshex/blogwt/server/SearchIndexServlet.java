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
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;

import com.willshex.blogwt.server.helper.SearchHelper;
import com.willshex.blogwt.server.service.search.ISearch;
import com.willshex.server.ContextAwareServlet;
import com.willshex.service.IService;
import com.willshex.service.ServiceDiscovery;

/**
 * @author billy1380
 *
 */
@WebServlet(name = "Search Indexer", urlPatterns = "/searchindexer")
@ServletSecurity(value = @HttpConstraint(rolesAllowed = "admin"))
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

		String nameParam = REQUEST.get()
				.getParameter(SearchHelper.ENTITY_NAME_KEY);
		String idParam = REQUEST.get().getParameter(SearchHelper.ENTITY_ID_KEY);

		if (nameParam != null && idParam != null) {
			Long id = Long.valueOf(idParam);

			IService service = ServiceDiscovery.getService(nameParam);

			if (service != null && service instanceof ISearch) {
				((ISearch<?>) service).index(id);
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
