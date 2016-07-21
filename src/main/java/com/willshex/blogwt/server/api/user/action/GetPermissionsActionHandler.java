//  
//  GetPermissionsActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.Arrays;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.PermissionSortType;
import com.willshex.blogwt.shared.api.user.call.GetPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetPermissionsResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;

public final class GetPermissionsActionHandler
		extends ActionHandler<GetPermissionsRequest, GetPermissionsResponse> {
	private static final Logger LOG = Logger
			.getLogger(GetPermissionsActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (GetPermissionsRequest input,
			GetPermissionsResponse output) throws Exception {
		ApiValidator.notNull(input, GetPermissionsRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupAndExtend(input.session, "input.session");

		input.session.user = UserServiceProvider.provide()
				.getUser(Long.valueOf(input.session.userKey.getId()));

		UserValidator.authorisation(input.session.user,
				Arrays.asList(
						PermissionServiceProvider.provide().getCodePermission(
								PermissionHelper.MANAGE_PERMISSIONS)),
				"input.session.user");

		output.permissions = PermissionServiceProvider.provide().getPermissions(
				input.pager.start, input.pager.count,
				PermissionSortType.fromString(input.pager.sortBy),
				input.pager.sortDirection);

		output.pager = PagerHelper.moveForward(input.pager);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected GetPermissionsResponse newOutput () {
		return new GetPermissionsResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}

}