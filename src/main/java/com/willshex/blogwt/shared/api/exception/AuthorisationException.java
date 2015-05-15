//
//  AuthorisationException.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 15 May 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.shared.api.exception;

import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.json.service.server.ServiceException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class AuthorisationException extends ServiceException {

	private static final long serialVersionUID = -5372114641321295027L;

	public AuthorisationException(User user, boolean canAddInvoice, boolean canAddCustomer, boolean canAddUser, boolean canChangeVendor) {
		super(ApiError.AuthorisationFailed.getCode(), ApiError.AuthorisationFailed.getMessage(user.username + " without permission to "
				+ permission(canAddInvoice, canAddCustomer, canAddUser, canChangeVendor)));
	}

	private static String permission(boolean canAddInvoice, boolean canAddCustomer, boolean canAddUser, boolean canChangeVendor) {
		String permission = null;
		if (canAddInvoice) {
			permission = "add new invoice";
		} else if (canAddCustomer) {
			permission = "add new customer";
		} else if (canAddUser) {
			permission = "add new user";
		} else if (canChangeVendor) {
			permission = "change vendor details";
		}

		return permission;
	}

}