//  
//  LogoutActionHandlerjava
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
import com.willshex.blogwt.server.service.session.SessionServiceProvider;
import com.willshex.blogwt.shared.api.user.call.LogoutRequest;
import com.willshex.blogwt.shared.api.user.call.LogoutResponse;

public final class LogoutActionHandler
		extends ActionHandler<LogoutRequest, LogoutResponse> {
	private static final Logger LOG = Logger
			.getLogger(LogoutActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (LogoutRequest input, LogoutResponse output)
			throws Exception {
		ApiValidator.request(input, LogoutRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		input.session = SessionValidator.lookup(input.session, "input.session");

		SessionServiceProvider.provide().deleteSession(input.session);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected LogoutResponse newOutput () {
		return new LogoutResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}

}