// 
//  NotificationSettingService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.notificationsetting;

import static com.willshex.blogwt.server.helper.PersistenceHelper.id;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.NotificationSetting;
import com.willshex.blogwt.shared.api.datatype.NotificationSettingSortType;
import com.willshex.blogwt.shared.api.datatype.User;

final class NotificationSettingService implements INotificationSettingService {
	public String getName() {
		return NAME;
	}

	@Override
	public NotificationSetting getNotificationSetting(Long id) {
		return id(load(), id);
	}

	private LoadType<NotificationSetting> load() {
		return provide().load().type(NotificationSetting.class);
	}

	@Override
	public NotificationSetting addNotificationSetting(
			NotificationSetting notificationSetting) {
		if (notificationSetting.created == null) {
			notificationSetting.created = new Date();
		}

		if (notificationSetting.meta != null) {
			notificationSetting.metaKey = ObjectifyService.key(notificationSetting.meta);
		}

		if (notificationSetting.user != null) {
			notificationSetting.userKey = ObjectifyService.key(notificationSetting.user);
		}

		Key<NotificationSetting> key = provide().save()
				.entity(notificationSetting).now();
		notificationSetting.id = Long.valueOf(key.getId());

		return notificationSetting;
	}

	@Override
	public NotificationSetting updateNotificationSetting(
			NotificationSetting notificationSetting) {
		provide().save().entity(notificationSetting).now();
		return notificationSetting;
	}

	@Override
	public void deleteNotificationSetting(
			NotificationSetting notificationSetting) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.notificationsetting.
	 * INotificationSettingService#getUserNotificationSettings(com.willshex.
	 * blogwt.shared.api.datatype.User, java.lang.Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.NotificationSettingSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType)
	 */
	@Override
	public List<NotificationSetting> getUserNotificationSettings(User user,
			Integer start, Integer count, NotificationSettingSortType sortBy,
			SortDirectionType sortDirection) {
		return PersistenceHelper.pagedAndSorted(load().filter(map(
				NotificationSettingSortType.NotificationSettingSortTypeUser),
				user), start, count, sortBy, this, sortDirection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.notificationsetting.
	 * INotificationSettingService#getMetaUserNotificationSetting(com.willshex.
	 * blogwt.shared.api.datatype.MetaNotification,
	 * com.willshex.blogwt.shared.api.datatype.User)
	 */
	@Override
	public NotificationSetting getMetaUserNotificationSetting(
			MetaNotification metaNotification, User user) {
		return PersistenceHelper.one(load().filter(map(
				NotificationSettingSortType.NotificationSettingSortTypeUser),
				user)
				.filter(map(
						NotificationSettingSortType.NotificationSettingSortTypeMeta),
						metaNotification));
	}

}
