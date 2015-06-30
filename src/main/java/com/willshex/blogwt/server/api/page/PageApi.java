//  
//  PageApi.java
//  xsdwsdl2code
//
//  Created by William Shakour on June 21, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.page;

import java.util.logging.Logger;

import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.page.call.CreatePageRequest;
import com.willshex.blogwt.shared.api.page.call.CreatePageResponse;
import com.willshex.blogwt.shared.api.page.call.DeletePageRequest;
import com.willshex.blogwt.shared.api.page.call.DeletePageResponse;
import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageResponse;
import com.willshex.blogwt.shared.api.page.call.GetPagesRequest;
import com.willshex.blogwt.shared.api.page.call.GetPagesResponse;
import com.willshex.gson.json.service.server.ActionHandler;
import com.willshex.gson.json.service.shared.StatusType;

public final class PageApi extends ActionHandler {
	private static final Logger LOG = Logger.getLogger(Page.class.getName());

	public DeletePageResponse deletePage (DeletePageRequest input) {
		LOG.finer("Entering deletePage");
		DeletePageResponse output = new DeletePageResponse();
		try {
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting deletePage");
		return output;
	}

	public CreatePageResponse createPage (CreatePageRequest input) {
		LOG.finer("Entering createPage");
		CreatePageResponse output = new CreatePageResponse();
		try {
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting createPage");
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
}