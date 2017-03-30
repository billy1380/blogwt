//  
//  SubmitRatingActionHandler.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.shared.api.blog.call.SubmitRatingRequest;
import com.willshex.blogwt.shared.api.blog.call.SubmitRatingResponse;
import com.willshex.gson.web.service.server.ActionHandler;

public final class SubmitRatingActionHandler
		extends ActionHandler<SubmitRatingRequest, SubmitRatingResponse> {

	private static final Logger LOG = Logger
			.getLogger(SubmitRatingActionHandler.class.getName());

	@Override
	public void handle (SubmitRatingRequest input, SubmitRatingResponse output)
			throws Exception {
		ApiValidator.notNull(input, SubmitRatingRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
	}

	@Override
	protected SubmitRatingResponse newOutput () {
		return new SubmitRatingResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}