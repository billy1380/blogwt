//  
//  GetTagsActionHandler.java
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
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.service.tag.TagServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.GetTagsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetTagsResponse;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.Tag;

public final class GetTagsActionHandler
		extends ActionHandler<GetTagsRequest, GetTagsResponse> {
	private static final Logger LOG = Logger
			.getLogger(GetTagsActionHandler.class.getName());
	@Override
	protected void handle(GetTagsRequest input, GetTagsResponse output)
			throws Exception {
		ApiValidator.request(input, GetTagsRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		if (input.session != null) {
			try {
				output.session = input.session = SessionValidator
						.lookupCheckAndExtend(input.session, "input.session");
			} catch (Exception e) {
				output.session = input.session = null;
			}
		}

		output.tags = TagServiceProvider.provide().getTags();

		if (output.tags != null) {
			for (Tag tag : output.tags) {
				tag.posts = PersistenceHelper.typeList(Post.class,
						tag.postKeys);
			}
		}
	}
	@Override
	protected GetTagsResponse newOutput() {
		return new GetTagsResponse();
	}
	@Override
	protected Logger logger() {
		return LOG;
	}
}