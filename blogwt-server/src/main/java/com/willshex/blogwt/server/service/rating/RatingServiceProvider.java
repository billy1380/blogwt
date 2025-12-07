//  
//  RatingServiceProvider.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.rating;

import com.willshex.service.ServiceDiscovery;

final public class RatingServiceProvider {

	/**
	* @return
	*/
	public static IRatingService provide () {
		IRatingService ratingService = null;

		if ((ratingService = (IRatingService) ServiceDiscovery
				.getService(IRatingService.NAME)) == null) {
			ratingService = RatingServiceFactory.createNewRatingService();
			ServiceDiscovery.registerService(ratingService);
		}

		return ratingService;
	}

}