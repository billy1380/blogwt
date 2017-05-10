// 
//  UpdateNotificationSettingsActionHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.notification.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.shared.api.notification.call.UpdateNotificationSettingsRequest;
import com.willshex.blogwt.shared.api.notification.call.UpdateNotificationSettingsResponse;

public final class UpdateNotificationSettingsActionHandler extends
		ActionHandler<UpdateNotificationSettingsRequest, UpdateNotificationSettingsResponse> {

	private static final Logger LOG = Logger
			.getLogger(UpdateNotificationSettingsActionHandler.class.getName());

	@Override
	public void handle (UpdateNotificationSettingsRequest input,
			UpdateNotificationSettingsResponse output) throws Exception {}

	@Override
	protected UpdateNotificationSettingsResponse newOutput () {
		return new UpdateNotificationSettingsResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}