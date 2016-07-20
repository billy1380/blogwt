//  
//  GetPageActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.page.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PageValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.PersistenceService;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageResponse;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.shared.StatusType;

public final class GetPageActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(GetPageActionHandler.class.getName());

	public GetPageResponse handle (GetPageRequest input) {
		LOG.finer("Entering getPage");
		GetPageResponse output = new GetPageResponse();
		try {
			ApiValidator.notNull(input, GetPageRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			if (input.session != null) {
				try {
					output.session = input.session = SessionValidator
							.lookupAndExtend(input.session, "input.session");
				} catch (InputValidationException ex) {
					output.session = input.session = null;
				}
			}

			output.page = input.page = PageValidator.lookup(input.page,
					"input.page");

			if (output.page.parentKey != null) {
				output.page.parent = PageValidator.lookup(PersistenceService
						.dataType(Page.class, output.page.parentKey),
						"input.page.parent");
			}

			if (Boolean.TRUE.equals(input.includePosts)) {
				output.page = PageServiceProvider.provide()
						.getPage(input.page.id, input.includePosts);
			}

			output.page.owner = UserHelper
					.stripSensitive(UserServiceProvider.provide().getUser(
							Long.valueOf(output.page.ownerKey.getId())));

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getPage");
		return output;
	}
}