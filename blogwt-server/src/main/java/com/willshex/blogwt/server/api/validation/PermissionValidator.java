//
//  PermissionValidator.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 18 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.server.ServiceException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PermissionValidator extends ApiValidator {
	private static final String TYPE = Permission.class.getSimpleName();

	public static Permission lookup (Permission permission, String name)
			throws InputValidationException {
		if (permission == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, TYPE + ": " + name);

		boolean isIdLookup = false, isCodeLookup = false, isNameLookup = false;

		if (permission.id != null) {
			isIdLookup = true;
		} else if (permission.code != null) {
			isCodeLookup = true;
		}

		if (!(isIdLookup || isNameLookup || isCodeLookup))
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNoLookup, TYPE + ": " + name);

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
					ApiError.DataTypeNotFound, TYPE + ": " + name);

		return lookupPermission;
	}

	/**
	 * @param permissions
	 * @return 
	 * @throws InputValidationException 
	 */
	public static <T extends Iterable<Permission>> T lookupAll (T permissions,
			String name) throws ServiceException {
		return processAll(false, permissions, PermissionValidator::lookup, TYPE,
				name);
	}
}
