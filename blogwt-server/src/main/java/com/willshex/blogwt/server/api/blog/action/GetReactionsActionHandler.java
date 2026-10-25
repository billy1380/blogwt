//
//  GetReactionsActionHandler.java
//  blogwt
//
//  Created by William Shakour on October 25, 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.List;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.CommentValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.comment.CommentServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.GetReactionsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetReactionsResponse;
import com.willshex.blogwt.shared.api.datatype.Comment;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.Reaction;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;

/**
 * The reactions on a post, page or comment, exactly one of them
 */
public final class GetReactionsActionHandler
		extends ActionHandler<GetReactionsRequest, GetReactionsResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetReactionsActionHandler.class.getName());

	@Override
	protected void handle (GetReactionsRequest input,
			GetReactionsResponse output) throws Exception {
		ApiValidator.request(input, GetReactionsRequest.class);
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

		int given = (input.post == null ? 0 : 1) + (input.page == null ? 0 : 1)
				+ (input.comment == null ? 0 : 1);

		if (given != 1)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.NotEnoughData,
					"exactly one of input.post, input.page or input.comment");

		DataType parent;
		String type;

		if (input.post != null) {
			parent = input.post;
			type = Post.class.getSimpleName();
		} else if (input.page != null) {
			parent = input.page;
			type = Page.class.getSimpleName();
		} else {
			parent = input.comment;
			type = Comment.class.getSimpleName();
		}

		parent = CommentValidator.lookupReactionParent(parent, type,
				"input");

		output.reactions = CommentServiceProvider.provide()
				.getReactions(parent);
	}

	@Override
	public void clearSensitiveFields (GetReactionsResponse output) {
		super.clearSensitiveFields(output);

		strip(output.reactions);
	}

	static void strip (List<Reaction> reactions) {
		if (reactions != null) {
			for (Reaction reaction : reactions) {
				UserHelper.stripSensitive(reaction.owner);
			}
		}
	}

	@Override
	protected GetReactionsResponse newOutput () {
		return new GetReactionsResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
