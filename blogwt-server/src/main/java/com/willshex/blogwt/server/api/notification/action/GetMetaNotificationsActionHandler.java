// 
//  GetMetaNotificationsActionHandler.java
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
import com.willshex.blogwt.server.service.metanotification.MetaNotificationServiceProvider;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.shared.api.datatype.MetaNotificationSortType;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationsResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;

public final class GetMetaNotificationsActionHandler extends
		ActionHandler<GetMetaNotificationsRequest, GetMetaNotificationsResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetMetaNotificationsActionHandler.class.getName());

	@Override
	public void handle (GetMetaNotificationsRequest input,
			GetMetaNotificationsResponse output) throws Exception {
		ApiValidator.request(input, GetMetaNotificationsRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.authorisation(input.session.user,
				Arrays.asList(
						PermissionServiceProvider.provide().getCodePermission(
								PermissionHelper.MANAGE_NOTIFICATIONS)),
				"input.session.user");

		if (input.pager == null) {
			input.pager = PagerHelper.createDefaultPager();
		}

		output.metas = MetaNotificationServiceProvider.provide()
				.getMetaNotifications(input.pager.start, input.pager.count,
						MetaNotificationSortType.fromString(input.pager.sortBy),
						input.pager.sortDirection);

		output.pager = PagerHelper.moveForward(input.pager);
	}

	@Override
	protected GetMetaNotificationsResponse newOutput () {
		return new GetMetaNotificationsResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}