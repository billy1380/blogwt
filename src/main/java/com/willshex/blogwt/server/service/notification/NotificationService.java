// 
//  NotificationService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.notification;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.service.ISortable;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Notification;
import com.willshex.blogwt.shared.api.datatype.NotificationSetting;
import com.willshex.blogwt.shared.api.datatype.NotificationSortType;
import com.willshex.blogwt.shared.api.datatype.User;

final class NotificationService
		implements INotificationService, ISortable<NotificationSortType> {
	public String getName () {
		return NAME;
	}

	@Override
	public Notification getNotification (Long id) {
		return load().id(id.longValue()).now();
	}

	private LoadType<Notification> load () {
		return provide().load().type(Notification.class);
	}

	@Override
	public Notification addNotification (Notification notification) {
		if (notification.created == null) {
			notification.created = new Date();
		}

		if (notification.meta != null) {
			notification.metaKey = Key.create(notification.meta);
		}

		if (notification.target != null) {
			notification.targetKey = Key.create(notification.target);
		}

		if (notification.sender != null) {
			notification.senderKey = Key.create(notification.sender);
		}

		Key<Notification> key = provide().save().entity(notification).now();
		notification.id = Long.valueOf(key.getId());

		return notification;
	}

	@Override
	public Notification updateNotification (Notification notification) {
		throw new UnsupportedOperationException(
				"Notifications cannot be updated");
	}

	@Override
	public void deleteNotification (Notification notification) {
		provide().delete().entity(notification).now();
	}

	@Override
	public List<Notification> getUserNotifications (User user, Integer start,
			Integer count, NotificationSortType sortBy,
			SortDirectionType sortDirection) {
		return PersistenceHelper.pagedAndSorted(load().filter(
				map(NotificationSortType.NotificationSortTypeTarget), user),
				start, count, sortBy, this, sortDirection);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.ISortable#map(java.lang.Enum) */
	@Override
	public String map (NotificationSortType sortBy) {
		String mapped = sortBy.toString();

		if (sortBy == NotificationSortType.NotificationSortTypeTarget) {
			mapped += "Key";
		}

		return mapped;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.notification.INotificationService#
	 * updateNotificationSettings(java.util.Collection) */
	@Override
	public List<NotificationSetting> updateNotificationSettings (
			Collection<NotificationSetting> notificationSettings) {
		for (NotificationSetting setting : notificationSettings) {
			if (setting.user != null) {
				setting.userKey = Key.create(setting.user);
			}

			if (setting.meta != null) {
				setting.metaKey = Key.create(setting.meta);
			}
		}
		provide().save().entities(notificationSettings).now();
		return notificationSettings instanceof List
				? (List<NotificationSetting>) notificationSettings
				: new ArrayList<>(notificationSettings);
	}

}