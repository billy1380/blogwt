//  
//  VerifyAccountActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.session.ISessionService;
import com.willshex.blogwt.server.service.session.SessionServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.VerifyAccountRequest;
import com.willshex.blogwt.shared.api.user.call.VerifyAccountResponse;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.shared.StatusType;

public final class VerifyAccountActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(VerifyAccountActionHandler.class.getName());

	public VerifyAccountResponse handle (VerifyAccountRequest input) {
		LOG.finer("Entering verifyAccount");
		VerifyAccountResponse output = new VerifyAccountResponse();
		try {
			ApiValidator.notNull(input, VerifyAccountRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			ApiValidator.validateToken(input.actionCode, "input.actionCode");
			User user = UserServiceProvider.provide()
					.getActionCodeUser(input.actionCode);

			if (user == null)
				ApiValidator.throwServiceError(InputValidationException.class,
						ApiError.DataTypeNotFound,
						"String: " + "input.actionCode");

			user.verified = Boolean.TRUE;
			user.actionCode = null;

			user = UserServiceProvider.provide().updateUser(user);

			output.session = SessionServiceProvider.provide()
					.getUserSession(user);

			if (output.session == null) {
				if (LOG.isLoggable(Level.FINER)) {
					LOG.finer(
							"Existing session not found, creating new session");
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
				output.session = SessionServiceProvider.provide().extendSession(
						output.session, ISessionService.MILLIS_MINUTES);
				output.session.user = user;
			}

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting verifyAccount");
		return output;
	}
}