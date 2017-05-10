// 
//  PushTokenServiceFactory.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.pushtoken;

final class PushTokenServiceFactory {

	/**
	* @return
	*/
	public static IPushTokenService createNewPushTokenService () {
		return new PushTokenService();
	}

}