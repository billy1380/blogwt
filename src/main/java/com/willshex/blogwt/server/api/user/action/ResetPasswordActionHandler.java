//  
//  ResetPasswordActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.ResetPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ResetPasswordResponse;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.shared.StatusType;

public final class ResetPasswordActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(ResetPasswordActionHandler.class.getName());

	public ResetPasswordResponse handle (ResetPasswordRequest input) {
		LOG.finer("Entering resetPassword");
		ResetPasswordResponse output = new ResetPasswordResponse();
		try {
			ApiValidator.notNull(input, ResetPasswordRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			if (input.session != null) {
				try {
					output.session = input.session = SessionValidator
							.lookupAndExtend(input.session, "input.session");
				} catch (InputValidationException ex) {
					output.session = input.session = null;
				}
			}

			ApiValidator.notNull(input.email, String.class, "input.email");

			User user = UserServiceProvider.provide().getEmailUser(input.email);

			if (user == null)
				ApiValidator.throwServiceError(InputValidationException.class,
						ApiError.DataTypeNotFound, "String: input.email");

			UserServiceProvider.provide().resetPassword(user);

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting resetPassword");
		return output;
	}
}