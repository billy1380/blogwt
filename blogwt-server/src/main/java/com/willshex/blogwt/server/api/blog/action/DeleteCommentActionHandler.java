//
//  DeleteCommentActionHandler.java
//  blogwt
//
//  Created by William Shakour on October 24, 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.CommentValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.comment.CommentServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.DeleteCommentRequest;
import com.willshex.blogwt.shared.api.blog.call.DeleteCommentResponse;

public final class DeleteCommentActionHandler
		extends ActionHandler<DeleteCommentRequest, DeleteCommentResponse> {

	private static final Logger LOG = Logger
			.getLogger(DeleteCommentActionHandler.class.getName());

	@Override
	protected void handle (DeleteCommentRequest input,
			DeleteCommentResponse output) throws Exception {
		ApiValidator.request(input, DeleteCommentRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		input.comment = CommentValidator.lookup(input.comment,
				"input.comment");

		boolean isOwner = input.comment.ownerKey != null
				&& keyToId(input.comment.ownerKey)
						.equals(input.session.user.id);

		if (!isOwner) {
			UserValidator.commentModeration(input.session.user,
					"input.session.user");
		}

		CommentServiceProvider.provide().deleteComment(input.comment);
	}

	@Override
	protected DeleteCommentResponse newOutput () {
		return new DeleteCommentResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
