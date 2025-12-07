// 
//  PushTokenServiceProvider.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.pushtoken;

import com.willshex.service.ServiceDiscovery;

final public class PushTokenServiceProvider {

	/**
	* @return
	*/
	public static IPushTokenService provide () {
		IPushTokenService pushTokenService = null;

		if ((pushTokenService = (IPushTokenService) ServiceDiscovery
				.getService(IPushTokenService.NAME)) == null) {
			pushTokenService = PushTokenServiceFactory
					.createNewPushTokenService();
			ServiceDiscovery.registerService(pushTokenService);
		}

		return pushTokenService;
	}

}