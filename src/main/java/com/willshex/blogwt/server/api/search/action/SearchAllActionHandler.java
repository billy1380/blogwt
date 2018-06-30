//  
//  SearchAllActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.search.action;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.helper.SearchHelper;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.search.ISearch;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.search.call.SearchAllRequest;
import com.willshex.blogwt.shared.api.search.call.SearchAllResponse;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.server.InputValidationException;

public final class SearchAllActionHandler
		extends ActionHandler<SearchAllRequest, SearchAllResponse> {
	private static final Logger LOG = Logger
			.getLogger(SearchAllActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@SuppressWarnings("unchecked")
	@Override
	protected void handle (SearchAllRequest input, SearchAllResponse output)
			throws Exception {
		ApiValidator.request(input, SearchAllRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		try {
			output.session = input.session = SessionValidator
					.lookupCheckAndExtend(input.session, "input.session");
		} catch (InputValidationException ex) {
			output.session = input.session = null;
		}

		if (input.query == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, "String: input.query");

		output.posts = SearchHelper.pagedAndSorted(
				(ISearch<Post>) PostServiceProvider.provide(), input.query,
				Integer.valueOf(0), SearchHelper.SHORT_SEARCH_LIMIT, null,
				null);

		Map<Key<User>, User> users = new HashMap<Key<User>, User>();
		if (output.posts != null) {
			for (Post post : output.posts) {
				if (users.get(post.authorKey) == null) {
					users.put(post.authorKey, UserServiceProvider.provide()
							.getUser(keyToId(post.authorKey)));
				}

				post.author = users.get(post.authorKey);
			}
		}

		output.pages = SearchHelper.pagedAndSorted(
				(ISearch<Page>) PageServiceProvider.provide(), input.query,
				Integer.valueOf(0), SearchHelper.SHORT_SEARCH_LIMIT, null,
				null);

		if (output.pages != null) {
			for (Page page : output.pages) {
				if (users.get(page.ownerKey) == null) {
					users.put(page.ownerKey, UserServiceProvider.provide()
							.getUser(keyToId(page.ownerKey)));
				}

				page.owner = users.get(page.ownerKey);
			}
		}

		output.users = SearchHelper.pagedAndSorted(
				(ISearch<User>) UserServiceProvider.provide(), input.query,
				Integer.valueOf(0), SearchHelper.SHORT_SEARCH_LIMIT, null,
				null);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.IClearSensitive#clearSensitiveFields(com.
	 * willshex.blogwt.shared.api.Response) */
	@Override
	public void clearSensitiveFields (SearchAllResponse output) {
		super.clearSensitiveFields(output);

		if (output.pages != null) {
			for (Page page : output.pages) {
				page.owner = UserHelper.stripPassword(page.owner);
			}
		}

		if (output.posts != null) {
			for (Post post : output.posts) {
				post.author = UserHelper.stripPassword(post.author);
			}
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected SearchAllResponse newOutput () {
		return new SearchAllResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}
}
