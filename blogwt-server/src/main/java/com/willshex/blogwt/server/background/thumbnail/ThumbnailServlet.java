//
//  ThumbnailServlet.java
//  blogwt
//
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.background.thumbnail;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;

import com.google.appengine.api.images.ImagesServiceFailureException;
import com.google.gson.JsonObject;
import com.willshex.blogwt.server.background.thumbnail.input.ThumbnailAction;
import com.willshex.blogwt.server.helper.QueueHelper;
import com.willshex.blogwt.server.helper.QueueHelper.HasQueueAction;
import com.willshex.blogwt.server.helper.ThumbnailHelper;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.server.ContextAwareServlet;

/** Makes and removes resource thumbnails from the default queue, one resource to a message. */
@WebServlet(name = "Thumbnail", description = "Makes and removes resource thumbnails", urlPatterns = {
		ThumbnailServlet.URL, ThumbnailServlet.URL + "/*" })
@ServletSecurity(value = @HttpConstraint(rolesAllowed = "admin"))
public class ThumbnailServlet extends ContextAwareServlet
		implements HasQueueAction {

	private static final Logger LOG = Logger
			.getLogger(ThumbnailServlet.class.getName());

	public static final String URL = "/thumbnail";

	private static final String CREATE_ACTION = "CreateThumbnail";
	private static final String DELETE_ACTION = "DeleteThumbnail";

	@Override
	protected void doPost () throws ServletException, IOException {
		doGet();
	}

	@Override
	protected void doGet () throws ServletException, IOException {
		super.doGet();

		try {
			QueueHelper.processGet(this);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void processAction (String action, JsonObject json)
			throws Exception {
		ThumbnailAction input = new ThumbnailAction();
		input.fromJson(json);

		Resource resource = input.resourceId == null ? null
				: ResourceServiceProvider.provide()
						.getResource(input.resourceId);

		if (resource == null) {
			LOG.log(Level.WARNING, "No resource [" + input.resourceId
					+ "] to " + action);

			return;
		}

		if (CREATE_ACTION.equals(action)) {
			try {
				if (!ThumbnailHelper.create(resource)) {
					LOG.log(Level.INFO, "Resource [" + input.resourceId
							+ "] has no thumbnail to make");
				}
			} catch (ImagesServiceFailureException
					| IllegalArgumentException e) {
				// not a decodable image: retrying cannot help
				LOG.log(Level.WARNING, "Resource [" + input.resourceId
						+ "] could not be scaled", e);
			}
		} else if (DELETE_ACTION.equals(action)) {
			ThumbnailHelper.delete(resource);
		}
	}

	/** Has the resource's thumbnail made, or made again. */
	public static void queueCreate (Long resourceId) {
		QueueHelper.enqueue(URL, CREATE_ACTION,
				new ThumbnailAction().resourceId(resourceId));
	}

	/** Has the resource's thumbnail removed. */
	public static void queueDelete (Long resourceId) {
		QueueHelper.enqueue(URL, DELETE_ACTION,
				new ThumbnailAction().resourceId(resourceId));
	}

}
