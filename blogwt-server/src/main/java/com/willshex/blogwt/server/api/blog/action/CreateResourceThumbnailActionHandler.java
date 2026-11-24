//  
//  CreateResourceThumbnailActionHandler.java
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
import com.willshex.blogwt.shared.api.blog.call.CreateResourceThumbnailRequest;
import com.willshex.blogwt.shared.api.blog.call.CreateResourceThumbnailResponse;
import com.willshex.blogwt.shared.helper.PermissionHelper;

/** Queues the making of a resource's thumbnail; it appears in the resource a moment later. */
public final class CreateResourceThumbnailActionHandler
		extends ActionHandler<CreateResourceThumbnailRequest, CreateResourceThumbnailResponse> {
	private static final Logger LOG = Logger
			.getLogger(CreateResourceThumbnailActionHandler.class.getName());
	@Override
	protected void handle (CreateResourceThumbnailRequest input,
			CreateResourceThumbnailResponse output) throws Exception {
		ApiValidator.request(input, CreateResourceThumbnailRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.authorisation(input.session.user,
				Arrays.asList(PermissionServiceProvider.provide()
						.getCodePermission(PermissionHelper.MANAGE_RESOURCES)),
				"input.session.user");

		output.resource = input.resource = ResourceValidator
				.lookup(input.resource, "input.resource");

		ThumbnailServlet.queueCreate(input.resource.id);
	}
	@Override
	protected CreateResourceThumbnailResponse newOutput () {
		return new CreateResourceThumbnailResponse();
	}
	@Override
	protected Logger logger () {
		return LOG;
	}
}
