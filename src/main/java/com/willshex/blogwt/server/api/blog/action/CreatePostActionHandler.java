//  
//  CreatePostActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PostValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.CreatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.CreatePostResponse;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class CreatePostActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(CreatePostActionHandler.class.getName());

	public CreatePostResponse handle (CreatePostRequest input) {
		LOG.finer("Entering createPost");
		CreatePostResponse output = new CreatePostResponse();
		try {
			ApiValidator.notNull(input, CreatePostRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator
					.lookupAndExtend(input.session, "input.session");

			List<Permission> permissions = new ArrayList<Permission>();
			Permission postPermission = PermissionServiceProvider.provide()
					.getCodePermission(PermissionHelper.MANAGE_POSTS);
			permissions.add(postPermission);

			input.session.user = UserServiceProvider.provide()
					.getUser(Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user, permissions,
					"input.session.user");

			input.post = PostValidator.validate(input.post, "input.post");

			input.post.author = input.session.user;

			if (Boolean.TRUE.equals(input.publish)) {
				input.post.published = new Date();
			}

			input.post.listed = (input.post.listed == null ? Boolean.TRUE
					: input.post.listed);
			input.post.commentsEnabled = (input.post.commentsEnabled == null
					? Boolean.FALSE : input.post.commentsEnabled);

			output.post = PostServiceProvider.provide().addPost(input.post);

			if (output.post != null) {
				output.post.author = UserServiceProvider.provide()
						.getUser(output.post.author.id);
				UserHelper.stripSensitive(output.post.author);
			}

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting createPost");
		return output;
	}
}