//
//  AddCommentActionHandler.java
//  blogwt
//
//  Created by William Shakour on October 24, 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.CommentValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.comment.CommentServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.AddCommentRequest;
import com.willshex.blogwt.shared.api.blog.call.AddCommentResponse;
import com.willshex.blogwt.shared.api.datatype.Comment;
import com.willshex.blogwt.shared.api.datatype.DataType;

public final class AddCommentActionHandler
		extends ActionHandler<AddCommentRequest, AddCommentResponse> {

	private static final Logger LOG = Logger
			.getLogger(AddCommentActionHandler.class.getName());

	@Override
	protected void handle (AddCommentRequest input, AddCommentResponse output)
			throws Exception {
		ApiValidator.request(input, AddCommentRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.suspended(input.session.user);

		DataType parent = CommentValidator.lookupParent(input.post, input.page,
				"input");

		input.comment = CommentValidator.validate(input.comment,
				"input.comment");

		Comment comment = new Comment();
		comment.body = input.comment.body;
		comment.owner = input.session.user;
		comment.parent = parent;
		comment.parentType = parent.getClass().getSimpleName();
		comment.approved = Boolean.valueOf(!CommentValidator.isModerated(parent)
				|| UserValidator.isCommentModerator(input.session.user));

		output.comment = CommentServiceProvider.provide().addComment(comment);
		CommentServiceProvider.provide().inflate(output.comment);
		output.comment.parent = null;
	}

	@Override
	public void clearSensitiveFields (AddCommentResponse output) {
		super.clearSensitiveFields(output);

		GetCommentsActionHandler.strip(output.comment);
	}

	@Override
	protected AddCommentResponse newOutput () {
		return new AddCommentResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
