//  
//  GetResourceActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.ResourceValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.shared.api.blog.call.GetResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourceResponse;

public final class GetResourceActionHandler
		extends ActionHandler<GetResourceRequest, GetResourceResponse> {
	private static final Logger LOG = Logger
			.getLogger(GetResourceActionHandler.class.getName());
	@Override
	protected void handle (GetResourceRequest input, GetResourceResponse output)
			throws Exception {
		ApiValidator.request(input, GetResourceRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		output.resource = input.resource = ResourceValidator
				.lookup(input.resource, "input.resource");
	}
	@Override
	protected GetResourceResponse newOutput () {
		return new GetResourceResponse();
	}
	@Override
	protected Logger logger () {
		return LOG;
	}
}