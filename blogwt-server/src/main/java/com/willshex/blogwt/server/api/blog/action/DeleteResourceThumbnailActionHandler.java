//  
//  DeleteResourceThumbnailActionHandler.java
//  blogwt
//
//  Created by William Shakour on November 24, 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.Arrays;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.ResourceValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.background.thumbnail.ThumbnailServlet;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceThumbnailRequest;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceThumbnailResponse;
import com.willshex.blogwt.shared.helper.PermissionHelper;

/** Queues the removal of a resource's thumbnail; it appears in the resource a moment later. */
public final class DeleteResourceThumbnailActionHandler
		extends ActionHandler<DeleteResourceThumbnailRequest, DeleteResourceThumbnailResponse> {
	private static final Logger LOG = Logger
			.getLogger(DeleteResourceThumbnailActionHandler.class.getName());
	@Override
	protected void handle (DeleteResourceThumbnailRequest input,
			DeleteResourceThumbnailResponse output) throws Exception {
		ApiValidator.request(input, DeleteResourceThumbnailRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.authorisation(input.session.user,
				Arrays.asList(PermissionServiceProvider.provide()
						.getCodePermission(PermissionHelper.MANAGE_RESOURCES)),
				"input.session.user");

		output.resource = input.resource = ResourceValidator
				.lookup(input.resource, "input.resource");

		ThumbnailServlet.queueDelete(input.resource.id);
	}
	@Override
	protected DeleteResourceThumbnailResponse newOutput () {
		return new DeleteResourceThumbnailResponse();
	}
	@Override
	protected Logger logger () {
		return LOG;
	}
}
