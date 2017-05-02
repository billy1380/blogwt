//
//  RatingValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Mar 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import com.willshex.blogwt.server.service.rating.RatingServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Rating;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.server.ServiceException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RatingValidator extends ApiValidator {
	private static final String TYPE = Rating.class.getSimpleName();
	private static final Processor<Rating> LOOKUP = new Processor<Rating>() {

		@Override
		public Rating process (Rating item, String name)
				throws InputValidationException {
			return lookup(item, name);
		}
	};

	public static Rating validate (Rating rating, String name)
			throws InputValidationException {
		if (rating == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, TYPE + ": " + name);

		rating.by = UserValidator.lookup(rating.by, name + ".by");

		rating.value = notNull(rating.value, Integer.class, name + ".value");

		notNull(rating.subjectId, Long.class, name + ".subjectId");

		notNull(rating.subjectType, String.class, name + ".subjectType");

		validateLength(rating.subjectType, 1, 256, name + ".subjectType");

		if (rating.note != null) {
			validateLength(rating.note, 1, 100000, name + ".note");
		}

		return rating;
	}

	public static Rating lookup (Rating rating, String name)
			throws InputValidationException {
		if (rating == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, TYPE + ": " + name);

		boolean isIdLookup = false;

		if (rating.id != null) {
			isIdLookup = true;
		}

		if (!isIdLookup) throwServiceError(InputValidationException.class,
				ApiError.DataTypeNoLookup, TYPE + ": " + name);

		Rating lookupRating = null;
		if (isIdLookup) {
			lookupRating = RatingServiceProvider.provide().getRating(rating.id);
		}

		if (lookupRating == null)
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, TYPE + ": " + name);

		return lookupRating;
	}

	/**
	 * @param ratings
	 * @return 
	 * @throws InputValidationException 
	 */
	public static <T extends Iterable<Rating>> T lookupAll (T ratings,
			String name) throws ServiceException {
		return processAll(false, ratings, LOOKUP, TYPE, name);
	}
}
