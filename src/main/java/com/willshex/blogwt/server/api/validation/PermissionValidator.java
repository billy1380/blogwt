//
//  PermissionValidator.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 18 May 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import java.util.ArrayList;
import java.util.List;

import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.json.service.server.InputValidationException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PermissionValidator {
	private static final String type = Permission.class.getSimpleName();

	public static Permission lookup (Permission permission, String name)
			throws InputValidationException {
		if (permission == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);

		boolean isIdLookup = false, isCodeLookup = false, isNameLookup = false;

		if (permission.id != null) {
			isIdLookup = true;
		} else if (permission.code != null) {
			isCodeLookup = true;
		}

		if (!(isIdLookup || isNameLookup || isCodeLookup))
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNoLookup, type + ": " + name);

		Permission lookupPermission = null;
		if (isIdLookup) {
			lookupPermission = PermissionServiceProvider.provide()
					.getPermission(permission.id);
		} else if (isCodeLookup) {
			lookupPermission = PermissionServiceProvider.provide()
					.getCodePermission(permission.code);
		}

		if (lookupPermission == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, type + ": " + name);

		return lookupPermission;
	}

	/**
	 * @param permissions
	 * @return 
	 * @throws InputValidationException 
	 */
	public static List<Permission> lookupAll (List<Permission> permissions,
			String name) throws InputValidationException {
		if (permissions == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + "[]: " + name);

		List<Permission> lookupPermissions = new ArrayList<Permission>();

		for (Permission permission : permissions) {
			lookupPermissions.add(lookup(permission, name + "[n]"));
		}

		return lookupPermissions;
	}
}
