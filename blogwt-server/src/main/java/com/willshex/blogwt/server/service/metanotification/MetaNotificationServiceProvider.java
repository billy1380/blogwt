// 
//  MetaNotificationServiceProvider.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.metanotification;

import com.willshex.service.ServiceDiscovery;

final public class MetaNotificationServiceProvider {

	/**
	* @return
	*/
	public static IMetaNotificationService provide () {
		IMetaNotificationService metaNotificationService = null;

		if ((metaNotificationService = (IMetaNotificationService) ServiceDiscovery
				.getService(IMetaNotificationService.NAME)) == null) {
			metaNotificationService = MetaNotificationServiceFactory
					.createNewMetaNotificationService();
			ServiceDiscovery.registerService(metaNotificationService);
		}

		return metaNotificationService;
	}

}