//  
//  GetRelatedPostsActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.shared.api.blog.call.GetRelatedPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetRelatedPostsResponse;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class GetRelatedPostsActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(GetRelatedPostsActionHandler.class.getName());

	public GetRelatedPostsResponse handle (GetRelatedPostsRequest input) {
		LOG.finer("Entering getRelatedPosts");
		GetRelatedPostsResponse output = new GetRelatedPostsResponse();
		try {
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getRelatedPosts");
		return output;
	}
}