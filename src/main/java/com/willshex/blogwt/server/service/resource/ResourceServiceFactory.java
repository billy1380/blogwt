//  
//  ResourceServiceFactory.java
//  blogwt
//
//  Created by William Shakour on May 4, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.resource;

/**
 * 
 * @author William Shakour (billy1380)
 *
 */
final class ResourceServiceFactory {

	/**
	 * @return
	 */
	public static IResourceService createNewResourceService () {
		return new ResourceService();
	}

}