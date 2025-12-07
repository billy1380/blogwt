// 
//  NotificationServiceProvider.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.notification;

import com.willshex.service.ServiceDiscovery;

final public class NotificationServiceProvider {

	/**
	* @return
	*/
	public static INotificationService provide () {
		INotificationService notificationService = null;

		if ((notificationService = (INotificationService) ServiceDiscovery
				.getService(INotificationService.NAME)) == null) {
			notificationService = NotificationServiceFactory
					.createNewNotificationService();
			ServiceDiscovery.registerService(notificationService);
		}

		return notificationService;
	}

}