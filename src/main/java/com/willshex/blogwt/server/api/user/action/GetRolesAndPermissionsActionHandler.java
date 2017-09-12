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

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsResponse;
import com.willshex.blogwt.shared.helper.DataTypeHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;

public final class GetRolesAndPermissionsActionHandler extends
		ActionHandler<GetRolesAndPermissionsRequest, GetRolesAndPermissionsResponse> {
	private static final Logger LOG = Logger
			.getLogger(GetRolesAndPermissionsActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (GetRolesAndPermissionsRequest input,
			GetRolesAndPermissionsResponse output) throws Exception {
		ApiValidator.request(input, GetRolesAndPermissionsRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		input.user = UserValidator.lookup(input.user, "input.user");

		if (!DataTypeHelper.<User> same(input.user, input.session.user)) {
			UserValidator.authorisation(input.session.user,
					Arrays.asList(PermissionServiceProvider.provide()
							.getCodePermission(PermissionHelper.MANAGE_USERS)),
					"input.session.user");
		}

		boolean idsOnly = Boolean.TRUE.equals(input.idsOnly);
		if (idsOnly) {
			input.user.roles = PersistenceHelper.typeList(Role.class,
					input.user.roleKeys);
			input.user.permissions = PersistenceHelper
					.typeList(Permission.class, input.user.permissionKeys);
		} else {
			if ((input.permissionOnly == null && input.rolesOnly == null)
					|| (Boolean.FALSE.equals(input.rolesOnly)
							&& Boolean.FALSE.equals(input.permissionOnly))) {
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

		if (input.user.roleKeys != null && !Boolean.TRUE.equals(input.rolesOnly)
				&& Boolean.TRUE.equals(input.expandRoles)) {
			List<Permission> expandedPermissions;
			if (idsOnly) {
				Role lookupRole;
				for (Role role : input.user.roles) {
					lookupRole = RoleServiceProvider.provide().getRole(role.id);

					if (lookupRole != null) {
						expandedPermissions = PersistenceHelper.typeList(
								Permission.class, lookupRole.permissionKeys);

						if (expandedPermissions != null) {
							if (output.permissions != null) {
								output.permissions.addAll(expandedPermissions);
							} else {
								output.permissions = expandedPermissions;
							}
						}
					}
				}
			} else {
				for (Role role : input.user.roles) {
					expandedPermissions = PermissionServiceProvider.provide()
							.getRolePermissions(role);

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
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected GetRolesAndPermissionsResponse newOutput () {
		return new GetRolesAndPermissionsResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}
}
