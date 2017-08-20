//  
//  ChangePasswordActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordResponse;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;

public final class ChangePasswordActionHandler
		extends ActionHandler<ChangePasswordRequest, ChangePasswordResponse> {
	private static final Logger LOG = Logger
			.getLogger(ChangePasswordActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (ChangePasswordRequest input,
			ChangePasswordResponse output) throws Exception {
		ApiValidator.request(input, ChangePasswordRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		ApiValidator.notNull(input.changedPassword, String.class,
				"input.changedPassword");

		if (input.session != null) {
			try {
				output.session = input.session = SessionValidator
						.lookupCheckAndExtend(input.session, "input.session");
			} catch (InputValidationException ex) {
				output.session = input.session = null;
			}
		}

		//			// if not the logged in user
		//			if (input.user.id.longValue() != input.session.userKey.getId()) {
		//				List<Role> roles = new ArrayList<Role>();
		//				roles.add(RoleHelper.createAdmin());
		//
		//				List<Permission> permissions = new ArrayList<Permission>();
		//				Permission postPermission = PermissionServiceProvider.provide()
		//						.getCodePermission(PermissionHelper.MANAGE_USERS);
		//				permissions.add(postPermission);
		//
		//				UserValidator.authorisation(input.session.user, roles,
		//						permissions, "input.session.user");
		//			}

		boolean isExistingPassword = false, isActionCode = false;

		if (input.resetCode != null && input.resetCode.length() > 0) {
			isActionCode = true;
		}

		if (input.password != null && input.password.length() > 0) {
			isExistingPassword = true;
		}

		if (!(isActionCode || isExistingPassword))
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull,
					"String: input.password or input.resetCode");

		User user = null;

		if (isActionCode) {
			input.resetCode = UserValidator.validateToken(input.resetCode,
					"input.resetCode");
			user = UserServiceProvider.provide()
					.getActionCodeUser(input.resetCode);

			if (user == null)
				ApiValidator.throwServiceError(InputValidationException.class,
						ApiError.DataTypeNotFound, "String: input.resetToken");

			user.actionCode = null;
		}

		if (isExistingPassword && !isActionCode) {
			user = input.session.user;

			if (!UserServiceProvider.provide().verifyPassword(user,
					input.password))
				ApiValidator.throwServiceError(InputValidationException.class,
						ApiError.AuthenticationFailedBadPassword,
						"String: input.password");
		}

		user.password = UserServiceProvider.provide()
				.generatePassword(input.changedPassword);
		UserServiceProvider.provide().updateUser(user);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected ChangePasswordResponse newOutput () {
		return new ChangePasswordResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}
}