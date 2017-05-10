// 
//  GetNotificationsActionHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.notification.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationsResponse;

public final class GetNotificationsActionHandler extends
		ActionHandler<GetNotificationsRequest, GetNotificationsResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetNotificationsActionHandler.class.getName());

	@Override
	public void handle (GetNotificationsRequest input,
			GetNotificationsResponse output) throws Exception {}

	@Override
	protected GetNotificationsResponse newOutput () {
		return new GetNotificationsResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}