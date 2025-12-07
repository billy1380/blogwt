//
//  RoleValidator.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 18 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.server.ServiceException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RoleValidator extends ApiValidator {
	private static final String TYPE = Role.class.getSimpleName();

	public static Role lookup (Role role, String name)
			throws InputValidationException {
		if (role == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, TYPE + ": " + name);

		boolean isIdLookup = false, isCodeLookup = false, isNameLookup = false;

		if (role.id != null) {
			isIdLookup = true;
		} else if (role.code != null) {
			isCodeLookup = true;
		}

		if (!(isIdLookup || isNameLookup || isCodeLookup))
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNoLookup, TYPE + ": " + name);

		Role lookupRole = null;
		if (isIdLookup) {
			lookupRole = RoleServiceProvider.provide().getRole(role.id);
		} else if (isCodeLookup) {
			lookupRole = RoleServiceProvider.provide().getCodeRole(role.code);
		}

		if (lookupRole == null)
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, TYPE + ": " + name);

		return lookupRole;
	}

	/**
	 * @param roles
	 * @return 
	 * @throws InputValidationException 
	 */
	public static <T extends Iterable<Role>> T lookupAll (T roles, String name)
			throws ServiceException {
		return processAll(false, roles, RoleValidator::lookup, TYPE, name);
	}
}
