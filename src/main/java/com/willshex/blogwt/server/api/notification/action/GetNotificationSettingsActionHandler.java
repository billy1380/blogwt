// 
//  GetNotificationSettingsActionHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.notification.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationSettingsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationSettingsResponse;

public final class GetNotificationSettingsActionHandler extends
		ActionHandler<GetNotificationSettingsRequest, GetNotificationSettingsResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetNotificationSettingsActionHandler.class.getName());

	@Override
	public void handle (GetNotificationSettingsRequest input,
			GetNotificationSettingsResponse output) throws Exception {}

	@Override
	protected GetNotificationSettingsResponse newOutput () {
		return new GetNotificationSettingsResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}