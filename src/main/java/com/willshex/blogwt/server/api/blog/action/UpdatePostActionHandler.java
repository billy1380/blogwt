//  
//  UpdatePostActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostResponse;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class UpdatePostActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(UpdatePostActionHandler.class.getName());

	public UpdatePostResponse handle (UpdatePostRequest input) {
		LOG.finer("Entering updatePost");
		UpdatePostResponse output = new UpdatePostResponse();
		try {
			ApiValidator.notNull(input, UpdatePostRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator
					.lookupAndExtend(input.session, "input.session");

			input.session.user = UserServiceProvider.provide()
					.getUser(Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user,
					Arrays.asList(PermissionServiceProvider.provide()
							.getCodePermission(PermissionHelper.MANAGE_POSTS)),
					"input.session.user");

			Post updatedPost = input.post;

			input.post = PostValidator.lookup(input.post, "input.post");
			input.post.content = PostServiceProvider.provide()
					.getPostContent(input.post);

			updatedPost = PostValidator.validate(updatedPost, "input.post");

			input.post.commentsEnabled = updatedPost.commentsEnabled;
			input.post.content.body = updatedPost.content.body;
			input.post.listed = updatedPost.listed;
			input.post.summary = updatedPost.summary;

			List<String> removedTags = null;
			if (updatedPost.tags == null) {
				removedTags = input.post.tags;
			} else {
				if (input.post.tags != null) {
					removedTags = new ArrayList<String>();
					for (String tag : input.post.tags) {
						if (!updatedPost.tags.contains(tag)) {
							removedTags.add(tag);
						}
					}
				}
			}

			input.post.tags = updatedPost.tags;
			input.post.title = updatedPost.title;

			// don't change the original publish date
			if (Boolean.TRUE.equals(input.publish)
					&& input.post.published == null) {
				input.post.published = new Date();
			}

			PostServiceProvider.provide().updatePost(input.post, removedTags);

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting updatePost");
		return output;
	}
}