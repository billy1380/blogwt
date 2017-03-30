//  
//  GetRatingsActionHandler.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.rating.RatingServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.GetRatingsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetRatingsResponse;
import com.willshex.blogwt.shared.api.datatype.Rating;
import com.willshex.blogwt.shared.api.datatype.RatingSortType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.server.InputValidationException;

public final class GetRatingsActionHandler
		extends ActionHandler<GetRatingsRequest, GetRatingsResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetRatingsActionHandler.class.getName());

	@Override
	public void handle (GetRatingsRequest input, GetRatingsResponse output)
			throws Exception {
		ApiValidator.notNull(input, GetRatingsRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		if (input.session != null) {
			try {
				output.session = input.session = SessionValidator
						.lookupAndExtend(input.session, "input.session");

				UserHelper.stripPassword(
						output.session == null ? null : output.session.user);
			} catch (InputValidationException ex) {
				output.session = input.session = null;
			}
		}

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

			if (output.ratings != null && !output.ratings.isEmpty()) {
				Map<Long, List<Rating>> userRatingLookup = new HashMap<>();
				List<Rating> ratings;
				Long id;

				for (Rating rating : output.ratings) {
					id = Long.valueOf(rating.byKey.getId());

					if ((ratings = userRatingLookup.get(id)) == null) {
						userRatingLookup.put(id, ratings = new ArrayList<>());
					}

					ratings.add(rating);
				}

				List<User> users = UserServiceProvider.provide()
						.getIdUserBatch(userRatingLookup.keySet());

				for (User user : users) {
					UserHelper.stripPassword(user);

					ratings = userRatingLookup.get(user.id);

					for (Rating rating : ratings) {
						rating.by = user;
					}
				}
			}
		}

		output.pager = PagerHelper.moveForward(input.pager);
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