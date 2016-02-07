//  
//  ForgotPasswordActionHandlerjava
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
import com.willshex.blogwt.shared.api.user.call.ForgotPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ForgotPasswordResponse;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class ForgotPasswordActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(ForgotPasswordActionHandler.class.getName());

	public ForgotPasswordResponse handle (ForgotPasswordRequest input) {
		LOG.finer("Entering forgotPassword");
		ForgotPasswordResponse output = new ForgotPasswordResponse();
		try {
			ApiValidator.notNull(input, ForgotPasswordRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator
					.lookupAndExtend(input.session, "input.session");

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting forgotPassword");
		return output;
	}
}