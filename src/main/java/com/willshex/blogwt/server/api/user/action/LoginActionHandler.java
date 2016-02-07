//  
//  LoginActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.exception.AuthenticationException;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.PersistenceService;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.session.ISessionService;
import com.willshex.blogwt.server.service.session.SessionServiceProvider;
import com.willshex.blogwt.server.service.user.IUserService;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.LoginRequest;
import com.willshex.blogwt.shared.api.user.call.LoginResponse;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class LoginActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(LoginActionHandler.class.getName());

	public LoginResponse handle (LoginRequest input) {
		LOG.finer("Entering login");
		LoginResponse output = new LoginResponse();
		try {
			input = ApiValidator.request(input, LoginRequest.class);
			input.accessCode = ApiValidator.accessCode(input.accessCode,
					"input.accessCode");

			boolean foundToken = false;

			if (input.session != null && input.session.id != null) {
				foundToken = true;
			}

			if (!foundToken) {
				IUserService userService = UserServiceProvider.provide();

				User user = userService.getLoginUser(input.username,
						input.password);

				if (user == null)
					throw new AuthenticationException(input.username);

				ISessionService sessionService = SessionServiceProvider
						.provide();

				if (LOG.isLoggable(Level.FINER)) {
					LOG.finer("Getting user session");
				}

				output.session = sessionService.getUserSession(user);

				if (output.session == null) {
					if (LOG.isLoggable(Level.FINER)) {
						LOG.finer(
								"Existing session not found, creating new session");
					}

					output.session = sessionService.createUserSession(user,
							input.longTerm);

					if (output.session != null) {
						output.session.user = user;
					} else {
						throw new Exception(
								"Unexpected blank session after creating user session.");
					}
				} else {
					output.session = SessionServiceProvider.provide()
							.extendSession(output.session,
									ISessionService.MILLIS_MINUTES);
					output.session.user = user;
				}
			} else {
				output.session = SessionValidator.lookupAndExtend(input.session,
						"input.session");
				input.session.user = UserServiceProvider.provide()
						.getUser(Long.valueOf(input.session.userKey.getId()));
			}

			if (output.session.user.roleKeys != null) {
				output.session.user.roles = RoleServiceProvider.provide()
						.getIdRolesBatch(PersistenceService
								.keysToIds(output.session.user.roleKeys));
			}

			if (output.session.user.permissionKeys != null) {
				output.session.user.permissions = PermissionServiceProvider
						.provide().getIdPermissionsBatch(PersistenceService
								.keysToIds(output.session.user.permissionKeys));
			}

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting login");
		return output;
	}
}