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

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (GetResourceRequest input, GetResourceResponse output)
			throws Exception {
		ApiValidator.notNull(input, GetResourceRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		output.resource = input.resource = ResourceValidator
				.lookup(input.resource, "input.resource");
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected GetResourceResponse newOutput () {
		return new GetResourceResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}
}