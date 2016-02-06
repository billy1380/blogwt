//  
//  ChangeUserAccessActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PermissionValidator;
import com.willshex.blogwt.server.api.validation.RoleValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessResponse;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class ChangeUserAccessActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(ChangeUserAccessActionHandler.class.getName());

	public ChangeUserAccessResponse handle (ChangeUserAccessRequest input) {
		LOG.finer("Entering changeUserAccess");
		ChangeUserAccessResponse output = new ChangeUserAccessResponse();
		try {
			ApiValidator.notNull(input, ChangeUserAccessRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator
					.lookupAndExtend(input.session, "input.session");

			if (input.revoke == null) {
				input.revoke = Boolean.FALSE;
			}

			input.user = UserValidator.lookup(input.user, "input.user");

			if (input.roles != null) {
				input.roles = RoleValidator.lookupAll(input.roles,
						"input.roles");
			}

			if (input.permissions != null) {
				input.permissions = PermissionValidator
						.lookupAll(input.permissions, "input.permissions");
			}

			if (Boolean.TRUE.equals(input.revoke)) {
				output.user = UserServiceProvider.provide()
						.removeUserRolesAndPermissions(input.user, input.roles,
								input.permissions);
			} else {
				output.user = UserServiceProvider.provide()
						.addUserRolesAndPermissions(input.user, input.roles,
								input.permissions);
			}

			UserHelper.stripPassword(output.user);

			UserHelper.stripPassword(output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting changeUserAccess");
		return output;
	}
}