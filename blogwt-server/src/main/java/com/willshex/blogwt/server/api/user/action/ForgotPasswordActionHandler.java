//  
//  ForgotPasswordActionHandlerjava
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
import com.willshex.blogwt.shared.api.user.call.ForgotPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ForgotPasswordResponse;

public final class ForgotPasswordActionHandler
		extends ActionHandler<ForgotPasswordRequest, ForgotPasswordResponse> {
	private static final Logger LOG = Logger
			.getLogger(ForgotPasswordActionHandler.class.getName());
	@Override
	protected void handle (ForgotPasswordRequest input,
			ForgotPasswordResponse output) throws Exception {
		ApiValidator.request(input, ForgotPasswordRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");
	}
	@Override
	protected ForgotPasswordResponse newOutput () {
		return new ForgotPasswordResponse();
	}
	@Override
	protected Logger logger () {
		return LOG;
	}
}