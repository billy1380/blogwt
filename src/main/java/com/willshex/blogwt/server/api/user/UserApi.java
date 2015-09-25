//  
//  UserApi.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.exception.AuthenticationException;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PermissionValidator;
import com.willshex.blogwt.server.api.validation.RoleValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.PersistenceService;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.session.ISessionService;
import com.willshex.blogwt.server.service.session.SessionServiceProvider;
import com.willshex.blogwt.server.service.user.IUserService;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.PermissionSortType;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.RoleSortType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.datatype.UserSortType;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordResponse;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.ChangeUserPowersRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserPowersResponse;
import com.willshex.blogwt.shared.api.user.call.CheckUsernameRequest;
import com.willshex.blogwt.shared.api.user.call.CheckUsernameResponse;
import com.willshex.blogwt.shared.api.user.call.ForgotPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ForgotPasswordResponse;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarRequest;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarResponse;
import com.willshex.blogwt.shared.api.user.call.GetPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetPermissionsResponse;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsResponse;
import com.willshex.blogwt.shared.api.user.call.GetRolesRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesResponse;
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
import com.willshex.blogwt.shared.api.user.call.RegisterUserRequest;
import com.willshex.blogwt.shared.api.user.call.RegisterUserResponse;
import com.willshex.blogwt.shared.api.user.call.ResetPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ResetPasswordResponse;
import com.willshex.blogwt.shared.api.user.call.VerifyAccountRequest;
import com.willshex.blogwt.shared.api.user.call.VerifyAccountResponse;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.helper.TagHelper;
import com.willshex.gson.json.service.server.ActionHandler;
import com.willshex.gson.json.service.server.InputValidationException;
import com.willshex.gson.json.service.shared.StatusType;

public final class UserApi extends ActionHandler {
	private static final Logger LOG = Logger.getLogger(UserApi.class.getName());

