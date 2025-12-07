// 
//  GetMetaNotificationActionHandler.java
//  blogwt
// 
//  Created by William Shakour on March 19, 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.api.notification.action;

import java.util.Arrays;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.MetaNotificationValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationResponse;
import com.willshex.blogwt.shared.helper.PermissionHelper;

public final class GetMetaNotificationActionHandler extends
		ActionHandler<GetMetaNotificationRequest, GetMetaNotificationResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetMetaNotificationActionHandler.class.getName());

	@Override
	public void handle (GetMetaNotificationRequest input,
			GetMetaNotificationResponse output) throws Exception {
		ApiValidator.request(input, GetMetaNotificationRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.authorisation(input.session.user,
				Arrays.asList(
						PermissionServiceProvider.provide().getCodePermission(
								PermissionHelper.MANAGE_NOTIFICATIONS)),
				"input.session.user");

		output.meta = input.meta = MetaNotificationValidator.lookup(input.meta,
				"input.meta");
	}

	@Override
	protected GetMetaNotificationResponse newOutput () {
		return new GetMetaNotificationResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
