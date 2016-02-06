//  
//  ChangePasswordActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordResponse;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.shared.StatusType;

public final class ChangePasswordActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(ChangePasswordActionHandler.class.getName());

	public ChangePasswordResponse handle (ChangePasswordRequest input) {
		LOG.finer("Entering changePassword");
		ChangePasswordResponse output = new ChangePasswordResponse();
		try {
			ApiValidator.notNull(input, ChangePasswordRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			ApiValidator.notNull(input.changedPassword, String.class,
					"input.changedPassword");

			if (input.session != null) {
				try {
					output.session = input.session = SessionValidator
							.lookupAndExtend(input.session, "input.session");
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

				if (user == null) ApiValidator.throwServiceError(
						InputValidationException.class,
						ApiError.DataTypeNotFound, "String: input.resetToken");

				user.actionCode = null;
			}

			if (isExistingPassword && !isActionCode) {
				user = UserServiceProvider.provide()
						.getUser(Long.valueOf(input.session.userKey.getId()));

				if (!UserServiceProvider.provide().verifyPassword(user,
						input.password))
					ApiValidator.throwServiceError(
							InputValidationException.class,
							ApiError.AuthenticationFailedBadPassword,
							"String: input.password");
			}

			user.password = UserServiceProvider.provide()
					.generatePassword(input.changedPassword);
			UserServiceProvider.provide().updateUser(user);

			if (output.session != null) {
				UserHelper.stripPassword(output.session.user);
			}

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting changePassword");
		return output;
	}
}