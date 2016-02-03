//
//  PageValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 1 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import java.util.ArrayList;
import java.util.List;

import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PageValidator {
	private static final String type = Page.class.getSimpleName();

	public static Page validate (Page page, String name)
			throws InputValidationException {
		if (page == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);

		List<Post> posts = new ArrayList<Post>();
		for (Post post : page.posts) {
			posts.add(PostValidator.lookup(post, name + ".posts[n]"));
		}

		page.posts = posts;

		if (page.parent != null) {
			page.parent = lookup(page.parent, name + ".parent");
		}

		page.owner = UserValidator.lookup(page.owner, name + ".owner");

		return page;
	}

	public static Page lookup (Page page, String name)
			throws InputValidationException {
		if (page == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);

		boolean isIdLookup = false, isSlugLookup = false;
		if (page.id != null) {
			isIdLookup = true;
		} else if (page.slug != null) {
			isSlugLookup = true;
		}

		if (!(isIdLookup || isSlugLookup))
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNoLookup, type + ": " + name);

		Page lookupPage;

		if (isIdLookup) {
			lookupPage = PageServiceProvider.provide().getPage(page.id);
		} else {
			lookupPage = PageServiceProvider.provide().getSlugPage(page.slug,
					Boolean.FALSE);
		}

		if (lookupPage == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, type + ": " + name);

		return lookupPage;
	}
}
