//  
//  PageApi.java
//  xsdwsdl2code
//
//  Created by William Shakour on June 21, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.page;

import java.util.logging.Logger;

import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageResponse;
import com.willshex.blogwt.shared.api.page.call.GetPagesRequest;
import com.willshex.blogwt.shared.api.page.call.GetPagesResponse;
import com.willshex.gson.json.service.server.ActionHandler;
import com.willshex.gson.json.service.shared.StatusType;

public final class PageApi extends ActionHandler {
	private static final Logger LOG = Logger.getLogger(PageApi.class.getName());

	public GetPageResponse getPage (GetPageRequest input) {
		LOG.finer("Entering getPage");
		GetPageResponse output = new GetPageResponse();
		try {
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getPage");
		return output;
	}

	public GetPagesResponse getPages (GetPagesRequest input) {
		LOG.finer("Entering getPages");
		GetPagesResponse output = new GetPagesResponse();
		try {
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getPages");
		return output;
	}
}