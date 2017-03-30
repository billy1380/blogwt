//  
//  RatingService.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.rating;

import static com.willshex.blogwt.server.service.persistence.PersistenceService.ofy;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.server.service.persistence.PersistenceService;
import com.willshex.blogwt.server.service.persistence.PersistenceService.ISortable;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Rating;
import com.willshex.blogwt.shared.api.datatype.RatingSortType;
import com.willshex.blogwt.shared.api.datatype.User;

final class RatingService implements IRatingService, ISortable<RatingSortType> {
	public String getName () {
		return NAME;
	}

	@Override
	public Rating getRating (Long id) {
		return ofy().load().type(Rating.class).id(id.longValue()).now();
	}

	@Override
	public Rating addRating (Rating rating) {
		if (rating.created == null) {
			rating.created = new Date();
		}

		if (rating.by != null) {
			rating.byKey = Key.create(rating.by);
		}

		Key<Rating> key = ofy().save().entity(rating).now();
		rating.id = Long.valueOf(key.getId());
		return rating;
	}

	@Override
	public Rating updateRating (Rating rating) {
		if (rating.by != null) {
			rating.byKey = Key.create(rating.by);
		}

		ofy().save().entity(rating).now();

		return rating;
	}

	@Override
	public void deleteRating (Rating rating) {
		ofy().delete().entity(rating).now();
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
		return PersistenceService
				.pagedAndSorted(ofy().load().type(Rating.class), start, count,
						sortBy, this, sortDirection)
				.list();
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
		return PersistenceService
				.pagedAndSorted(ofy().load().type(Rating.class), start, count,
						sortBy, this, sortDirection)
				.filter(map(RatingSortType.RatingSortTypeBy), Key.create(user))
				.list();
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
		return PersistenceService
				.pagedAndSorted(ofy().load().type(Rating.class), start, count,
						sortBy, this, sortDirection)
				.filter(map(RatingSortType.RatingSortTypeSubjectId), subjectId)
				.filter(map(RatingSortType.RatingSortTypeSubjectId),
						subjectType)
				.list();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.persistence.PersistenceService.
	 * ISortable#map(java.lang.Enum) */
	@Override
	public String map (RatingSortType sortBy) {
		String mapped = sortBy.toString();

		if (sortBy == RatingSortType.RatingSortTypeBy) {
			mapped += "Key";
		}

		return mapped;
	}

}