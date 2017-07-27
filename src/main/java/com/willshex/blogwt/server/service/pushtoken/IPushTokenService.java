// 
//  IPushTokenService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.pushtoken;

import java.util.List;

import com.willshex.blogwt.server.service.ISortable;
import com.willshex.blogwt.shared.api.datatype.PushToken;
import com.willshex.blogwt.shared.api.datatype.PushTokenSortType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.service.IService;

public interface IPushTokenService
		extends IService, ISortable<PushTokenSortType> {

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

	/**
	 * Get user push tokens
	 * @param user
	 * @return
	 */
	public List<PushToken> getUserPushTokens (User user);

	public default String map (PushTokenSortType sortBy) {
		String mapped = sortBy.toString();

		if (sortBy == PushTokenSortType.PushTokenSortTypeUser) {
			mapped += "Key";
		}

		return mapped;
	}

}