	public VerifyAccountResponse verifyAccount (VerifyAccountRequest input) {
		LOG.finer("Entering verifyAccount");
		VerifyAccountResponse output = new VerifyAccountResponse();
		try {
			ApiValidator.notNull(input, VerifyAccountRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			ApiValidator.validateToken(input.actionCode, "input.actionCode");
			User user = UserServiceProvider.provide().getActionCodeUser(
					input.actionCode);

			if (user == null)
				ApiValidator.throwServiceError(InputValidationException.class,
						ApiError.DataTypeNotFound, "String: "
								+ "input.actionCode");

			user.verified = Boolean.TRUE;
			user.actionCode = null;

			user = UserServiceProvider.provide().updateUser(user);

			output.session = SessionServiceProvider.provide().getUserSession(
					user);

			if (output.session == null) {
				if (LOG.isLoggable(Level.FINER)) {
					LOG.finer("Existing session not found, creating new session");
				}

				output.session = SessionServiceProvider.provide()
						.createUserSession(user, false);

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

			UserHelper.stripPassword(output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting verifyAccount");
		return output;
	}

	public ResetPasswordResponse resetPassword (ResetPasswordRequest input) {
		LOG.finer("Entering resetPassword");
		ResetPasswordResponse output = new ResetPasswordResponse();
		try {
			ApiValidator.notNull(input, ResetPasswordRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			if (input.session != null) {
				try {
					output.session = input.session = SessionValidator
							.lookupAndExtend(input.session, "input.session");
				} catch (InputValidationException ex) {
					output.session = input.session = null;
				}
			}

			ApiValidator.notNull(input.email, String.class, "input.email");

			User user = UserServiceProvider.provide().getEmailUser(input.email);

			if (user == null)
				ApiValidator.throwServiceError(InputValidationException.class,
						ApiError.DataTypeNotFound, "String: input.email");

			UserServiceProvider.provide().resetPassword(user);

			if (output.session != null) {
				UserHelper.stripPassword(output.session.user);
			}

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting resetPassword");
		return output;
	}

	public ChangeUserPowersResponse changeUserPowers (
			ChangeUserPowersRequest input) {
		LOG.finer("Entering changeUserPowers");
		ChangeUserPowersResponse output = new ChangeUserPowersResponse();
		try {
			ApiValidator.notNull(input, ChangeUserPowersRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			ApiValidator.notNull(input.assign, Boolean.class, "input.assign");

			input.user = UserValidator.lookup(input.user, "input.user");

			if (input.roles != null) {
				input.roles = RoleValidator.lookupAll(input.roles,
						"input.roles");
			}

			if (input.permissions != null) {
				input.permissions = PermissionValidator.lookupAll(
						input.permissions, "input.permissions");
			}

			if (Boolean.TRUE.equals(input.assign)) {
				output.user = UserServiceProvider.provide()
						.addUserRolesAndPermissions(input.user, input.roles,
								input.permissions);
			} else {
				output.user = UserServiceProvider.provide()
						.removeUserRolesAndPermissions(input.user, input.roles,
								input.permissions);
			}

			UserHelper.stripPassword(output.user);

			UserHelper.stripPassword(output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting changeUserPowers");
		return output;
	}

	public GetEmailAvatarResponse getEmailAvatar (GetEmailAvatarRequest input) {
		LOG.finer("Entering getEmailAvatar");
		GetEmailAvatarResponse output = new GetEmailAvatarResponse();
		try {
			ApiValidator.notNull(input, GetEmailAvatarRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			if (input.session != null) {
				try {
					output.session = input.session = SessionValidator
							.lookupAndExtend(input.session, "input.session");
				} catch (InputValidationException ex) {
					output.session = input.session = null;
				}
			}

			ApiValidator.notNull(input.email, String.class, "input.email");

			output.avatar = UserHelper.emailAvatar(input.email);

			if (output.session != null) {
				UserHelper.stripPassword(output.session.user);
			}

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getEmailAvatar");
		return output;
	}

	public RegisterUserResponse registerUser (RegisterUserRequest input) {
		LOG.finer("Entering registerUser");
		RegisterUserResponse output = new RegisterUserResponse();
		try {
			ApiValidator.notNull(input, GetEmailAvatarRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			if (input.session != null) {
				try {
					output.session = input.session = SessionValidator
							.lookupAndExtend(input.session, "input.session");
				} catch (InputValidationException ex) {
					output.session = input.session = null;
				}
			}

			input.user = UserValidator.validate(input.user, "input.user");

			List<String> codes;

			List<Permission> permissions = null;
			Property property = PropertyServiceProvider.provide()
					.getNamedProperty(PropertyHelper.NEW_USER_PERMISSIONS);

			if (!PropertyHelper.isEmpty(property)
					&& !PropertyHelper.NONE_VALUE.equals(property.value)) {
				codes = TagHelper.convertToTagList(property.value, true);

				for (String code : codes) {
					if (permissions == null) {
						permissions = new ArrayList<Permission>();
					}

					permissions.add(new Permission().code(code.toUpperCase()));
				}

				permissions = PermissionValidator.lookupAll(permissions,
						PropertyHelper.NEW_USER_PERMISSIONS);
			}

			List<Role> roles = null;
			property = PropertyServiceProvider.provide().getNamedProperty(
					PropertyHelper.NEW_USER_ROLES);

			if (!PropertyHelper.isEmpty(property)
					&& !PropertyHelper.NONE_VALUE.equals(property.value)) {
				codes = TagHelper.convertToTagList(property.value, true);

				for (String code : codes) {
					if (roles == null) {
						roles = new ArrayList<Role>();
					}

					roles.add(new Role().code(code.toUpperCase()));
				}

				roles = RoleValidator.lookupAll(roles,
						PropertyHelper.NEW_USER_ROLES);
			}

			input.user.permissions = permissions;
			input.user.roles = roles;

			output.user = UserServiceProvider.provide().addUser(input.user);
			UserServiceProvider.provide().verifyAccount(output.user);

			UserHelper.stripPassword(output.user);

			if (output.session != null) {
				UserHelper.stripPassword(output.session.user);
			}

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting registerUser");
		return output;
	}

	public GetUsersResponse getUsers (GetUsersRequest input) {
		LOG.finer("Entering getUsers");
		GetUsersResponse output = new GetUsersResponse();
		try {
			ApiValidator.notNull(input, GetUsersRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			output.session.user = UserServiceProvider.provide().getUser(
					Long.valueOf(output.session.userKey.getId()));

			UserValidator.authorisation(input.session.user, Arrays
					.asList(PermissionServiceProvider.provide()
							.getCodePermission(PermissionHelper.MANAGE_USERS)),
					"input.session.user");

			output.users = UserServiceProvider.provide().getUsers(
					input.pager.start, input.pager.count,
					UserSortType.fromString(input.pager.sortBy),
					input.pager.sortDirection);

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
				user = UserServiceProvider.provide().getActionCodeUser(
						input.resetCode);

				if (user == null)
					ApiValidator.throwServiceError(
							InputValidationException.class,
							ApiError.DataTypeNotFound,
							"String: input.resetToken");

				user.actionCode = null;
			}

			if (isExistingPassword && !isActionCode) {
				user = UserServiceProvider.provide().getUser(
						Long.valueOf(input.session.userKey.getId()));

				if (!UserServiceProvider.provide().verifyPassword(user,
						input.password))
					ApiValidator.throwServiceError(
							InputValidationException.class,
							ApiError.AuthenticationFailedBadPassword,
							"String: input.password");
			}

			user.password = UserServiceProvider.provide().generatePassword(
					input.changedPassword);
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

			User updatedUser = input.user;

			input.user = UserValidator.lookup(input.user, "input.user");

			// if the not logged in user
			if (input.user.id.longValue() != input.session.userKey.getId()) {
				input.session.user = UserServiceProvider.provide().getUser(
						Long.valueOf(input.session.userKey.getId()));

				UserValidator.authorisation(input.session.user, Arrays
						.asList(PermissionServiceProvider.provide()
								.getCodePermission(
										PermissionHelper.MANAGE_USERS)),
						"input.session.user");
			}

			updatedUser = UserValidator.validate(updatedUser, "input.user");

			input.user.username = updatedUser.username;
			input.user.forename = updatedUser.forename;
			input.user.surname = updatedUser.surname;
			input.user.email = updatedUser.email;

			output.user = UserServiceProvider.provide().updateUser(input.user);

			UserHelper.stripPassword(output.user);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;

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

			input.session.user = UserServiceProvider.provide().getUser(
					Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user, Arrays
					.asList(PermissionServiceProvider.provide()
							.getCodePermission(PermissionHelper.MANAGE_USERS)),
					"input.session.user");

			output.user = input.user = UserValidator.lookup(input.user,
					"input.user");
			UserHelper.stripPassword(output.user);
			UserHelper.populateRolesAndPermissionsFromKeys(output.user);

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

	public GetPermissionsResponse getPermissions (GetPermissionsRequest input) {
		LOG.finer("Entering getPermissions");
		GetPermissionsResponse output = new GetPermissionsResponse();
		try {
			ApiValidator.notNull(input, GetPermissionsRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			input.session.user = UserServiceProvider.provide().getUser(
					Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user, Arrays
					.asList(PermissionServiceProvider.provide()
							.getCodePermission(
									PermissionHelper.MANAGE_PERMISSIONS)),
					"input.session.user");

			output.permissions = PermissionServiceProvider.provide()
					.getPermissions(input.pager.start, input.pager.count,
							PermissionSortType.fromString(input.pager.sortBy),
							input.pager.sortDirection);

			output.pager = PagerHelper.moveForward(input.pager);

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getPermissions");
		return output;
	}

	public GetRolesResponse getRoles (GetRolesRequest input) {
		LOG.finer("Entering getRoles");
		GetRolesResponse output = new GetRolesResponse();
		try {
			ApiValidator.notNull(input, GetRolesRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			input.session.user = UserServiceProvider.provide().getUser(
					Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user, Arrays
					.asList(PermissionServiceProvider.provide()
							.getCodePermission(
									PermissionHelper.MANAGE_PERMISSIONS)),
					"input.session.user");

			output.roles = RoleServiceProvider.provide().getRoles(
					input.pager.start, input.pager.count,
					RoleSortType.fromString(input.pager.sortBy),
					input.pager.sortDirection);

			output.pager = PagerHelper.moveForward(input.pager);

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getRoles");
		return output;
	}
}