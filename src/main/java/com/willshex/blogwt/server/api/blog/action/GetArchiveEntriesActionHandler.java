//  
//  GetArchiveEntriesActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.shared.api.blog.call.GetArchiveEntriesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetArchiveEntriesResponse;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class GetArchiveEntriesActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(GetArchiveEntriesActionHandler.class.getName());

	public GetArchiveEntriesResponse handle (GetArchiveEntriesRequest input) {
		LOG.finer("Entering getArchiveEntries");
		GetArchiveEntriesResponse output = new GetArchiveEntriesResponse();
		try {
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getArchiveEntries");
		return output;
	}
}