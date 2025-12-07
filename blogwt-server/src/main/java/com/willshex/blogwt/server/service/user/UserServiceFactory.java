//  
//  UserServiceFactory.java
//  blogwt
//
//  Created by William Shakour on 12 May 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.user;

/**
 * @author William Shakour (billy1380)
 *
 */
final class UserServiceFactory {

	/**
	* @return
	*/
	public static IUserService createNewUserService () {
		return new UserService();
	}

}