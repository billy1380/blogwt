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

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (IsAuthorisedRequest input,
			IsAuthorisedResponse output) throws Exception {
		ApiValidator.notNull(input, IsAuthorisedRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupAndExtend(input.session, "input.session");
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected IsAuthorisedResponse newOutput () {
		return new IsAuthorisedResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}

}