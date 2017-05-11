// 
//  INotificationService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.notification;

import java.util.List;

import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Notification;
import com.willshex.blogwt.shared.api.datatype.NotificationSortType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.service.IService;

public interface INotificationService extends IService {

	public static final String NAME = "blogwt.notification";

	/**
	* @param id
	* @return
	*/
	public Notification getNotification (Long id);

	/**
	* @param notification
	* @return
	*/
	public Notification addNotification (Notification notification);

	/**
	* @param notification
	* @return
	*/
	public Notification updateNotification (Notification notification);

	/**
	* @param notification
	*/
	public void deleteNotification (Notification notification);

	/**
	 * Get user notifications
	 * @param user
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Notification> getUserNotifications (User user, Integer start,
			Integer count, NotificationSortType sortBy,
			SortDirectionType sortDirection);

}