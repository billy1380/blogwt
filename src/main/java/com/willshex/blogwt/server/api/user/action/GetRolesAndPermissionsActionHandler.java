//  
//  GetRolesAndPermissionsActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.PersistenceService;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsResponse;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class GetRolesAndPermissionsActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(GetRolesAndPermissionsActionHandler.class.getName());

	public GetRolesAndPermissionsResponse handle (
			GetRolesAndPermissionsRequest input) {
		LOG.finer("Entering getRolesAndPermissions");
		GetRolesAndPermissionsResponse output = new GetRolesAndPermissionsResponse();
		try {
			ApiValidator.notNull(input, GetRolesAndPermissionsRequest.class,
					"input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator
					.lookupAndExtend(input.session, "input.session");

			input.session.user = UserServiceProvider.provide()
					.getUser(Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user,
					Arrays.asList(PermissionServiceProvider.provide()
							.getCodePermission(PermissionHelper.MANAGE_USERS)),
					"input.session.user");

			input.user = UserValidator.lookup(input.user, "input.user");

			boolean idsOnly = Boolean.TRUE.equals(input.idsOnly);
			if (idsOnly) {
				input.user.roles = PersistenceService.dataTypeList(Role.class,
						input.user.roleKeys);
				input.user.permissions = PersistenceService.dataTypeList(
						Permission.class, input.user.permissionKeys);
			} else {
				if ((input.permissionOnly == null && input.rolesOnly == null)
						|| (Boolean.FALSE.equals(input.rolesOnly)
								&& Boolean.FALSE
										.equals(input.permissionOnly))) {
					UserHelper.populateRolesAndPermissionsFromKeys(input.user);

					output.roles = input.user.roles;
					output.permissions = input.user.permissions;
				} else if (Boolean.TRUE.equals(input.permissionOnly)
						&& Boolean.TRUE.equals(input.rolesOnly)) {

				} else if (Boolean.TRUE.equals(input.permissionOnly)) {
					UserHelper.populatePermissionsFromKeys(input.user);
					output.permissions = input.user.permissions;
				} else if (Boolean.TRUE.equals(input.rolesOnly)) {
					UserHelper.populateRolesFromKeys(input.user);
					output.roles = input.user.roles;
				}
			}

			if (input.user.roleKeys != null
					&& !Boolean.TRUE.equals(input.rolesOnly)
					&& Boolean.TRUE.equals(input.expandRoles)) {
				List<Permission> expandedPermissions;
				if (idsOnly) {
					Role lookupRole;
					for (Role role : input.user.roles) {
						lookupRole = RoleServiceProvider.provide()
								.getRole(role.id);

						if (lookupRole != null) {
							expandedPermissions = PersistenceService
									.dataTypeList(Permission.class,
											lookupRole.permissionKeys);

							if (expandedPermissions != null) {
								if (output.permissions != null) {
									output.permissions
											.addAll(expandedPermissions);
								} else {
									output.permissions = expandedPermissions;
								}
							}
						}
					}
				} else {
					for (Role role : input.user.roles) {
						expandedPermissions = PermissionServiceProvider
								.provide().getRolePermissions(role);

						if (expandedPermissions != null) {
							if (output.permissions != null) {
								output.permissions.addAll(expandedPermissions);
							} else {
								output.permissions = expandedPermissions;
							}
						}
					}
				}
			}

			UserHelper.stripPassword(output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getRolesAndPermissions");
		return output;
	}
}