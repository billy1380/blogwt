//  
//  GetTagsActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.shared.api.blog.call.GetTagsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetTagsResponse;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class GetTagsActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(GetTagsActionHandler.class.getName());

	public GetTagsResponse handle (GetTagsRequest input) {
		LOG.finer("Entering getTags");
		GetTagsResponse output = new GetTagsResponse();
		try {
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getTags");
		return output;
	}
}