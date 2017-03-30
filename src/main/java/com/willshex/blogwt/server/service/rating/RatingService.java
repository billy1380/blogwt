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
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Rating;
import com.willshex.blogwt.shared.api.datatype.RatingSortType;

final class RatingService implements IRatingService {
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
		Query<Rating> query = ofy().load().type(Rating.class);

		if (start != null) {
			query = query.offset(start.intValue());
		}

		if (count != null) {
			query = query.limit(count.intValue());
		}

		if (sortBy != null) {
			String condition = sortBy.toString();

			if (sortDirection != null) {
				switch (sortDirection) {
				case SortDirectionTypeDescending:
					condition = "-" + condition;
					break;
				default:
					break;
				}
			}

			query = query.order(condition);
		}

		return query.list();
	}

}