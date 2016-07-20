//  
//  GetResourcesActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class GetResourcesActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(GetResourcesActionHandler.class.getName());

	public GetResourcesResponse handle (GetResourcesRequest input) {
		LOG.finer("Entering getResources");
		GetResourcesResponse output = new GetResourcesResponse();
		try {
			ApiValidator.notNull(input, GetResourcesRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator
					.lookupAndExtend(input.session, "input.session");

			output.resources = ResourceServiceProvider.provide().getResources(
					input.pager.start, input.pager.count, null,
					SortDirectionType.SortDirectionTypeDescending);

			output.pager = PagerHelper.moveForward(input.pager);

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getResources");
		return output;
	}
}