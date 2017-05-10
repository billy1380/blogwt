// 
//  INotificationService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.notification;

import com.willshex.blogwt.shared.api.datatype.Notification;
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

}