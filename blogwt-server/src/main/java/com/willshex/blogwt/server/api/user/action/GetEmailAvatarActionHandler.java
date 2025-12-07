//  
//  GetEmailAvatarActionHandlerjava
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
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarRequest;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarResponse;
import com.willshex.gson.web.service.server.InputValidationException;

public final class GetEmailAvatarActionHandler
		extends ActionHandler<GetEmailAvatarRequest, GetEmailAvatarResponse> {
	private static final Logger LOG = Logger
			.getLogger(GetEmailAvatarActionHandler.class.getName());
	@Override
	protected void handle (GetEmailAvatarRequest input,
			GetEmailAvatarResponse output) throws Exception {
		ApiValidator.request(input, GetEmailAvatarRequest.class);
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

		output.avatar = UserHelper.emailGravatar(input.email);
	}
	@Override
	protected GetEmailAvatarResponse newOutput () {
		return new GetEmailAvatarResponse();
	}
	@Override
	protected Logger logger () {
		return LOG;
	}
}
