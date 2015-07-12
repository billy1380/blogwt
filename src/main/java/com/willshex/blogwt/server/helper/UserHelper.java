//
//  UserHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 10 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import com.willshex.blogwt.server.service.PersistenceService;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.shared.api.datatype.User;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UserHelper extends com.willshex.blogwt.shared.helper.UserHelper {
	/**
	 * Adds roles and permissions to user object based on roleKeys and permissionKeys respectively 
	 * @param user
	 */
	public static void addRolesAndPermissions (User user) {
		if (user != null) {
			if (user.roleKeys != null) {
				user.roles = RoleServiceProvider.provide().getIdRolesBatch(
						PersistenceService.keysToIds(user.roleKeys));
			}

			if (user.permissionKeys != null) {
				user.permissions = PermissionServiceProvider.provide()
						.getIdPermissionsBatch(
								PersistenceService
										.keysToIds(user.permissionKeys));
			}
		}
	}
}