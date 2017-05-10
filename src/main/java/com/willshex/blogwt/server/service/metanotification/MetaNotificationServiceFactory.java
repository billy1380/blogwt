// 
//  MetaNotificationServiceFactory.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.metanotification;

final class MetaNotificationServiceFactory {

	/**
	* @return
	*/
	public static IMetaNotificationService createNewMetaNotificationService () {
		return new MetaNotificationService();
	}

}