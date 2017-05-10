// 
//  INotificationSettingService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.notificationsetting;

import com.willshex.blogwt.shared.api.datatype.NotificationSetting;
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

}