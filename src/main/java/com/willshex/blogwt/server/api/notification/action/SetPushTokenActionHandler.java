// 
//  SetPushTokenActionHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.notification.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.shared.api.notification.call.SetPushTokenRequest;
import com.willshex.blogwt.shared.api.notification.call.SetPushTokenResponse;

public final class SetPushTokenActionHandler
		extends ActionHandler<SetPushTokenRequest, SetPushTokenResponse> {

	private static final Logger LOG = Logger
			.getLogger(SetPushTokenActionHandler.class.getName());

	@Override
	public void handle (SetPushTokenRequest input, SetPushTokenResponse output)
			throws Exception {
		ApiValidator.notNull(input, SetPushTokenRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");
		
		
	}

	@Override
	protected SetPushTokenResponse newOutput () {
		return new SetPushTokenResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}