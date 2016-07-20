//  
//  GetPagesActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.page.action;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.PageSortType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.page.call.GetPagesRequest;
import com.willshex.blogwt.shared.api.page.call.GetPagesResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class GetPagesActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(GetPagesActionHandler.class.getName());

	public GetPagesResponse handle (GetPagesRequest input) {
		LOG.finer("Entering getPages");
		GetPagesResponse output = new GetPagesResponse();
		try {
			ApiValidator.notNull(input, GetPagesRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator
					.lookupAndExtend(input.session, "input.session");

			if (input.pager == null) {
				input.pager = PagerHelper.createDefaultPager();
			}

			if (input.query == null || input.query.trim().length() == 0) {
				output.pages = PageServiceProvider.provide().getPages(
						input.includePosts, input.pager.start,
						input.pager.count,
						PageSortType.fromString(input.pager.sortBy),
						input.pager.sortDirection);
			} else {
				output.pages = PageServiceProvider.provide()
						.getPartialSlugPages(input.query, input.includePosts,
								input.pager.start, input.pager.count,
								PageSortType.fromString(input.pager.sortBy),
								input.pager.sortDirection);
			}

			Map<Long, User> owners = new HashMap<Long, User>();
			Long id;
			for (Page page : output.pages) {
				id = Long.valueOf(page.ownerKey.getId());
				page.owner = owners.get(id);
				if (page.owner == null) {
					owners.put(id, page.owner = UserHelper.stripSensitive(
							UserServiceProvider.provide().getUser(id)));
				}
			}

			output.pager = PagerHelper.moveForward(input.pager);

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getPages");
		return output;
	}
}