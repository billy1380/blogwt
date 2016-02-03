//  
//  UserServiceProvider.java
//  blogwt
//
//  Created by William Shakour on 12 May 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.user;

import com.willshex.service.ServiceDiscovery;

/**
 * 
 * @author William Shakour (billy1380)
 *
 */
final public class UserServiceProvider {

	/**
	 * @return
	 */
	public static IUserService provide() {
		IUserService userService = null;

		if ((userService = (IUserService) ServiceDiscovery.getService(IUserService.NAME)) == null) {
			userService = UserServiceFactory.createNewUserService();
			ServiceDiscovery.registerService(userService);
		}

		return userService;
	}

}