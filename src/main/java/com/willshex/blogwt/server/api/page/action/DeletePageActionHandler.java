//  
//  DeletePageActionHandler.java
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
import com.willshex.blogwt.shared.api.page.call.DeletePageRequest;
import com.willshex.blogwt.shared.api.page.call.DeletePageResponse;
import com.willshex.blogwt.shared.helper.PermissionHelper;

public final class DeletePageActionHandler
		extends ActionHandler<DeletePageRequest, DeletePageResponse> {
	private static final Logger LOG = Logger
			.getLogger(DeletePageActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (DeletePageRequest input, DeletePageResponse output)
			throws Exception {
		ApiValidator.request(input, DeletePageRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.authorisation(input.session.user,
				Arrays.asList(PermissionServiceProvider.provide()
						.getCodePermission(PermissionHelper.MANAGE_PAGES)),
				"input.session.user");

		input.page = PageValidator.lookup(input.page, "input.page");

		PageServiceProvider.provide().deletePage(input.page);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected DeletePageResponse newOutput () {
		return new DeletePageResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}
}