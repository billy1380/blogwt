//
//  DeleteReactionActionHandler.java
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
import com.willshex.blogwt.server.api.validation.ReactionValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.comment.CommentServiceProvider;
import com.willshex.blogwt.server.service.comment.ICommentService;
import com.willshex.blogwt.shared.api.blog.call.DeleteReactionRequest;
import com.willshex.blogwt.shared.api.blog.call.DeleteReactionResponse;

public final class DeleteReactionActionHandler
		extends ActionHandler<DeleteReactionRequest, DeleteReactionResponse> {

	private static final Logger LOG = Logger
			.getLogger(DeleteReactionActionHandler.class.getName());

	@Override
	protected void handle (DeleteReactionRequest input,
			DeleteReactionResponse output) throws Exception {
		ApiValidator.request(input, DeleteReactionRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		input.reaction = ReactionValidator.lookup(input.reaction,
				"input.reaction");

		boolean isOwner = input.reaction.ownerKey != null
				&& keyToId(input.reaction.ownerKey)
						.equals(input.session.user.id);

		if (!isOwner) {
			UserValidator.commentModeration(input.session.user,
					"input.session.user");
		}

		ICommentService service = CommentServiceProvider.provide();

		service.deleteReaction(input.reaction);

		output.reactions = service.getReactions(input.reaction.parentKey);
	}

	@Override
	public void clearSensitiveFields (DeleteReactionResponse output) {
		super.clearSensitiveFields(output);

		GetReactionsActionHandler.strip(output.reactions);
	}

	@Override
	protected DeleteReactionResponse newOutput () {
		return new DeleteReactionResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
