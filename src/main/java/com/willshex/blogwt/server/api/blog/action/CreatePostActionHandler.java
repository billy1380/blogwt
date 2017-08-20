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

import com.willshex.blogwt.server.api.ActionHandler;
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

public final class CreatePostActionHandler
		extends ActionHandler<CreatePostRequest, CreatePostResponse> {
	private static final Logger LOG = Logger
			.getLogger(CreatePostActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (CreatePostRequest input, CreatePostResponse output)
			throws Exception {
		ApiValidator.request(input, CreatePostRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		List<Permission> permissions = new ArrayList<Permission>();
		Permission postPermission = PermissionServiceProvider.provide()
				.getCodePermission(PermissionHelper.MANAGE_POSTS);
		permissions.add(postPermission);

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
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected CreatePostResponse newOutput () {
		return new CreatePostResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.api.ActionHandler#clearSensitiveFields(com.
	 * willshex.blogwt.shared.api.Response) */
	@Override
	public void clearSensitiveFields (CreatePostResponse output) {
		super.clearSensitiveFields(output);

		if (output.post != null) {
			output.post.author = UserServiceProvider.provide()
					.getUser(output.post.author.id);
			UserHelper.stripSensitive(output.post.author);
		}
	}
}