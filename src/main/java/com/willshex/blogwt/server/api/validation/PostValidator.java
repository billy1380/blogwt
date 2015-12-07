//
//  PostValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 17 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import java.util.ArrayList;
import java.util.List;

import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.Session;
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

	/**
	 * @param posts
	 * @return 
	 * @throws InputValidationException 
	 */
	public static List<Post> lookupAll (List<Post> posts, String name)
			throws InputValidationException {
		if (posts == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + "[]: " + name);

		List<Post> lookupPosts = new ArrayList<Post>();

		for (Post post : posts) {
			lookupPosts.add(lookup(post, name + "[n]"));
		}

		return lookupPosts;
	}

	public static Post viewable (Post post, Session session, String name)
			throws InputValidationException {
		if (post.published == null && session == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.UnpublishedPost, type + ": " + name);

		return post;
	}

}
