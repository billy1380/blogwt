//
//  ApproveCommentActionHandler.java
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
import com.willshex.blogwt.server.service.comment.ICommentService;
import com.willshex.blogwt.shared.api.blog.call.ApproveCommentRequest;
import com.willshex.blogwt.shared.api.blog.call.ApproveCommentResponse;

public final class ApproveCommentActionHandler extends
		ActionHandler<ApproveCommentRequest, ApproveCommentResponse> {

	private static final Logger LOG = Logger
			.getLogger(ApproveCommentActionHandler.class.getName());

	@Override
	protected void handle (ApproveCommentRequest input,
			ApproveCommentResponse output) throws Exception {
		ApiValidator.request(input, ApproveCommentRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.commentModeration(input.session.user,
				"input.session.user");

		input.comment = CommentValidator.lookup(input.comment,
				"input.comment");

		ICommentService service = CommentServiceProvider.provide();

		input.comment.approved = Boolean.TRUE;
		service.updateComment(input.comment);

		output.comment = service.inflateParent(service.inflate(input.comment));
	}

	@Override
	public void clearSensitiveFields (ApproveCommentResponse output) {
		super.clearSensitiveFields(output);

		GetCommentsActionHandler.strip(output.comment);
	}

	@Override
	protected ApproveCommentResponse newOutput () {
		return new ApproveCommentResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
