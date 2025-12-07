//  
//  ResetPasswordActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.ResetPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ResetPasswordResponse;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;

public final class ResetPasswordActionHandler
		extends ActionHandler<ResetPasswordRequest, ResetPasswordResponse> {
	private static final Logger LOG = Logger
			.getLogger(ResetPasswordActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (ResetPasswordRequest input,
			ResetPasswordResponse output) throws Exception {
		ApiValidator.request(input, ResetPasswordRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		if (input.session != null) {
			try {
				output.session = input.session = SessionValidator
						.lookupCheckAndExtend(input.session, "input.session");
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
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected ResetPasswordResponse newOutput () {
		return new ResetPasswordResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}
}