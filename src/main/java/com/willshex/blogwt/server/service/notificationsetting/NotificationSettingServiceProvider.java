// 
//  NotificationSettingServiceProvider.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.notificationsetting;

import com.willshex.service.ServiceDiscovery;

final public class NotificationSettingServiceProvider {

	/**
	* @return
	*/
	public static INotificationSettingService provide () {
		INotificationSettingService notificationSettingService = null;

		if ((notificationSettingService = (INotificationSettingService) ServiceDiscovery
				.getService(INotificationSettingService.NAME)) == null) {
			notificationSettingService = NotificationSettingServiceFactory
					.createNewNotificationSettingService();
			ServiceDiscovery.registerService(notificationSettingService);
		}

		return notificationSettingService;
	}

}