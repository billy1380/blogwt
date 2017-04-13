//
//  UserValidator.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 14 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.willshex.blogwt.server.api.exception.AuthorisationException;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.RoleHelper;
import com.willshex.gson.web.service.server.InputValidationException;

/**
 * @author billy1380
 *
 */
public class UserValidator extends ApiValidator {
	private static final String type = User.class.getSimpleName();

	public static User validate (User user, String name)
			throws InputValidationException {
		boolean foundUsername = false, foundEmail = false;

		if (user.username != null) {
			ApiValidator.validateLength(user.username, 1, 512,
					type + ": " + name + "[" + user.username + "].username");

			foundUsername = true;
		}

		if (user.email != null) {
			ApiValidator.validateLength(user.email, 1, 512,
					type + ": " + name + "[" + user.email + "].email");
			foundEmail = true;
		}

		if (foundUsername) {
			User existingUsernameUser = UserServiceProvider.provide()
					.getUsernameUser(user.username);

			if (existingUsernameUser != null
					&& (user.id == null || (user.id != null
							&& !user.id.equals(existingUsernameUser.id))))
				ApiValidator.throwServiceError(InputValidationException.class,
						ApiError.UsernameInUse,
						"String: " + name + ".username");
		}

		if (foundEmail) {
			User existingEmailUser = UserServiceProvider.provide()
					.getEmailUser(user.email);

			if (existingEmailUser != null
					&& (user.id == null || (user.id != null
							&& !user.id.equals(existingEmailUser.id))))
				ApiValidator.throwServiceError(InputValidationException.class,
						ApiError.EmailInUse, "String: " + name + ".email");
		}

		if (!(foundUsername || foundEmail))
			throwServiceError(InputValidationException.class,
					ApiError.NotEnoughData,
					type + ": no username or email in " + name);

		return user;
	}

	public static <T extends Iterable<User>> T validateAll (T users,
			String name) throws InputValidationException {
		return users;
	}

	public static boolean isAdmin (User user) {
		List<Role> roles = user.roles == null && user.roleKeys != null
				? RoleServiceProvider.provide().getIdRoleBatch(
						PersistenceHelper.keysToIds(user.roleKeys))
				: user.roles;
		return user != null && roles != null
				&& RoleHelper.toLookup(roles).containsKey(RoleHelper.ADMIN);
	}

	public static void authorisation (User user,
			Collection<Permission> requiredPermissions, String name)
			throws AuthorisationException {
		boolean authorised = isAdmin(user);
		List<Permission> permissions = user.permissions == null
				&& user.permissionKeys != null
						? PermissionServiceProvider.provide()
								.getIdPermissionBatch(PersistenceHelper
										.keysToIds(user.permissionKeys))
						: user.permissions;

		if (!authorised && user != null && permissions != null) {
			if (requiredPermissions != null && requiredPermissions.size() > 0) {
				Map<String, Permission> lookup = PermissionHelper
						.toLookup(permissions);
				for (Permission permission : requiredPermissions) {
					if (permission.code != null
							&& lookup.containsKey(permission.code)) {
						authorised = true;
						break;
					}
				}
			}
		}

		if (!authorised)
			throw new AuthorisationException(user, permissions, name);
	}

	public static User lookup (User user, String name)
			throws InputValidationException {
		if (user == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, type + ": " + name);

		boolean isIdLookup = false, isNameLookup = false;

		if (user.id != null) {
			isIdLookup = true;
		} else if (user.username != null) {
			isNameLookup = true;
		}

		if (!(isIdLookup || isNameLookup))
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNoLookup, type + ": " + name);

		User lookupUser = null;
		if (isIdLookup) {
			lookupUser = UserServiceProvider.provide().getUser(user.id);
		} else if (isNameLookup) {
			lookupUser = UserServiceProvider.provide()
					.getUsernameUser(user.username);
		}

		if (lookupUser == null)
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, type + ": " + name);

		return lookupUser;
	}

	/**
	 * @param user
	 * @return
	 */
	public static boolean isSuspended (User user) {
		return false;
	}

}
