//  
//  PostServiceFactory.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.post;

/**
 * 
 * @author William Shakour (billy1380)
 *
 */
final class PostServiceFactory {

	/**
	 * @return
	 */
	public static IPostService createNewPostService() {
		return new PostService();
	}

}