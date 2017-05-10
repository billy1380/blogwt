// 
//  GetMetaNotificationsActionHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.notification.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationsResponse;

public final class GetMetaNotificationsActionHandler extends
		ActionHandler<GetMetaNotificationsRequest, GetMetaNotificationsResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetMetaNotificationsActionHandler.class.getName());

	@Override
	public void handle (GetMetaNotificationsRequest input,
			GetMetaNotificationsResponse output) throws Exception {}

	@Override
	protected GetMetaNotificationsResponse newOutput () {
		return new GetMetaNotificationsResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}