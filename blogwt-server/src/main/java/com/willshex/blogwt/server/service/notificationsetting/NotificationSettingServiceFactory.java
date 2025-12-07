// 
//  NotificationSettingServiceFactory.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.notificationsetting;

final class NotificationSettingServiceFactory {

	/**
	* @return
	*/
	public static INotificationSettingService createNewNotificationSettingService () {
		return new NotificationSettingService();
	}

}