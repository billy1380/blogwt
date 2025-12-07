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
import com.willshex.blogwt.server.api.validation.PushTokenValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.pushtoken.PushTokenServiceProvider;
import com.willshex.blogwt.shared.api.datatype.PushToken;
import com.willshex.blogwt.shared.api.notification.call.SetPushTokenRequest;
import com.willshex.blogwt.shared.api.notification.call.SetPushTokenResponse;

public final class SetPushTokenActionHandler
		extends ActionHandler<SetPushTokenRequest, SetPushTokenResponse> {

	private static final Logger LOG = Logger
			.getLogger(SetPushTokenActionHandler.class.getName());

	@Override
	public void handle (SetPushTokenRequest input, SetPushTokenResponse output)
			throws Exception {
		ApiValidator.request(input, SetPushTokenRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		if (input.token != null && input.token.user == null) {
			input.token.user = input.session.user;
		}

		input.token = PushTokenValidator.valiate(input.token, "input.token");

		PushToken token = PushTokenServiceProvider.provide()
				.getUserPlatformPushToken(input.token.user,
						input.token.platform);

		if (token == null) {
			input.token = PushTokenServiceProvider.provide()
					.addPushToken(input.token);
		} else {
			if (input.token.value != null) {
				token.value = input.token.value;
			}

			PushTokenServiceProvider.provide().updatePushToken(token);
		}
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
