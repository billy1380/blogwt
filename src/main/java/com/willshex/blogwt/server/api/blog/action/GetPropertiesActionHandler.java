// 
//  GetPropertiesActionHandler.java
//  blogwt
// 
//  Created by William Shakour on April 27, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.shared.api.blog.call.GetPropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPropertiesResponse;
import com.willshex.gson.web.service.server.ActionHandler;

public final class GetPropertiesActionHandler
		extends ActionHandler<GetPropertiesRequest, GetPropertiesResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetPropertiesActionHandler.class.getName());

	@Override
	public void handle (GetPropertiesRequest input,
			GetPropertiesResponse output) throws Exception {}

	@Override
	protected GetPropertiesResponse newOutput () {
		return new GetPropertiesResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}