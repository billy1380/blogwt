//  
//  RatingServiceFactory.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.rating;

final class RatingServiceFactory {

	/**
	* @return
	*/
	public static IRatingService createNewRatingService () {
		return new RatingService();
	}

}