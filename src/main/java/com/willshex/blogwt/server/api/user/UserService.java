//  
//  UserService.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.exception.AuthenticationException;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.PersistenceService;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.session.ISessionService;
import com.willshex.blogwt.server.service.session.SessionServiceProvider;
import com.willshex.blogwt.server.service.user.IUserService;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.helper.UserHelper;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordResponse;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.CheckUsernameRequest;
import com.willshex.blogwt.shared.api.user.call.CheckUsernameResponse;
import com.willshex.blogwt.shared.api.user.call.ForgotPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ForgotPasswordResponse;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsResponse;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.GetUsersRequest;
import com.willshex.blogwt.shared.api.user.call.GetUsersResponse;
import com.willshex.blogwt.shared.api.user.call.IsAuthorisedRequest;
import com.willshex.blogwt.shared.api.user.call.IsAuthorisedResponse;
import com.willshex.blogwt.shared.api.user.call.LoginRequest;
import com.willshex.blogwt.shared.api.user.call.LoginResponse;
import com.willshex.blogwt.shared.api.user.call.LogoutRequest;
import com.willshex.blogwt.shared.api.user.call.LogoutResponse;
import com.willshex.gson.json.service.server.ActionHandler;
import com.willshex.gson.json.service.shared.StatusType;

public final class UserService extends ActionHandler {
	private static final Logger LOG = Logger.getLogger(UserService.class
			.getName());

	public GetUsersResponse getUsers (GetUsersRequest input) {
		LOG.finer("Entering getUsers");
		GetUsersResponse output = new GetUsersResponse();
		try {
			ApiValidator.notNull(input, GetUsersRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");
			
			UserHelper.stripPassword(output.users);

			UserHelper.stripPassword(output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getUsers");
		return output;
	}

	public LoginResponse login (LoginRequest input) {
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
						LOG.finer("Existing session not found, creating new session");
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
				output.session = SessionValidator.lookupAndExtend(
						input.session, "input.session");
				input.session.user = UserServiceProvider.provide().getUser(
						Long.valueOf(input.session.userKey.getId()));
			}

			if (output.session.user.roleKeys != null) {
				output.session.user.roles = RoleServiceProvider
						.provide()
						.getIdRolesBatch(
								PersistenceService
										.keysToIds(output.session.user.roleKeys));
			}

			if (output.session.user.permissionKeys != null) {
				output.session.user.permissions = PermissionServiceProvider
						.provide()
						.getIdPermissionsBatch(
								PersistenceService
										.keysToIds(output.session.user.permissionKeys));
			}

			UserHelper.stripPassword(output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting login");
		return output;
	}

	public LogoutResponse logout (LogoutRequest input) {
		LOG.finer("Entering logout");
		LogoutResponse output = new LogoutResponse();
		try {
			ApiValidator.notNull(input, LogoutRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			input.session = SessionValidator.lookup(input.session,
					"input.session");

			SessionServiceProvider.provide().deleteSession(input.session);

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting logout");
		return output;
	}

	public ChangePasswordResponse changePassword (ChangePasswordRequest input) {
		LOG.finer("Entering changePassword");
		ChangePasswordResponse output = new ChangePasswordResponse();
		try {
			ApiValidator.notNull(input, ChangePasswordRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			UserHelper.stripPassword(output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting changePassword");
		return output;
	}

	public ChangeUserDetailsResponse changeUserDetails (
			ChangeUserDetailsRequest input) {
		LOG.finer("Entering changeUserDetails");
		ChangeUserDetailsResponse output = new ChangeUserDetailsResponse();
		try {
			ApiValidator
					.notNull(input, ChangeUserDetailsRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			UserHelper.stripPassword(output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting changeUserDetails");
		return output;
	}

	public CheckUsernameResponse checkUsername (CheckUsernameRequest input) {
		LOG.finer("Entering checkUsername");
		CheckUsernameResponse output = new CheckUsernameResponse();
		try {
			ApiValidator.notNull(input, CheckUsernameRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting checkUsername");
		return output;
	}

	public GetRolesAndPermissionsResponse getRolesAndPermissions (
			GetRolesAndPermissionsRequest input) {
		LOG.finer("Entering getRolesAndPermissions");
		GetRolesAndPermissionsResponse output = new GetRolesAndPermissionsResponse();
		try {
			ApiValidator.notNull(input, GetRolesAndPermissionsRequest.class,
					"input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			UserHelper.stripPassword(output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getRolesAndPermissions");
		return output;
	}

	public IsAuthorisedResponse isAuthorised (IsAuthorisedRequest input) {
		LOG.finer("Entering isAuthorised");
		IsAuthorisedResponse output = new IsAuthorisedResponse();
		try {
			ApiValidator.notNull(input, IsAuthorisedRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			UserHelper.stripPassword(output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting isAuthorised");
		return output;
	}

	public GetUserDetailsResponse getUserDetails (GetUserDetailsRequest input) {
		LOG.finer("Entering getUserDetails");
		GetUserDetailsResponse output = new GetUserDetailsResponse();
		try {
			ApiValidator.notNull(input, GetUserDetailsRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			UserHelper.stripPassword(output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getUserDetails");
		return output;
	}

	public ForgotPasswordResponse forgotPassword (ForgotPasswordRequest input) {
		LOG.finer("Entering forgotPassword");
		ForgotPasswordResponse output = new ForgotPasswordResponse();
		try {
			ApiValidator.notNull(input, ForgotPasswordRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			UserHelper.stripPassword(output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting forgotPassword");
		return output;
	}
}