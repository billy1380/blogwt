// 
//  GetNotificationsActionHandler.java
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
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.service.notification.NotificationServiceProvider;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.Notification;
import com.willshex.blogwt.shared.api.datatype.NotificationSortType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationsResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;

public final class GetNotificationsActionHandler extends
		ActionHandler<GetNotificationsRequest, GetNotificationsResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetNotificationsActionHandler.class.getName());

	@Override
	public void handle (GetNotificationsRequest input,
			GetNotificationsResponse output) throws Exception {
		ApiValidator.notNull(input, GetMetaNotificationsRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		if (input.user == null) {
			input.user = input.session.user;
		} else {
			UserValidator.authorisation(input.session.user,
					Arrays.asList(PermissionServiceProvider.provide()
							.getCodePermission(
									PermissionHelper.MANAGE_NOTIFICATIONS)),
					"input.session.user");

			input.user = UserValidator.lookup(input.user, "input.user");
		}

		if (input.pager == null) {
			input.pager = PagerHelper.createDefaultPager();
		}

		output.notifications = NotificationServiceProvider.provide()
				.getUserNotifications(input.user, input.pager.start,
						input.pager.count,
						NotificationSortType.fromString(input.pager.sortBy),
						input.pager.sortDirection);

		if (output.notifications != null) {
			for (Notification notification : output.notifications) {
				notification.meta = PersistenceHelper
						.type(MetaNotification.class, notification.metaKey);
				notification.target = PersistenceHelper.type(User.class,
						notification.targetKey);
				notification.sender = PersistenceHelper.type(User.class,
						notification.senderKey);
			}
		}

		output.pager = PagerHelper.moveForward(input.pager);
	}

	@Override
	protected GetNotificationsResponse newOutput () {
		return new GetNotificationsResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}