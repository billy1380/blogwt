//
//  PostValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 17 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.json.service.server.InputValidationException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostValidator {
	private static final String type = Post.class.getSimpleName();

	public static Post validate (Post post, String name)
			throws InputValidationException {
		if (post == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);
		
		return post;
	}

	public static Post lookup (Post post, String name)
			throws InputValidationException {
		if (post == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);

		boolean isIdLookup = false, isSlugLookup = false;
		if (post.id != null) {
			isIdLookup = true;
		} else if (post.slug != null) {
			isSlugLookup = true;
		}

		if (!(isIdLookup || isSlugLookup))
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNoLookup, type + ": " + name);

		Post lookupPost;

		if (isIdLookup) {
			lookupPost = PostServiceProvider.provide().getPost(post.id);
		} else {
			lookupPost = PostServiceProvider.provide().getSlugPost(post.slug);
		}

		if (lookupPost == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, type + ": " + name);

		return lookupPost;
	}

}
