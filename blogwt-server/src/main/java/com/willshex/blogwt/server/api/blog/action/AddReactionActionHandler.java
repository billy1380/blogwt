//
//  AddReactionActionHandler.java
//  blogwt
//
//  Created by William Shakour on October 25, 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.ReactionValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.comment.CommentServiceProvider;
import com.willshex.blogwt.server.service.comment.ICommentService;
import com.willshex.blogwt.shared.api.blog.call.AddReactionRequest;
import com.willshex.blogwt.shared.api.blog.call.AddReactionResponse;
import com.willshex.blogwt.shared.api.datatype.Comment;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.api.datatype.Reaction;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;

/**
 * Adds a reaction of the session user to a comment, post or page
 */
public final class AddReactionActionHandler
		extends ActionHandler<AddReactionRequest, AddReactionResponse> {

	private static final Logger LOG = Logger
			.getLogger(AddReactionActionHandler.class.getName());

	@Override
	protected void handle (AddReactionRequest input,
			AddReactionResponse output) throws Exception {
		ApiValidator.request(input, AddReactionRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.suspended(input.session.user);

		input.reaction = ReactionValidator.validate(input.reaction,
				"input.reaction");

		DataType parent = input.reaction.parent;

		if (parent instanceof Comment
				&& !Boolean.TRUE.equals(((Comment) parent).approved)
				&& !UserValidator.isCommentModerator(input.session.user))
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, "input.reaction.parent");

		ICommentService service = CommentServiceProvider.provide();

		if (service.getReaction(parent, input.session.user,
				input.reaction.emoticon) != null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeExists, "input.reaction");

		Reaction reaction = new Reaction();
		reaction.emoticon = input.reaction.emoticon;
		reaction.owner = input.session.user;
		reaction.parent = parent;
		reaction.parentType = input.reaction.parentType;

		service.addReaction(reaction);

		output.reactions = service.getReactions(parent);
	}

	@Override
	public void clearSensitiveFields (AddReactionResponse output) {
		super.clearSensitiveFields(output);

		GetReactionsActionHandler.strip(output.reactions);
	}

	@Override
	protected AddReactionResponse newOutput () {
		return new AddReactionResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
