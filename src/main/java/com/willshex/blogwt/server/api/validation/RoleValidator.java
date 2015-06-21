//
//  RoleValidator.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 18 May 2015.
//  Copyright © 2015 WillShex Limited Ltd. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import java.util.ArrayList;
import java.util.List;

import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.json.service.server.InputValidationException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RoleValidator {
	private static final String type = Role.class.getSimpleName();

	public static Role lookup (Role role, String name)
			throws InputValidationException {
		if (role == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);

		boolean isIdLookup = false, isCodeLookup = false, isNameLookup = false;

		if (role.id != null) {
			isIdLookup = true;
		} else if (role.code != null) {
			isCodeLookup = true;
		}

		if (!(isIdLookup || isNameLookup || isCodeLookup))
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNoLookup, type + ": " + name);

		Role lookupRole = null;
		if (isIdLookup) {
			lookupRole = RoleServiceProvider.provide().getRole(role.id);
		} else if (isCodeLookup) {
			lookupRole = RoleServiceProvider.provide().getCodeRole(role.code);
		}

		if (lookupRole == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, type + ": " + name);

		return lookupRole;
	}

	/**
	 * @param roles
	 * @return 
	 * @throws InputValidationException 
	 */
	public static List<Role> lookupAll (List<Role> roles, String name)
			throws InputValidationException {
		if (roles == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + "[]: " + name);

		List<Role> lookupRoles = new ArrayList<Role>();

		for (Role role : roles) {
			lookupRoles.add(lookup(role, name + "[n]"));
		}

		return lookupRoles;
	}
}
