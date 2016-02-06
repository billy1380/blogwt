//  
//  GetRolesActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.Arrays;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.RoleSortType;
import com.willshex.blogwt.shared.api.user.call.GetRolesRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class GetRolesActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(GetRolesActionHandler.class.getName());

	public GetRolesResponse handle (GetRolesRequest input) {
		LOG.finer("Entering getRoles");
		GetRolesResponse output = new GetRolesResponse();
		try {
			ApiValidator.notNull(input, GetRolesRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator
					.lookupAndExtend(input.session, "input.session");

			input.session.user = UserServiceProvider.provide()
					.getUser(Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user,
					Arrays.asList(PermissionServiceProvider.provide()
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