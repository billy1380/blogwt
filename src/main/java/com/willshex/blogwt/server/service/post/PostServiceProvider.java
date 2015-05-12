//  
//  PostServiceProvider.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.post;

import com.spacehopperstudios.service.ServiceDiscovery;

/**
 * 
 * @author William Shakour (billy1380)
 *
 */
public final class PostServiceProvider {

	/**
	 * @return
	 */
	public static IPostService provide () {
		IPostService postService = null;

		if ((postService = (IPostService) ServiceDiscovery
				.getService(IPostService.NAME)) == null) {
			postService = PostServiceFactory.createNewPostService();
			ServiceDiscovery.registerService(postService);
		}

		return postService;
	}

}