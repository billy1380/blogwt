//  
//  BlockUsersActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.logging.Logger;

import com.willshex.blogwt.shared.api.user.call.BlockUsersRequest;
import com.willshex.blogwt.shared.api.user.call.BlockUsersResponse;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class BlockUsersActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(BlockUsersActionHandler.class.getName());

	public BlockUsersResponse handle (BlockUsersRequest input) {
		LOG.finer("Entering blockUsers");
		BlockUsersResponse output = new BlockUsersResponse();
		try {
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting blockUsers");
		return output;
	}
}