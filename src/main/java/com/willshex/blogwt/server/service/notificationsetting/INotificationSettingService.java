// 
//  INotificationSettingService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.notificationsetting;

import java.util.List;

import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.NotificationSetting;
import com.willshex.blogwt.shared.api.datatype.NotificationSettingSortType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.service.IService;

public interface INotificationSettingService extends IService {

	public static final String NAME = "blogwt.notificationsetting";

	/**
	* @param id
	* @return
	*/
	public NotificationSetting getNotificationSetting (Long id);

	/**
	* @param notificationSetting
	* @return
	*/
	public NotificationSetting addNotificationSetting (
			NotificationSetting notificationSetting);

	/**
	* @param notificationSetting
	* @return
	*/
	public NotificationSetting updateNotificationSetting (
			NotificationSetting notificationSetting);

	/**
	* @param notificationSetting
	*/
	public void deleteNotificationSetting (
			NotificationSetting notificationSetting);

	/**
	 * Get user notification settings
	 * @param user
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<NotificationSetting> getUserNotificationSettings (User user,
			Integer start, Integer count, NotificationSettingSortType sortBy,
			SortDirectionType sortDirection);

	/**
	 * Get meta user notification setting
	 * @param metaNotification
	 * @param user
	 * @return
	 */
	public NotificationSetting getMetaUserNotificationSetting (
			MetaNotification metaNotification, User user);

}