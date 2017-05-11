// 
//  SendAdhocNotificationActionHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.notification.action;

import java.util.Arrays;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.shared.api.datatype.NotificationModeType;
import com.willshex.blogwt.shared.api.notification.call.SendAdhocNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.SendAdhocNotificationResponse;
import com.willshex.blogwt.shared.helper.PermissionHelper;

public final class SendAdhocNotificationActionHandler extends
		ActionHandler<SendAdhocNotificationRequest, SendAdhocNotificationResponse> {

	private static final Logger LOG = Logger
			.getLogger(SendAdhocNotificationActionHandler.class.getName());

	@Override
	public void handle (SendAdhocNotificationRequest input,
			SendAdhocNotificationResponse output) throws Exception {
		ApiValidator.notNull(input, SendAdhocNotificationRequest.class,
				"input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.authorisation(input.session.user,
				Arrays.asList(
						PermissionServiceProvider.provide().getCodePermission(
								PermissionHelper.MANAGE_NOTIFICATIONS)),
				"input.session.user");

		if (input.mode == null) {
			input.mode = NotificationModeType.NotificationModeTypeEmail;
		}

		ApiValidator.notNull(input.content, String.class, "input.content");

		input.users = UserValidator.lookupAll(input.users, "input.users");

		// TODO: enqueue and send
	}

	@Override
	protected SendAdhocNotificationResponse newOutput () {
		return new SendAdhocNotificationResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}