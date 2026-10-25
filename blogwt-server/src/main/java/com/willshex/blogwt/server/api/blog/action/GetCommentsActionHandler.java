//
//  GetCommentsActionHandler.java
//  blogwt
//
//  Created by William Shakour on October 24, 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.List;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.CommentValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.comment.CommentServiceProvider;
import com.willshex.blogwt.server.service.comment.ICommentService;
import com.willshex.blogwt.shared.api.blog.call.GetCommentsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetCommentsResponse;
import com.willshex.blogwt.shared.api.datatype.Comment;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.api.datatype.Reaction;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.server.InputValidationException;

public final class GetCommentsActionHandler
		extends ActionHandler<GetCommentsRequest, GetCommentsResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetCommentsActionHandler.class.getName());

	@Override
	protected void handle (GetCommentsRequest input,
			GetCommentsResponse output) throws Exception {
		ApiValidator.request(input, GetCommentsRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		if (input.session != null) {
			try {
				output.session = input.session = SessionValidator
						.lookupCheckAndExtend(input.session, "input.session");

				UserHelper.stripPassword(
						output.session == null ? null : output.session.user);
			} catch (InputValidationException ex) {
				output.session = input.session = null;
			}
		}

		DataType parent = CommentValidator.lookupParent(input.post, input.page,
				"input");

		if (input.pager == null) {
			input.pager = PagerHelper.createDefaultPager();
		}

		ICommentService service = CommentServiceProvider.provide();

		List<Comment> visible = service.getVisibleComments(parent,
				input.session == null ? null : input.session.user,
				input.session != null
						&& UserValidator.isCommentModerator(input.session.user));

		output.comments = service.inflateAll(visible, input.pager.start,
				input.pager.count);

		output.pager = input.pager;
		PagerHelper.updatePager(output.pager, output.comments,
				Integer.valueOf(visible.size()));
	}

	@Override
	public void clearSensitiveFields (GetCommentsResponse output) {
		super.clearSensitiveFields(output);

		if (output.comments != null) {
			for (Comment comment : output.comments) {
				strip(comment);
			}
		}
	}

	static void strip (Comment comment) {
		UserHelper.stripSensitive(comment.owner);

		if (comment.reactions != null) {
			for (Reaction reaction : comment.reactions) {
				UserHelper.stripSensitive(reaction.owner);
			}
		}
	}

	@Override
	protected GetCommentsResponse newOutput () {
		return new GetCommentsResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
