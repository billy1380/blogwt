// 
//  SendAdhocNotificationActionHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.notification.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.shared.api.notification.call.SendAdhocNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.SendAdhocNotificationResponse;

public final class SendAdhocNotificationActionHandler extends
		ActionHandler<SendAdhocNotificationRequest, SendAdhocNotificationResponse> {

	private static final Logger LOG = Logger
			.getLogger(SendAdhocNotificationActionHandler.class.getName());

	@Override
	public void handle (SendAdhocNotificationRequest input,
			SendAdhocNotificationResponse output) throws Exception {}

	@Override
	protected SendAdhocNotificationResponse newOutput () {
		return new SendAdhocNotificationResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}