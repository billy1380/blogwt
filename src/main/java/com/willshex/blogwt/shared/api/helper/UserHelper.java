//
//  UserHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.helper;

import com.willshex.blogwt.shared.api.datatype.User;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UserHelper {

	/**
	 * Returns user forname and surname
	 * @param user
	 * @return
	 */
	public static String handle (User user) {
		return user == null ? "none" : user.forename + " " + user.surname;
	}

	/**
	 * Returns email address as it is read eg. billy1380 @ server dot com
	 * @param user
	 * @return
	 */
	public static String emailDescription (User user) {
		return user.email == null ? "empty" : user.email.replace(".", " dot ")
				.replace("@", " at ");
	}
}
