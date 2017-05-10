// 
//  NotificationServiceFactory.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.notification;

final class NotificationServiceFactory {

	/**
	* @return
	*/
	public static INotificationService createNewNotificationService () {
		return new NotificationService();
	}

}