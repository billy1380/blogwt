//  
//  GetRatingsActionHandler.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PropertyValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.persistence.batch.Batcher;
import com.willshex.blogwt.server.service.rating.RatingServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.GetRatingsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetRatingsResponse;
import com.willshex.blogwt.shared.api.datatype.Rating;
import com.willshex.blogwt.shared.api.datatype.RatingSortType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.gson.web.service.server.InputValidationException;

public final class GetRatingsActionHandler
		extends ActionHandler<GetRatingsRequest, GetRatingsResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetRatingsActionHandler.class.getName());

	@Override
	public void handle (GetRatingsRequest input, GetRatingsResponse output)
			throws Exception {
		ApiValidator.request(input, GetRatingsRequest.class);
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

		PropertyValidator.ensureTrue(PropertyHelper.RATING_ENABLED);

		if (input.pager == null) {
			input.pager = PagerHelper.createDefaultPager();
		}

		boolean isUserRatings = false, isSubjectRatings = false;

		try {
			input.by = UserValidator.validate(input.by, "input.by");

			isUserRatings = true;
		} catch (Exception ex) {}

		if (!isUserRatings) {
			try {
				ApiValidator.notNull(input.subjectId, Long.class,
						"input.subjectId");
				ApiValidator.notNull(input.subjectType, String.class,
						"input.subjectType");
				ApiValidator.validateLength(input.subjectType, 1, 100,
						"input.subjectType");

				isSubjectRatings = true;
			} catch (Exception ex) {}
		}

		if (!(isUserRatings || isSubjectRatings))
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.NotEnoughData,
					"either User: input.by or Long and Integer: input.subjectId and input.subjectType required");

		if (isUserRatings) {
			output.ratings = RatingServiceProvider.provide().getRatings(
					input.pager.start, input.pager.count,
					RatingSortType.fromString(input.pager.sortBy),
					input.pager.sortDirection);
		} else if (isSubjectRatings) {
			output.ratings = RatingServiceProvider.provide().getRatings(
					input.pager.start, input.pager.count,
					RatingSortType.fromString(input.pager.sortBy),
					input.pager.sortDirection);

			Batcher.<Rating, User> lookup(output.ratings,
					GetRatingsActionHandler::byKey,
					GetRatingsActionHandler::setBy,
					UserServiceProvider.provide());
		}

		output.pager = PagerHelper.moveForward(input.pager);
	}

	private static Key<User> byKey (Rating t) {
		return t.byKey;
	}

	private static void setBy (Rating t, User u) {
		t.by = u;
		UserHelper.stripPassword(u);
	}

	@Override
	protected GetRatingsResponse newOutput () {
		return new GetRatingsResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
