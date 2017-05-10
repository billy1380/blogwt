// 
//  NotificationService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.notification;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.shared.api.datatype.Notification;

final class NotificationService implements INotificationService {
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

}