//
//  PermissionHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.PermissionTypeType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PermissionHelper {

	public static final String MANAGE_PERMISSIONS = "MPR";
	public static final String MANAGE_ROLES = "MRO";
	public static final String MANAGE_USERS = "MUS";
	public static final String MANAGE_POSTS = "MPO";
	public static final String MANAGE_PAGES = "MPA";

	public static final String MANAGE_PERMISSIONS_NAME = "Manage Permissions";
	public static final String MANAGE_ROLES_NAME = "Manage Roles";
	public static final String MANAGE_USERS_NAME = "Manage Users";
	public static final String MANAGE_POSTS_NAME = "Manage Posts";
	public static final String MANAGE_PAGES_NAME = "Manage Pages";

	public static final String MANAGE_PERMISSIONS_DESCRIPTION = "allows users to add permissions to users";
	public static final String MANAGE_ROLES_DESCRIPTION = "allows users to add roles to other users as well as add permissions to roles";
	public static final String MANAGE_USERS_DESCRIPTION = "allows users to add and remove users";
	public static final String MANAGE_POSTS_DESCRIPTION = "allows users to add and remove posts";
	public static final String MANAGE_PAGES_DESCRIPTION = "allows users to add pages with links that appear in the header";

	public static Map<String, Permission> toLookup (
			Collection<Permission> permissions) {
		Map<String, Permission> lookup = new HashMap<String, Permission>();

		for (Permission permission : permissions) {
			lookup.put(permission.code, permission);
		}

		return lookup;
	}

	public static Permission createFull (String code, String name,
			String description) {
		return new Permission().code(code).name(name).description(description)
				.type(PermissionTypeType.PermissionTypeTypeUser);
	}

	public static Permission create (String code) {
		return new Permission().code(code);
	}

	public static Collection<Permission> createAll () {
		List<Permission> all = new ArrayList<Permission>();

		all.add(createFull(MANAGE_PERMISSIONS, MANAGE_PERMISSIONS_NAME,
				MANAGE_PERMISSIONS_DESCRIPTION));
		all.add(createFull(MANAGE_ROLES, MANAGE_ROLES_NAME,
				MANAGE_ROLES_DESCRIPTION));
		all.add(createFull(MANAGE_USERS, MANAGE_USERS_NAME,
				MANAGE_USERS_DESCRIPTION));
		all.add(createFull(MANAGE_POSTS, MANAGE_POSTS_NAME,
				MANAGE_POSTS_DESCRIPTION));
		all.add(createFull(MANAGE_PAGES, MANAGE_PAGES_NAME,
				MANAGE_PAGES_DESCRIPTION));

		return all;
	}

}
