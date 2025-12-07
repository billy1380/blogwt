//  
//  GetRelatedPostsActionHandler.java
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
import com.willshex.blogwt.shared.api.blog.call.GetRelatedPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetRelatedPostsResponse;

public final class GetRelatedPostsActionHandler
		extends ActionHandler<GetRelatedPostsRequest, GetRelatedPostsResponse> {
	private static final Logger LOG = Logger
			.getLogger(GetRelatedPostsActionHandler.class.getName());
	@Override
	protected void handle (GetRelatedPostsRequest input,
			GetRelatedPostsResponse output) throws Exception {
		ApiValidator.request(input, GetRelatedPostsRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");
	}
	@Override
	protected GetRelatedPostsResponse newOutput () {
		return new GetRelatedPostsResponse();
	}
	@Override
	protected Logger logger () {
		return LOG;
	}

}