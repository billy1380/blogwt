//  
//  SubmitRatingActionHandler.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PropertyValidator;
import com.willshex.blogwt.server.api.validation.RatingValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.rating.RatingServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.SubmitRatingRequest;
import com.willshex.blogwt.shared.api.blog.call.SubmitRatingResponse;
import com.willshex.blogwt.shared.helper.PropertyHelper;

public final class SubmitRatingActionHandler
		extends ActionHandler<SubmitRatingRequest, SubmitRatingResponse> {

	private static final Logger LOG = Logger
			.getLogger(SubmitRatingActionHandler.class.getName());

	@Override
	protected void handle (SubmitRatingRequest input,
			SubmitRatingResponse output) throws Exception {
		ApiValidator.notNull(input, SubmitRatingRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		PropertyValidator.ensureTrue(PropertyHelper.RATING_ENABLED,
				PropertyHelper.RATING_ENABLED);

		if (input.rating != null && input.rating.by == null) {
			input.rating.by = input.session.user;
		}

		input.rating = RatingValidator.validate(input.rating, "input.rating");

		output.rating = RatingServiceProvider.provide().addRating(input.rating);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.api.ActionHandler#clearSensitiveFields(com.
	 * willshex.blogwt.shared.api.Response) */
	@Override
	public void clearSensitiveFields (SubmitRatingResponse output) {
		super.clearSensitiveFields(output);

		UserHelper.stripSensitive(output.rating.by);
	}

	@Override
	protected SubmitRatingResponse newOutput () {
		return new SubmitRatingResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}