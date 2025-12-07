//  
//  IsAuthorisedActionHandlerjava
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
import com.willshex.blogwt.shared.api.user.call.IsAuthorisedRequest;
import com.willshex.blogwt.shared.api.user.call.IsAuthorisedResponse;

public final class IsAuthorisedActionHandler
		extends ActionHandler<IsAuthorisedRequest, IsAuthorisedResponse> {
	private static final Logger LOG = Logger
			.getLogger(IsAuthorisedActionHandler.class.getName());
	@Override
	protected void handle (IsAuthorisedRequest input,
			IsAuthorisedResponse output) throws Exception {
		ApiValidator.request(input, IsAuthorisedRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");
	}
	@Override
	protected IsAuthorisedResponse newOutput () {
		return new IsAuthorisedResponse();
	}
	@Override
	protected Logger logger () {
		return LOG;
	}

}