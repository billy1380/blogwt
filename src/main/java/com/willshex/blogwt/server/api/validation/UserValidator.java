//
//  UserValidator.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 14 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import java.util.Collection;

import com.willshex.blogwt.server.api.exception.AuthorisationException;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.json.service.server.InputValidationException;

/**
 * @author billy1380
 *
 */
public class UserValidator extends ApiValidator {
	private static final String type = User.class.getSimpleName();

	public static User validate (User user, String name)
			throws InputValidationException {
		return user;
	}

	public static <T extends Iterable<User>> T validateAll (T users, String name)
			throws InputValidationException {
		return users;
	}

	public static void authorisation (User user, Collection<Role> roles,
			Collection<Permission> permissions, String name)
			throws AuthorisationException {

		for (Role role : roles) {}

		for (Permission permission : permissions) {}
	}

	public static User lookup (User user, String name)
			throws InputValidationException {
		if (user == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);
		return user;
	}

}
