//  
//  GetResourcesActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesResponse;
import com.willshex.blogwt.shared.api.datatype.ResourceSortType;
import com.willshex.blogwt.shared.helper.PagerHelper;

public final class GetResourcesActionHandler
		extends ActionHandler<GetResourcesRequest, GetResourcesResponse> {
	private static final Logger LOG = Logger
			.getLogger(GetResourcesActionHandler.class.getName());
	@Override
	protected void handle (GetResourcesRequest input,
			GetResourcesResponse output) throws Exception {
		ApiValidator.request(input, GetResourcesRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		if (input.pager == null) {
			input.pager = PagerHelper.createDefaultPager();
		}

		output.resources = ResourceServiceProvider.provide().getResources(
				input.pager.start, input.pager.count,
				ResourceSortType.fromString(input.pager.sortBy),
				SortDirectionType.SortDirectionTypeDescending);

		output.pager = PagerHelper.moveForward(input.pager);
	}
	@Override
	protected GetResourcesResponse newOutput () {
		return new GetResourcesResponse();
	}
	@Override
	protected Logger logger () {
		return LOG;
	}
}