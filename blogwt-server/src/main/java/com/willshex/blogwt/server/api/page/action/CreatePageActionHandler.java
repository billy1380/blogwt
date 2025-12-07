//  
//  CreatePageActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.page.action;

import java.util.Arrays;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PageValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.shared.api.page.call.CreatePageRequest;
import com.willshex.blogwt.shared.api.page.call.CreatePageResponse;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.PostHelper;

public final class CreatePageActionHandler
		extends ActionHandler<CreatePageRequest, CreatePageResponse> {
	private static final Logger LOG = Logger
			.getLogger(CreatePageActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (CreatePageRequest input, CreatePageResponse output)
			throws Exception {
		ApiValidator.request(input, CreatePageRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.authorisation(input.session.user,
				Arrays.asList(PermissionServiceProvider.provide()
						.getCodePermission(PermissionHelper.MANAGE_PAGES)),
				"input.session.user");

		input.page = PageValidator.validate(input.page, "input.page");
		input.page.slug = PostHelper.slugify(input.page.title);

		output.page = PageServiceProvider.provide().addPage(input.page);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected CreatePageResponse newOutput () {
		return new CreatePageResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}
}