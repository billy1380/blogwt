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

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.exception.AuthenticationException;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.session.ISessionService;
import com.willshex.blogwt.server.service.session.SessionServiceProvider;
import com.willshex.blogwt.server.service.user.IUserService;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.LoginRequest;
import com.willshex.blogwt.shared.api.user.call.LoginResponse;

public final class LoginActionHandler
		extends ActionHandler<LoginRequest, LoginResponse> {
	private static final Logger LOG = Logger
			.getLogger(LoginActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (LoginRequest input, LoginResponse output)
			throws Exception {
		input = ApiValidator.request(input, LoginRequest.class);
		input.accessCode = ApiValidator.accessCode(input.accessCode,
				"input.accessCode");

		boolean foundToken = false;

		if (input.session != null && input.session.id != null) {
			foundToken = true;
		}

		if (!foundToken) {
			IUserService userService = UserServiceProvider.provide();

			User user = null;

			if (input.username != null) {
				user = userService.getLoginUser(input.username, input.password);

				if (user == null)
					throw new AuthenticationException(input.username);
			}

			if (user == null && input.email != null) {
				user = userService.getEmailLoginUser(input.email,
						input.password);

				if (user == null)
					throw new AuthenticationException(input.email);
			}

			if (user == null) throw new AuthenticationException(
					"Either username or email addressed cannot be null");

			ISessionService sessionService = SessionServiceProvider.provide();

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
				UserServiceProvider.provide().updateUserIdLastLoggedIn(user.id);
			} else {
				output.session = SessionServiceProvider.provide().extendSession(
						output.session, ISessionService.MILLIS_MINUTES);
				output.session.user = user;
			}
		} else {
			output.session = SessionValidator
					.lookupCheckAndExtend(input.session, "input.session");
		}

		if (output.session.user.roleKeys != null) {
			output.session.user.roles = PersistenceHelper.batchLookup(
					RoleServiceProvider.provide(),
					output.session.user.roleKeys);
		}

		if (output.session.user.permissionKeys != null) {
			output.session.user.permissions = PersistenceHelper.batchLookup(
					PermissionServiceProvider.provide(),
					output.session.user.permissionKeys);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected LoginResponse newOutput () {
		return new LoginResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}
}
