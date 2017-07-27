//  
//  RatingService.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.rating;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Rating;
import com.willshex.blogwt.shared.api.datatype.RatingSortType;
import com.willshex.blogwt.shared.api.datatype.User;

final class RatingService implements IRatingService {
	public String getName () {
		return NAME;
	}

	@Override
	public Rating getRating (Long id) {
		return load().id(id.longValue()).now();
	}

	private LoadType<Rating> load () {
		return provide().load().type(Rating.class);
	}

	@Override
	public Rating addRating (Rating rating) {
		if (rating.created == null) {
			rating.created = new Date();
		}

		if (rating.by != null) {
			rating.byKey = Key.create(rating.by);
		}

		Key<Rating> key = provide().save().entity(rating).now();
		rating.id = keyToId(key);

		return rating;
	}

	@Override
	public Rating updateRating (Rating rating) {
		if (rating.by != null) {
			rating.byKey = Key.create(rating.by);
		}

		provide().save().entity(rating).now();

		return rating;
	}

	@Override
	public void deleteRating (Rating rating) {
		provide().delete().entity(rating).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.rating.IRatingService#getRatings(java.
	 * lang.Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.RatingSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Rating> getRatings (Integer start, Integer count,
			RatingSortType sortBy, SortDirectionType sortDirection) {
		return PersistenceHelper.pagedAndSorted(load(), start, count, sortBy,
				this, sortDirection);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.rating.IRatingService#getUserRatings(
	 * com.willshex.blogwt.shared.api.datatype.User, java.lang.Integer,
	 * java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.RatingSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Rating> getUserRatings (User user, Integer start, Integer count,
			RatingSortType sortBy, SortDirectionType sortDirection) {
		return PersistenceHelper.pagedAndSorted(
				load().filter(map(RatingSortType.RatingSortTypeBy), user),
				start, count, sortBy, this, sortDirection);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.rating.IRatingService#
	 * getSubjectRatings(java.lang.Long, java.lang.String, java.lang.Integer,
	 * java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.RatingSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Rating> getSubjectRatings (Long subjectId, String subjectType,
			Integer start, Integer count, RatingSortType sortBy,
			SortDirectionType sortDirection) {
		return PersistenceHelper.pagedAndSorted(
				load().filter(map(RatingSortType.RatingSortTypeSubjectId),
						subjectId)
						.filter(map(RatingSortType.RatingSortTypeSubjectType),
								subjectType),
				start, count, sortBy, this, sortDirection);
	}

}