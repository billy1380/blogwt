//
//  UserHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 10 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.server.ContextAwareServlet;
import com.willshex.utility.StringUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UserHelper extends com.willshex.blogwt.shared.helper.UserHelper {
	/**
	 * Adds roles and permissions to user object based on roleKeys and permissionKeys respectively 
	 * @param user
	 */
	public static void populateRolesAndPermissionsFromKeys (User user) {
		populateRolesFromKeys(user);
		populatePermissionsFromKeys(user);
	}

	public static void populateRolesFromKeys (User user) {
		if (user != null) {
			if (user.roleKeys != null) {
				user.roles = PersistenceHelper.batchLookup(
						RoleServiceProvider.provide(), user.roleKeys);
			}
		}
	}

	public static void populatePermissionsFromKeys (User user) {
		if (user != null) {
			if (user.permissionKeys != null) {
				user.permissions = PersistenceHelper.batchLookup(
						PermissionServiceProvider.provide(),
						user.permissionKeys);
			}
		}
	}

	/**
	 * @param email
	 * @return
	 */
	public static String emailGravatar (String email) {
		return "https://secure.gravatar.com/avatar/"
				+ StringUtils.md5Hash(email.trim().toLowerCase());
	}

	public static String ensureEmail (String email, String username) {
		return email == null || email.trim().isEmpty() ? "user" + username + "@"
				+ ServletHelper
						.constructBaseAddress(ContextAwareServlet.REQUEST.get())
				: email;
	}
}
