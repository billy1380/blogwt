// 
//  UpdateNotificationSettingsActionHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.notification.action;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.dev.util.collect.HashMap;
import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.NotificationSettingValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.service.metanotification.MetaNotificationServiceProvider;
import com.willshex.blogwt.server.service.notification.NotificationServiceProvider;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.NotificationSetting;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.notification.call.UpdateNotificationSettingsRequest;
import com.willshex.blogwt.shared.api.notification.call.UpdateNotificationSettingsResponse;

public final class UpdateNotificationSettingsActionHandler extends
		ActionHandler<UpdateNotificationSettingsRequest, UpdateNotificationSettingsResponse> {

	private static final Logger LOG = Logger
			.getLogger(UpdateNotificationSettingsActionHandler.class.getName());

	@Override
	public void handle (UpdateNotificationSettingsRequest input,
			UpdateNotificationSettingsResponse output) throws Exception {
		ApiValidator.notNull(input, UpdateNotificationSettingsRequest.class,
				"input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		List<NotificationSetting> settings = NotificationSettingValidator
				.lookupAll(input.settings, "input.settings");

		input.settings = NotificationSettingValidator
				.validateAll(input.settings, "input.settings");
		Map<Long, NotificationSetting> inputMap = PersistenceHelper
				.typeMap(input.settings);

		for (NotificationSetting setting : settings) {
			setting.selected = inputMap.get(setting.id).selected;
		}

		output.settings = NotificationServiceProvider.provide()
				.updateNotificationSettings(settings);

		if (output.settings != null) {
			Map<Long, NotificationSetting> notificationSettingLookup = new HashMap<>();
			for (NotificationSetting setting : output.settings) {
				notificationSettingLookup.put(
						PersistenceHelper.keyToId(setting.metaKey), setting);
				setting.user = PersistenceHelper.type(User.class,
						setting.userKey);
			}

			List<MetaNotification> metas = MetaNotificationServiceProvider
					.provide().getIdMetaNotificationBatch(
							notificationSettingLookup.keySet());

			for (MetaNotification meta : metas) {
				notificationSettingLookup.get(meta.id).meta = meta;
			}
		}
	}

	@Override
	protected UpdateNotificationSettingsResponse newOutput () {
		return new UpdateNotificationSettingsResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}