//  
//  ChangeUserAccessActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.Date;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PermissionValidator;
import com.willshex.blogwt.server.api.validation.RoleValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessResponse;

public final class ChangeUserAccessActionHandler extends
		ActionHandler<ChangeUserAccessRequest, ChangeUserAccessResponse> {
	private static final Logger LOG = Logger
			.getLogger(ChangeUserAccessActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (ChangeUserAccessRequest input,
			ChangeUserAccessResponse output) throws Exception {
		ApiValidator.notNull(input, ChangeUserAccessRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		if (input.revoke == null) {
			input.revoke = Boolean.FALSE;
		}

		input.user = UserValidator.lookup(input.user, "input.user");

		if (input.roles != null) {
			input.roles = RoleValidator.lookupAll(input.roles, "input.roles");
		}

		if (input.permissions != null) {
			input.permissions = PermissionValidator.lookupAll(input.permissions,
					"input.permissions");
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

		if (input.suspend != null) {
			if (Boolean.TRUE.equals(input.suspend)) {
				if (input.suspendUntil == null) {
					input.suspendUntil = new Date(Long.MAX_VALUE);
				}

				input.user.suspendUntil = input.suspendUntil;

			} else {
				input.user.suspendUntil = null;
			}

			output.user = UserServiceProvider.provide().updateUser(input.user);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected ChangeUserAccessResponse newOutput () {
		return new ChangeUserAccessResponse();
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
	public void clearSensitiveFields (ChangeUserAccessResponse output) {
		super.clearSensitiveFields(output);

		UserHelper.stripPassword(output.user);
	}
}