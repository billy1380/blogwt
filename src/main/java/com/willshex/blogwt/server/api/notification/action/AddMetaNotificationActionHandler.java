// 
//  AddMetaNotificationActionHandler.java
//  blogwt
// 
//  Created by William Shakour on March 19, 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.api.notification.action;

import java.util.Arrays;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.MetaNotificationValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.metanotification.MetaNotificationServiceProvider;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.shared.api.notification.call.AddMetaNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.AddMetaNotificationResponse;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.gson.web.service.server.ActionHandler;

public final class AddMetaNotificationActionHandler extends
		ActionHandler<AddMetaNotificationRequest, AddMetaNotificationResponse> {

	private static final Logger LOG = Logger
			.getLogger(AddMetaNotificationActionHandler.class.getName());

	@Override
	public void handle (AddMetaNotificationRequest input,
			AddMetaNotificationResponse output) throws Exception {
		ApiValidator.request(input, AddMetaNotificationRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.authorisation(input.session.user,
				Arrays.asList(
						PermissionServiceProvider.provide().getCodePermission(
								PermissionHelper.MANAGE_NOTIFICATIONS)),
				"input.session.user");

		input.meta = MetaNotificationValidator.validate(input.meta,
				"input.meta");

		output.meta = MetaNotificationServiceProvider.provide()
				.addMetaNotification(input.meta);
	}

	@Override
	protected AddMetaNotificationResponse newOutput () {
		return new AddMetaNotificationResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}