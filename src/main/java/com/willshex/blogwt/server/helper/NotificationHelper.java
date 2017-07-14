//
//  NotificationHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonObject;
import com.willshex.blogwt.server.helper.push.AndroidMessage;
import com.willshex.blogwt.server.service.metanotification.MetaNotificationServiceProvider;
import com.willshex.blogwt.server.service.notification.NotificationServiceProvider;
import com.willshex.blogwt.server.service.notificationsetting.NotificationSettingServiceProvider;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.pushtoken.PushTokenServiceProvider;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.Notification;
import com.willshex.blogwt.shared.api.datatype.NotificationModeType;
import com.willshex.blogwt.shared.api.datatype.NotificationSetting;
import com.willshex.blogwt.shared.api.datatype.PushToken;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.JsonableHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.gson.shared.Jsonable;

/**
 * @author William Shakour (billy1380)
 *
 */
public class NotificationHelper {

	private static final Logger LOG = Logger
			.getLogger(NotificationHelper.class.getName());

	public static List<Notification> sendNotification (String code, User user) {
		return sendNotification(code, user, null);
	}

	public static List<Notification> sendNotification (String code, User user,
			final Map<String, ?> values) {
		List<Notification> notifications = new ArrayList<>();

		final MetaNotification meta = MetaNotificationServiceProvider.provide()
				.getCodeMetaNotification(code);

		if (meta == null) {
			if (LOG.isLoggable(Level.WARNING)) {
				LOG.warning(
						"No meta notification found for code [" + code + "]");
			}
		} else {
			NotificationSetting setting = NotificationSettingServiceProvider
					.provide().getMetaUserNotificationSetting(meta, user);

			if (setting == null) {
				setting = NotificationSettingServiceProvider.provide()
						.addNotificationSetting(new NotificationSetting()
								.meta(meta).selected(meta.defaults).user(user));
			}

			String content;
			final Notification template = new Notification().content(
					content = InflatorHelper.inflate(values, meta.content))
					.meta(meta).target(user);
			Notification notification;

			String title = "Message from "
					+ PropertyHelper.value(PropertyServiceProvider.provide()
							.getNamedProperty(PropertyHelper.TITLE));

			if (setting.selected == null || setting.selected.size() == 0) {
				if (LOG.isLoggable(Level.INFO)) {
					LOG.info("No notification modes found for [" + code
							+ "] and user [" + user + "] adding template ["
							+ template + "]");
				}

				notifications.add(NotificationServiceProvider.provide()
						.addNotification(template));
			} else {
				for (NotificationModeType mode : setting.selected) {
					notification = JsonableHelper
							.copy(template, new Notification()).mode(mode);

					switch (mode) {
					case NotificationModeTypeEmail:
						EmailHelper.sendEmail(user.email, UserHelper.name(user),
								title, content, false);
						break;
					case NotificationModeTypePush:
						List<PushToken> tokens = PushTokenServiceProvider
								.provide().getUserPushTokens(user);

						Jsonable data = new Jsonable() {

							@Override
							public JsonObject toJson () {
								JsonObject object = super.toJson();

								object.addProperty("code", meta.code);

								if (values != null) {
									Object value;
									DataType slim;
									for (String key : values.keySet()) {
										value = values.get(key);

										if (value instanceof DataType) {
											slim = PersistenceHelper.slim(
													DataType.class,
													(DataType) value, null);

											object.add(key, slim.toJson());
										}
									}
								}

								return object;
							}
						};

						for (PushToken pushToken : tokens) {
							PushHelper.push(pushToken, meta.name, content,
									data);
						}

						if (LOG.isLoggable(Level.FINE) && tokens.size() == 0) {
							LOG.fine("Could not push because no tokens found ["
									+ meta.name + "], [" + content
									+ "] payload approximation ["
									+ PushHelper.buildPayload(
											new PushToken().platform("android")
													.user(user)
													.value("approx-test-token"),
											new AndroidMessage()
													.title(meta.name)
													.body(content),
											data)
									+ "]");
						}

						break;
					case NotificationModeTypeSms:
						break;
					}

					notifications.add(NotificationServiceProvider.provide()
							.addNotification(notification));
				}
			}
		}

		return notifications;
	}

}
