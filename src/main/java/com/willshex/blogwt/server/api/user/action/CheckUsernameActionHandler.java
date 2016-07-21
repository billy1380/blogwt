//  
//  CheckUsernameActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.shared.api.user.call.CheckUsernameRequest;
import com.willshex.blogwt.shared.api.user.call.CheckUsernameResponse;

public final class CheckUsernameActionHandler
		extends ActionHandler<CheckUsernameRequest, CheckUsernameResponse> {
	private static final Logger LOG = Logger
			.getLogger(CheckUsernameActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (CheckUsernameRequest input,
			CheckUsernameResponse output) throws Exception {
		ApiValidator.notNull(input, CheckUsernameRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected CheckUsernameResponse newOutput () {
		return new CheckUsernameResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}
}