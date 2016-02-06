//  
//  FollowUsersActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.logging.Logger;

import com.willshex.blogwt.shared.api.user.call.FollowUsersRequest;
import com.willshex.blogwt.shared.api.user.call.FollowUsersResponse;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class FollowUsersActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(FollowUsersActionHandler.class.getName());

	public FollowUsersResponse handle (FollowUsersRequest input) {
		LOG.finer("Entering followUsers");
		FollowUsersResponse output = new FollowUsersResponse();
		try {
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting followUsers");
		return output;
	}
}