//
//  QueueHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 12 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.api.taskqueue.TransientFailureException;
import com.google.gson.JsonObject;
import com.willshex.gson.shared.Convert;
import com.willshex.gson.web.service.server.ServiceException;
import com.willshex.gson.web.service.shared.Request;
import com.willshex.server.ContextAwareServlet;

/**
 * @author William Shakour (billy1380)
 *
 */
public class QueueHelper {

	public static interface HasQueueAction {
		void processAction (String action, JsonObject json) throws Exception;
	}

	private static final Logger LOG = Logger
			.getLogger(QueueHelper.class.getName());

	private static final String ACTION_KEY = "action";
	private static final String REQUEST_KEY = "request";

	public static void enqueue (String url, String action, Request request) {
		enqueue(url, action, request, null);
	}

	public static void enqueue (String url, String action, Request request,
			Date eta) {
		Queue queue = QueueFactory.getDefaultQueue();

		TaskOptions options = TaskOptions.Builder.withMethod(Method.POST)
				.url(url).param(REQUEST_KEY, request.toString())
				.param(ACTION_KEY, action);

		if (eta != null) {
			options.etaMillis(eta.getTime());
		}

		if (LOG.isLoggable(Level.FINE)) {
			LOG.log(Level.FINE,
					"Enquing url [" + url + "] with key [" + REQUEST_KEY + "="
							+ request + "], action [" + ACTION_KEY + "="
							+ action + "]");
		}

		int retry = 3;

		do {
			try {
				queue.add(options);

				// success no need to retry
				retry = 0;
			} catch (TransientFailureException ex) {
				retry--;
			}
		} while (retry > 0);
	}

	/**
	 * @param appointmentSyncServlet
	 * @throws Exception 
	 */
	public static void processGet (HasQueueAction processor)
			throws Exception {
		String appEngineQueue = ContextAwareServlet.REQUEST.get()
				.getHeader("X-AppEngine-QueueName");

		if (LOG.isLoggable(Level.FINE)) {
			LOG.log(Level.FINE,
					String.format("appEngineQueue is [%s]", appEngineQueue));
		}

		boolean isQueue = appEngineQueue != null
				&& "default".toLowerCase().equals(appEngineQueue.toLowerCase());

		if (!isQueue) {
			ContextAwareServlet.RESPONSE.get().setStatus(401);
			ContextAwareServlet.RESPONSE.get().getOutputStream()
					.print("failure");
			LOG.log(Level.WARNING,
					"Attempt to run script directly, this is not permitted");
			return;
		}

		if (LOG.isLoggable(Level.FINE)) {
			LOG.log(Level.FINE, String.format(
					"Call from [%s] allowed to proceed", appEngineQueue));
		}

		String action = ContextAwareServlet.REQUEST.get()
				.getParameter(ACTION_KEY);
		String request = ContextAwareServlet.REQUEST.get()
				.getParameter(REQUEST_KEY);
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Action is [" + action + "] and request is [" + request
					+ "]");
		}

		if (action != null && request != null) {
			try {
				processor.processAction(action, Convert.toJsonObject(request));
			} catch (ServiceException e) {
				if (LOG.isLoggable(Level.WARNING)) {
					LOG.log(Level.WARNING,
							"An error occured processing action [" + action
									+ "]. It will will not be retried",
							e);
				}
			} finally {
				if (LOG.isLoggable(Level.FINE)) {
					LOG.fine("Action [" + action + "] completed");
				}
			}
		}
	}
}
