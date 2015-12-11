//
//  UserHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import java.util.List;

import com.willshex.blogwt.shared.api.datatype.User;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UserHelper {

	public static final int AVATAR_HEADER_SIZE = 18;
	public static final int AVATAR_SIZE = 35;
	public static final int AVATAR_LARGE_SIZE = 128;

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

	public static User stripPassword (User user) {
		return user == null ? null : user.password(null);
	}

	public static List<User> stripPassword (List<User> users) {
		if (users != null) {
			for (User user : users) {
				stripPassword(user);
			}
		}

		return users;
	}

	public static User stripSensitive (User user) {
		return user == null ? null : user.password(null).email(null);
	}

	public static List<User> stripSensitive (List<User> users) {
		if (users != null) {
			for (User user : users) {
				stripSensitive(user);
			}
		}

		return users;
	}

	public static String name (User user) {
		return user == null ? "unknown" : user.forename + " " + user.surname;
	}

	/**
	 * @param user
	 * @return
	 */
	public static boolean isAdmin (User user) {
		return user.roles == null ? false : RoleHelper.toLookup(user.roles)
				.containsKey(RoleHelper.ADMIN);
	}
}
