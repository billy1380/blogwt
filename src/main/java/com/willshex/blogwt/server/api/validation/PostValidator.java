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
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.server.ServiceException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostValidator extends ApiValidator {
	private static final String TYPE = Post.class.getSimpleName();
	private static final Processor<Post> LOOKUP = new Processor<Post>() {

		@Override
		public Post process (Post item, String name)
				throws InputValidationException {
			return lookup(item, name);
		}
	};

	public static Post validate (Post post, String name)
			throws InputValidationException {
		if (post == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, TYPE + ": " + name);

		return post;
	}

	public static Post lookup (Post post, String name)
			throws InputValidationException {
		if (post == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, TYPE + ": " + name);

		boolean isIdLookup = false, isSlugLookup = false;
		if (post.id != null) {
			isIdLookup = true;
		} else if (post.slug != null) {
			isSlugLookup = true;
		}

		if (!(isIdLookup || isSlugLookup))
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNoLookup, TYPE + ": " + name);

		Post lookupPost;

		if (isIdLookup) {
			lookupPost = PostServiceProvider.provide().getPost(post.id);
		} else {
			lookupPost = PostServiceProvider.provide().getSlugPost(post.slug);
		}

		if (lookupPost == null)
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, TYPE + ": " + name);

		return lookupPost;
	}

	/**
	 * @param posts
	 * @return 
	 * @throws InputValidationException 
	 */
	public static <T extends Iterable<Post>> T lookupAll (T posts, String name)
			throws ServiceException {
		return processAll(false, posts, LOOKUP, TYPE, name);
	}

	public static Post viewable (Post post, Session session, String name)
			throws InputValidationException {
		if (post.published == null && session == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.UnpublishedPost, TYPE + ": " + name);

		return post;
	}

}
