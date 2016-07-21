//  
//  DeletePostActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.ArrayList;
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
import com.willshex.blogwt.shared.api.blog.call.DeletePostRequest;
import com.willshex.blogwt.shared.api.blog.call.DeletePostResponse;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.helper.PermissionHelper;

public final class DeletePostActionHandler
		extends ActionHandler<DeletePostRequest, DeletePostResponse> {
	private static final Logger LOG = Logger
			.getLogger(DeletePostActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (DeletePostRequest input, DeletePostResponse output)
			throws Exception {
		ApiValidator.notNull(input, DeletePostRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupAndExtend(input.session, "input.session");

		input.session.user = UserServiceProvider.provide()
				.getUser(Long.valueOf(input.session.userKey.getId()));

		List<Permission> permissions = new ArrayList<Permission>();
		Permission postPermission = PermissionServiceProvider.provide()
				.getCodePermission(PermissionHelper.MANAGE_POSTS);
		permissions.add(postPermission);

		UserValidator.authorisation(input.session.user, permissions,
				"input.session.user");

		input.post = PostValidator.lookup(input.post, "input.post");

		PostServiceProvider.provide().deletePost(input.post);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected DeletePostResponse newOutput () {
		return new DeletePostResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}

}