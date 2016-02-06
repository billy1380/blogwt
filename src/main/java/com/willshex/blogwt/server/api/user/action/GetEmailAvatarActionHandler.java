//  
//  GetEmailAvatarActionHandlerjava
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
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarRequest;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarResponse;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.shared.StatusType;

public final class GetEmailAvatarActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(GetEmailAvatarActionHandler.class.getName());

	public GetEmailAvatarResponse handle (GetEmailAvatarRequest input) {
		LOG.finer("Entering getEmailAvatar");
		GetEmailAvatarResponse output = new GetEmailAvatarResponse();
		try {
			ApiValidator.notNull(input, GetEmailAvatarRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			if (input.session != null) {
				try {
					output.session = input.session = SessionValidator
							.lookupAndExtend(input.session, "input.session");
				} catch (InputValidationException ex) {
					output.session = input.session = null;
				}
			}

			ApiValidator.notNull(input.email, String.class, "input.email");

			output.avatar = UserHelper.emailAvatar(input.email);

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getEmailAvatar");
		return output;
	}
}