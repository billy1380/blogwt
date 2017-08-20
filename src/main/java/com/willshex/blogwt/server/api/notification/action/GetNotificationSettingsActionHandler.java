// 
//  GetNotificationSettingsActionHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.notification.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.metanotification.MetaNotificationServiceProvider;
import com.willshex.blogwt.server.service.notificationsetting.NotificationSettingServiceProvider;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.MetaNotificationSortType;
import com.willshex.blogwt.shared.api.datatype.NotificationSetting;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationSettingsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationSettingsResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;

public final class GetNotificationSettingsActionHandler extends
		ActionHandler<GetNotificationSettingsRequest, GetNotificationSettingsResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetNotificationSettingsActionHandler.class.getName());

	@Override
	public void handle (GetNotificationSettingsRequest input,
			GetNotificationSettingsResponse output) throws Exception {
		ApiValidator.request(input, GetNotificationSettingsRequest.class);
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

		List<MetaNotification> metas = MetaNotificationServiceProvider.provide()
				.getMetaNotifications(input.pager.start, input.pager.count,
						MetaNotificationSortType.fromString(input.pager.sortBy),
						input.pager.sortDirection);

		if (metas != null && metas.size() > 0) {
			output.settings = new ArrayList<>();
			NotificationSetting setting;
			User slimUser = (User) new User().id(input.user.id);

			for (MetaNotification meta : metas) {
				setting = NotificationSettingServiceProvider.provide()
						.getMetaUserNotificationSetting(meta, input.user);

				if (setting == null) {
					setting = new NotificationSetting().meta(meta)
							.user(input.user).selected(meta.defaults);
					setting = NotificationSettingServiceProvider.provide()
							.addNotificationSetting(setting);
				}

				setting.user = slimUser;
				setting.meta = meta;
			}
		}

		output.pager = PagerHelper.moveForward(input.pager);
	}

	@Override
	protected GetNotificationSettingsResponse newOutput () {
		return new GetNotificationSettingsResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}