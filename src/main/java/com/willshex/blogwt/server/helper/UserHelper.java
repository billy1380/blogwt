//
//  UserHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 10 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
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
				user.roles = RoleServiceProvider.provide().getIdRoleBatch(
						PersistenceHelper.keysToIds(user.roleKeys));
			}
		}
	}

	public static void populatePermissionsFromKeys (User user) {
		if (user != null) {
			if (user.permissionKeys != null) {
				user.permissions = PermissionServiceProvider.provide()
						.getIdPermissionBatch(PersistenceHelper
								.keysToIds(user.permissionKeys));
			}
		}
	}

	/**
	 * @param email
	 * @return
	 */
	public static String emailAvatar (String email) {
		return "https://secure.gravatar.com/avatar/"
				+ StringUtils.md5Hash(email.trim().toLowerCase());
	}

	public static String ensureEmail (String email, String username) {
		return email == null || email.trim().isEmpty()
				? "user" + username + "@" + ServletHelper
						.constructBaseAddress(ContextAwareServlet.REQUEST.get())
				: email;
	}

	public static interface HasUser<T> {
		Key<User> get (T t);

		void set (T t, User u);
	}

	public static <T> void lookupUsers (Collection<T> c, HasUser<T> u) {
		if (c != null && !c.isEmpty()) {
			Map<Long, List<T>> lookup = new HashMap<>();
			List<T> sub;
			Long id;
			for (T t : c) {
				id = keyToId(u.get(t));

				if ((sub = lookup.get(id)) == null) {
					lookup.put(id, sub = new ArrayList<>());
				}

				sub.add(t);
			}

			List<User> users = UserServiceProvider.provide()
					.getIdUserBatch(lookup.keySet());

			for (User user : users) {
				UserHelper.stripPassword(user);

				sub = lookup.get(user.id);

				for (T t : sub) {
					u.set(t, user);
				}
			}
		}
	}
}
