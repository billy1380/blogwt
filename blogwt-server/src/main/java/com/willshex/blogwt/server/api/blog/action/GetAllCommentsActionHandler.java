//
//  GetAllCommentsActionHandler.java
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
import com.willshex.blogwt.server.api.validation.PageValidator;
import com.willshex.blogwt.server.api.validation.PostValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.comment.CommentServiceProvider;
import com.willshex.blogwt.server.service.comment.ICommentService;
import com.willshex.blogwt.shared.api.blog.call.GetAllCommentsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetAllCommentsResponse;
import com.willshex.blogwt.shared.api.datatype.Comment;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.server.InputValidationException;

/**
 * Every comment, newest first, for moderators
 */
public final class GetAllCommentsActionHandler extends
		ActionHandler<GetAllCommentsRequest, GetAllCommentsResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetAllCommentsActionHandler.class.getName());

	@Override
	protected void handle (GetAllCommentsRequest input,
			GetAllCommentsResponse output) throws Exception {
		ApiValidator.request(input, GetAllCommentsRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.commentModeration(input.session.user,
				"input.session.user");

		if (input.post != null && input.page != null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.NotEnoughData,
					"at most one of input.post or input.page");

		DataType parent = null;

		if (input.post != null) {
			parent = PostValidator.lookup(input.post, "input.post");
		} else if (input.page != null) {
			parent = PageValidator.lookup(input.page, "input.page");
		}

		User owner = input.owner == null ? null
				: UserValidator.lookup(input.owner, "input.owner");

		if (input.pager == null) {
			input.pager = PagerHelper.createDefaultPager();
		}

		ICommentService service = CommentServiceProvider.provide();

		List<Comment> comments = service.getComments(parent, owner,
				input.pending);

		output.comments = service.inflateAll(comments, input.pager.start,
				input.pager.count);

		for (Comment comment : output.comments) {
			service.inflateParent(comment);
		}

		output.pager = input.pager;
		PagerHelper.updatePager(output.pager, output.comments,
				Integer.valueOf(comments.size()));
	}

	@Override
	public void clearSensitiveFields (GetAllCommentsResponse output) {
		super.clearSensitiveFields(output);

		if (output.comments != null) {
			for (Comment comment : output.comments) {
				GetCommentsActionHandler.strip(comment);
			}
		}
	}

	@Override
	protected GetAllCommentsResponse newOutput () {
		return new GetAllCommentsResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
