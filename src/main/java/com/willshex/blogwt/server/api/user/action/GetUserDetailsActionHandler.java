//  
//  GetUserDetailsActionHandlerjava
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
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsResponse;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class GetUserDetailsActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(GetUserDetailsActionHandler.class.getName());

	public GetUserDetailsResponse handle (GetUserDetailsRequest input) {
		LOG.finer("Entering getUserDetails");
		GetUserDetailsResponse output = new GetUserDetailsResponse();
		try {
			ApiValidator.notNull(input, GetUserDetailsRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator
					.lookupAndExtend(input.session, "input.session");

			input.session.user = UserServiceProvider.provide()
					.getUser(Long.valueOf(input.session.userKey.getId()));

			// if the not logged in user
			if (input.user.id.longValue() != input.session.userKey.getId()) {
				input.session.user = UserServiceProvider.provide()
						.getUser(Long.valueOf(input.session.userKey.getId()));

				UserValidator.authorisation(input.session.user,
						Arrays.asList(PermissionServiceProvider.provide()
								.getCodePermission(
										PermissionHelper.MANAGE_USERS)),
						"input.session.user");
			}

			output.user = input.user = UserValidator.lookup(input.user,
					"input.user");
			UserHelper.stripPassword(output.user);
			UserHelper.populateRolesAndPermissionsFromKeys(output.user);

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getUserDetails");
		return output;
	}
}