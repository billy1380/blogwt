//  
//  IRatingService.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.rating;

import java.util.List;

import com.willshex.blogwt.server.service.ISortable;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Rating;
import com.willshex.blogwt.shared.api.datatype.RatingSortType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.service.IService;

public interface IRatingService extends IService, ISortable<RatingSortType> {

	public static final String NAME = "blogwt.rating";

	/**
	* @param id
	* @return
	*/
	public Rating getRating (Long id);

	/**
	* @param rating
	* @return
	*/
	public Rating addRating (Rating rating);

	/**
	* @param rating
	* @return
	*/
	public Rating updateRating (Rating rating);

	/**
	* @param rating
	*/
	public void deleteRating (Rating rating);

	/**
	 * Get Ratings
	 * 
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Rating> getRatings (Integer start, Integer count,
			RatingSortType sortBy, SortDirectionType sortDirection);

	/**
	 * Get user ratings
	 * 
	 * @param user
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Rating> getUserRatings (User user, Integer start, Integer count,
			RatingSortType sortBy, SortDirectionType sortDirection);

	/**
	 * Get subject ratings
	 * @param subjectId
	 * @param subjectType
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Rating> getSubjectRatings (Long subjectId, String subjectType,
			Integer start, Integer count, RatingSortType sortBy,
			SortDirectionType sortDirection);

	public default String map (RatingSortType sortBy) {
		String mapped = sortBy.toString();

		if (sortBy == RatingSortType.RatingSortTypeBy) {
			mapped += "Key";
		}

		return mapped;
	}

}