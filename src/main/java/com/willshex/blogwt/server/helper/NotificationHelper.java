//
//  NotificationHelper.java
//  qure
//
//  Created by William Shakour (billy1380) on 15 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.util.List;
import java.util.Map;

import com.willshex.blogwt.server.service.metanotification.MetaNotificationServiceProvider;
import com.willshex.blogwt.server.service.notification.NotificationServiceProvider;
import com.willshex.blogwt.server.service.notificationsetting.NotificationSettingServiceProvider;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.pushtoken.PushTokenServiceProvider;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.Notification;
import com.willshex.blogwt.shared.api.datatype.NotificationModeType;
import com.willshex.blogwt.shared.api.datatype.NotificationSetting;
import com.willshex.blogwt.shared.api.datatype.PushToken;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.PropertyHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class NotificationHelper {

	public static Notification sendNotification (String code, User user,
			Map<String, ?> values) {
		Notification notification = null;

		MetaNotification meta = MetaNotificationServiceProvider.provide()
				.getCodeMetaNotification(code);

		if (meta != null) {
			NotificationSetting setting = NotificationSettingServiceProvider
					.provide().getMetaUserNotificationSetting(meta, user);

			if (setting == null) {
				setting = NotificationSettingServiceProvider.provide()
						.addNotificationSetting(new NotificationSetting()
								.meta(meta).selected(meta.defaults).user(user));
			}

			String content;
			notification = new Notification().content(
					content = InflatorHelper.inflate(values, meta.content))
					.meta(meta).target(user);

			notification = NotificationServiceProvider.provide()
					.addNotification(notification);

			String title = "Message from "
					+ PropertyHelper.value(PropertyServiceProvider.provide()
							.getNamedProperty(PropertyHelper.TITLE));

			for (NotificationModeType mode : setting.selected) {
				switch (mode) {
				case NotificationModeTypeEmail:
					EmailHelper.sendEmail(user.email, UserHelper.name(user),
							title, content, false);
					break;
				case NotificationModeTypePush:
					List<PushToken> tokens = PushTokenServiceProvider.provide()
							.getUserPushTokens(user);

					for (PushToken pushToken : tokens) {
						PushHelper.push(pushToken, meta.name, content);
					}

					break;
				case NotificationModeTypeSms:
					break;
				}
			}
		}

		return notification;
	}

}
