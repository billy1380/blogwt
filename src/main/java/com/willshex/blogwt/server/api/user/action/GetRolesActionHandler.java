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

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.shared.api.datatype.RoleSortType;
import com.willshex.blogwt.shared.api.user.call.GetRolesRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;

public final class GetRolesActionHandler
		extends ActionHandler<GetRolesRequest, GetRolesResponse> {
	private static final Logger LOG = Logger
			.getLogger(GetRolesActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (GetRolesRequest input, GetRolesResponse output)
			throws Exception {
		ApiValidator.notNull(input, GetRolesRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.authorisation(input.session.user,
				Arrays.asList(
						PermissionServiceProvider.provide().getCodePermission(
								PermissionHelper.MANAGE_PERMISSIONS)),
				"input.session.user");

		output.roles = RoleServiceProvider.provide().getRoles(input.pager.start,
				input.pager.count, RoleSortType.fromString(input.pager.sortBy),
				input.pager.sortDirection);

		output.pager = PagerHelper.moveForward(input.pager);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected GetRolesResponse newOutput () {
		return new GetRolesResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}
}