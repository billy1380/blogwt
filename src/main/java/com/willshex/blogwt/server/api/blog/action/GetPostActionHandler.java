//  
//  GetPostActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PostValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostResponse;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.shared.StatusType;

public final class GetPostActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(GetPostActionHandler.class.getName());

	public GetPostResponse handle (GetPostRequest input) {
		LOG.finer("Entering getPost");
		GetPostResponse output = new GetPostResponse();
		try {
			ApiValidator.notNull(input, GetPostRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			if (input.session != null) {
				try {
					output.session = input.session = SessionValidator
							.lookupAndExtend(input.session, "input.session");

					UserHelper.stripPassword(output.session == null ? null
							: output.session.user);
				} catch (InputValidationException ex) {
					output.session = input.session = null;
				}
			}

			Post post = PostValidator.lookup(input.post, "input.post");

			if (post != null) {
				output.post = PostValidator.viewable(post, output.session,
						"input.post");

				output.post.author = UserServiceProvider.provide()
						.getUser(Long.valueOf(output.post.authorKey.getId()));
				UserHelper.stripSensitive(output.post.author);

				output.post.content = PostServiceProvider.provide()
						.getPostContent(output.post);
			}

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getPost");
		return output;
	}
}