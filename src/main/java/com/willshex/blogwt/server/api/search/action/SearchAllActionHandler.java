//  
//  SearchAllActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.search.action;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.search.call.SearchAllRequest;
import com.willshex.blogwt.shared.api.search.call.SearchAllResponse;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.shared.StatusType;

public final class SearchAllActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(SearchAllActionHandler.class.getName());

	public SearchAllResponse handle (SearchAllRequest input) {
		LOG.finer("Entering searchAll");
		SearchAllResponse output = new SearchAllResponse();
		try {
			ApiValidator.notNull(input, SearchAllRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			try {
				output.session = input.session = SessionValidator
						.lookupAndExtend(input.session, "input.session");
			} catch (InputValidationException ex) {
				output.session = input.session = null;
			}

			if (input.query == null)
				ApiValidator.throwServiceError(InputValidationException.class,
						ApiError.InvalidValueNull, "String: input.query");

			output.posts = PostServiceProvider.provide()
					.searchPosts(input.query);

			Map<Key<User>, User> users = new HashMap<Key<User>, User>();
			if (output.posts != null) {
				for (Post post : output.posts) {
					if (users.get(post.authorKey) == null) {
						users.put(post.authorKey, UserHelper.stripSensitive(
								UserServiceProvider.provide().getUser(
										Long.valueOf(post.authorKey.getId()))));
					}

					post.author = users.get(post.authorKey);
				}
			}

			output.pages = PageServiceProvider.provide()
					.searchPages(input.query);

			if (output.pages != null) {
				for (Page page : output.pages) {
					if (users.get(page.ownerKey) == null) {
						users.put(page.ownerKey, UserHelper.stripSensitive(
								UserServiceProvider.provide().getUser(
										Long.valueOf(page.ownerKey.getId()))));
					}

					page.owner = users.get(page.ownerKey);
				}
			}

			output.users = UserServiceProvider.provide()
					.searchUsers(input.query);

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting searchAll");
		return output;
	}
}