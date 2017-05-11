// 
//  IPushTokenService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.pushtoken;

import com.willshex.blogwt.shared.api.datatype.PushToken;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.service.IService;

public interface IPushTokenService extends IService {

	public static final String NAME = "blogwt.pushtoken";

	/**
	* @param id
	* @return
	*/
	public PushToken getPushToken (Long id);

	/**
	* @param pushToken
	* @return
	*/
	public PushToken addPushToken (PushToken pushToken);

	/**
	* @param pushToken
	* @return
	*/
	public PushToken updatePushToken (PushToken pushToken);

	/**
	* @param pushToken
	*/
	public void deletePushToken (PushToken pushToken);

	/**
	 * Get user platform push token
	 * @param user
	 * @param platform
	 * @return
	 */
	public PushToken getUserPlatformPushToken (User user, String platform);

}