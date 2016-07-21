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

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsResponse;
import com.willshex.blogwt.shared.helper.PermissionHelper;

public final class GetUserDetailsActionHandler
		extends ActionHandler<GetUserDetailsRequest, GetUserDetailsResponse> {
	private static final Logger LOG = Logger
			.getLogger(GetUserDetailsActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (GetUserDetailsRequest input,
			GetUserDetailsResponse output) throws Exception {
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
							.getCodePermission(PermissionHelper.MANAGE_USERS)),
					"input.session.user");
		}

		output.user = input.user = UserValidator.lookup(input.user,
				"input.user");

		UserHelper.populateRolesAndPermissionsFromKeys(output.user);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected GetUserDetailsResponse newOutput () {
		return new GetUserDetailsResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.api.ActionHandler#clearSensitiveFields(com.
	 * willshex.blogwt.shared.api.Response) */
	@Override
	public void clearSensitiveFields (GetUserDetailsResponse output) {
		super.clearSensitiveFields(output);

		UserHelper.stripPassword(output.user);
	}
}