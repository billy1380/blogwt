//
//  AuthorisationException.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 15 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.exception;

import java.util.Collection;

import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.json.service.server.ServiceException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class AuthorisationException extends ServiceException {

	private static final long serialVersionUID = -5372114641321295027L;

	public AuthorisationException (User user,
			Collection<Permission> permissions, String fieldName) {
		super(ApiError.AuthorisationFailed.getCode(),
				ApiError.AuthorisationFailed.getMessage(user == null ? "null"
						: user.username + " without permission(s) ["
								+ describe(permissions) + "]: " + fieldName));
	}

	public static String describe (Collection<Permission> permissions) {
		StringBuffer buffer = new StringBuffer();

		if (permissions == null || permissions.isEmpty()) {
			buffer.append("administrator");
		} else {
			for (Permission permission : permissions) {
				if (buffer.length() != 0) {
					buffer.append(", ");
				}

				buffer.append(permission.code);
			}
		}

		return buffer.toString();
	}
}