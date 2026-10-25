//
//  ReactionValidator.java
//  blogwt
//
//  Created by William Shakour on October 24, 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import com.willshex.blogwt.server.service.comment.CommentServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Reaction;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.ReactionHelper;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.server.ServiceException;

public class ReactionValidator extends ApiValidator {
	private static final String TYPE = Reaction.class.getSimpleName();

	public static Reaction validate (Reaction reaction, String name)
			throws ServiceException {
		if (reaction == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, TYPE + ": " + name);

		reaction.parent = CommentValidator.lookupReactionParent(reaction.parent,
				reaction.parentType, name + ".parent");

		notNull(reaction.emoticon, String.class, name + ".emoticon");

		if (!ReactionHelper.isEmoticon(reaction.emoticon))
			throwServiceError(InputValidationException.class,
					ApiError.TokenNoMatch, name + ".emoticon");

		return reaction;
	}

	public static Reaction lookup (Reaction reaction, String name)
			throws InputValidationException {
		if (reaction == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, TYPE + ": " + name);

		if (reaction.id == null) throwServiceError(InputValidationException.class,
				ApiError.DataTypeNoLookup, TYPE + ": " + name);

		Reaction lookupReaction = CommentServiceProvider.provide()
				.getReaction(reaction.id);

		if (lookupReaction == null)
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, TYPE + ": " + name);

		return lookupReaction;
	}
}
