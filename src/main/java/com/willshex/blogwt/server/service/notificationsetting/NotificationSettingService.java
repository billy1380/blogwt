// 
//  NotificationSettingService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.notificationsetting;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.shared.api.datatype.NotificationSetting;

final class NotificationSettingService implements INotificationSettingService {
	public String getName () {
		return NAME;
	}

	@Override
	public NotificationSetting getNotificationSetting (Long id) {
		return load().id(id.longValue()).now();
	}

	private LoadType<NotificationSetting> load () {
		return provide().load().type(NotificationSetting.class);
	}

	@Override
	public NotificationSetting addNotificationSetting (
			NotificationSetting notificationSetting) {
		if (notificationSetting.created == null) {
			notificationSetting.created = new Date();
		}

		if (notificationSetting.meta != null) {
			notificationSetting.metaKey = Key.create(notificationSetting.meta);
		}

		if (notificationSetting.user != null) {
			notificationSetting.userKey = Key.create(notificationSetting.user);
		}

		Key<NotificationSetting> key = provide().save()
				.entity(notificationSetting).now();
		notificationSetting.id = Long.valueOf(key.getId());

		return notificationSetting;
	}

	@Override
	public NotificationSetting updateNotificationSetting (
			NotificationSetting notificationSetting) {
		provide().save().entity(notificationSetting).now();
		return notificationSetting;
	}

	@Override
	public void deleteNotificationSetting (
			NotificationSetting notificationSetting) {
		throw new UnsupportedOperationException();
	}

}