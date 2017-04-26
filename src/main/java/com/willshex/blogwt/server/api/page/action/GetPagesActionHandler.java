//  
//  GetPagesActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.page.action;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
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

public final class GetPagesActionHandler
		extends ActionHandler<GetPagesRequest, GetPagesResponse> {
	private static final Logger LOG = Logger
			.getLogger(GetPagesActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (GetPagesRequest input, GetPagesResponse output)
			throws Exception {

		ApiValidator.notNull(input, GetPagesRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		if (input.pager == null) {
			input.pager = PagerHelper.createDefaultPager();
		}

		if (input.query == null || input.query.trim().length() == 0) {
			output.pages = PageServiceProvider.provide().getPages(
					input.includePosts, input.pager.start, input.pager.count,
					PageSortType.fromString(input.pager.sortBy),
					input.pager.sortDirection);
		} else {
			output.pages = PageServiceProvider.provide().getPartialSlugPages(
					input.query, input.includePosts, input.pager.start,
					input.pager.count,
					PageSortType.fromString(input.pager.sortBy),
					input.pager.sortDirection);
		}

		Map<Long, User> owners = new HashMap<Long, User>();
		Long id;
		for (Page page : output.pages) {
			id = keyToId(page.ownerKey);
			page.owner = owners.get(id);
			if (page.owner == null) {
				owners.put(id, page.owner = UserHelper.stripSensitive(
						UserServiceProvider.provide().getUser(id)));
			}
		}

		output.pager = PagerHelper.moveForward(input.pager);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected GetPagesResponse newOutput () {
		return new GetPagesResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}
}