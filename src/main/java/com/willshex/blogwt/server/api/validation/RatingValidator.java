//
//  RatingValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Mar 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import java.util.ArrayList;
import java.util.List;

import com.willshex.blogwt.server.service.rating.RatingServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Rating;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RatingValidator {
	private static final String type = Rating.class.getSimpleName();

	public static Rating validate (Rating rating, String name)
			throws InputValidationException {
		if (rating == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);

		rating.by = UserValidator.lookup(rating.by, name + ".by");

		rating.value = ApiValidator.notNull(rating.value, Integer.class,
				name + ".value");

		ApiValidator.notNull(rating.subjectId, Long.class, name + ".subjectId");

		ApiValidator.notNull(rating.subjectType, String.class,
				name + ".subjectType");

		ApiValidator.validateLength(rating.subjectType, 1, 256,
				name + ".subjectType");

		if (rating.note != null) {
			ApiValidator.validateLength(rating.note, 1, 100000, name + ".note");
		}

		return rating;
	}

	public static Rating lookup (Rating rating, String name)
			throws InputValidationException {
		if (rating == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);

		boolean isIdLookup = false;

		if (rating.id != null) {
			isIdLookup = true;
		}

		if (!isIdLookup)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNoLookup, type + ": " + name);

		Rating lookupRating = null;
		if (isIdLookup) {
			lookupRating = RatingServiceProvider.provide().getRating(rating.id);
		}

		if (lookupRating == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, type + ": " + name);

		return lookupRating;
	}

	/**
	 * @param ratings
	 * @return 
	 * @throws InputValidationException 
	 */
	public static List<Rating> lookupAll (List<Rating> ratings, String name)
			throws InputValidationException {
		if (ratings == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + "[]: " + name);

		List<Rating> lookupRatings = new ArrayList<Rating>();

		for (Rating rating : ratings) {
			lookupRatings.add(lookup(rating, name + "[n]"));
		}

		return lookupRatings;
	}
}
