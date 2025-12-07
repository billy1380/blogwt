// 
//  UpdateMetaNotificationActionHandler.java
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
import com.willshex.blogwt.server.service.metanotification.MetaNotificationServiceProvider;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.notification.call.UpdateMetaNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.UpdateMetaNotificationResponse;
import com.willshex.blogwt.shared.helper.PermissionHelper;

public final class UpdateMetaNotificationActionHandler extends
		ActionHandler<UpdateMetaNotificationRequest, UpdateMetaNotificationResponse> {

	private static final Logger LOG = Logger
			.getLogger(UpdateMetaNotificationActionHandler.class.getName());

	@Override
	public void handle (UpdateMetaNotificationRequest input,
			UpdateMetaNotificationResponse output) throws Exception {
		ApiValidator.request(input, UpdateMetaNotificationRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.authorisation(input.session.user,
				Arrays.asList(
						PermissionServiceProvider.provide().getCodePermission(
								PermissionHelper.MANAGE_NOTIFICATIONS)),
				"input.session.user");

		MetaNotification updatedMetaNotification = input.meta;

		input.meta = MetaNotificationValidator.lookup(input.meta, "input.meta");
		updatedMetaNotification = MetaNotificationValidator
				.validate(updatedMetaNotification, "input.meta");

		input.meta.code = updatedMetaNotification.code;
		input.meta.name = updatedMetaNotification.name;
		input.meta.group = updatedMetaNotification.group;
		input.meta.content = updatedMetaNotification.content;
		input.meta.modes = updatedMetaNotification.modes;
		input.meta.defaults = updatedMetaNotification.defaults;

		output.meta = MetaNotificationServiceProvider.provide()
				.updateMetaNotification(input.meta);
	}

	@Override
	protected UpdateMetaNotificationResponse newOutput () {
		return new UpdateMetaNotificationResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
