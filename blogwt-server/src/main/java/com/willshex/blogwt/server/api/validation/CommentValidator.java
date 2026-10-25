//
//  CommentValidator.java
//  blogwt
//
//  Created by William Shakour on October 24, 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import com.willshex.blogwt.server.service.comment.CommentServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Comment;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.Comment;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.CommentHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.server.ServiceException;

public class CommentValidator extends ApiValidator {
	private static final String TYPE = Comment.class.getSimpleName();

	public static Comment validate (Comment comment, String name)
			throws InputValidationException {
		if (comment == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, TYPE + ": " + name);

		notNull(comment.body, String.class, name + ".body");

		validateLength(comment.body, CommentHelper.MIN_BODY_LENGTH,
				CommentHelper.MAX_BODY_LENGTH, name + ".body");

		return comment;
	}

	public static Comment lookup (Comment comment, String name)
			throws InputValidationException {
		if (comment == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, TYPE + ": " + name);

		if (comment.id == null) throwServiceError(InputValidationException.class,
				ApiError.DataTypeNoLookup, TYPE + ": " + name);

		Comment lookupComment = CommentServiceProvider.provide()
				.getComment(comment.id);

		if (lookupComment == null)
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, TYPE + ": " + name);

		return lookupComment;
	}

	/**
	 * The post or page, exactly one of them, that the comments are on, which
	 * must have comments switched on
	 */
	public static DataType lookupParent (Post post, Page page, String name)
			throws ServiceException {
		if ((post == null) == (page == null))
			throwServiceError(InputValidationException.class,
					ApiError.NotEnoughData,
					"exactly one of " + name + ".post or " + name + ".page");

		PropertyValidator.ensureTrue(PropertyHelper.POST_COMMENTS_ENABLED);

		DataType parent;
		Boolean enabled;

		if (post != null) {
			Post lookup = PostValidator.lookup(post, name + ".post");
			parent = lookup;
			enabled = lookup.commentsEnabled;
		} else {
			Page lookup = PageValidator.lookup(page, name + ".page");
			parent = lookup;
			enabled = lookup.commentsEnabled;
		}

		if (!Boolean.TRUE.equals(enabled))
			throwServiceError(InputValidationException.class,
					ApiError.PropertyDisallowed, name + ".commentsEnabled");

		return parent;
	}

	/**
	 * What a reaction is on, a comment, post or page, loaded and checked, by
	 * the type name given with it
	 */
	public static DataType lookupReactionParent (DataType parent,
			String parentType, String name) throws ServiceException {
		notNull(parent, DataType.class, name);
		notNull(parentType, String.class, name + "Type");

		if (Comment.class.getSimpleName().equals(parentType)) {
			PropertyValidator.ensureTrue(PropertyHelper.POST_COMMENTS_ENABLED);

			Comment comment = new Comment();
			comment.id = parent.id;

			return lookup(comment, name);
		} else if (Post.class.getSimpleName().equals(parentType)) {
			Post post = new Post();
			post.id = parent.id;

			post = PostValidator.lookup(post, name);
			ensureReactable(post.reactionsEnabled, name);

			return post;
		} else if (Page.class.getSimpleName().equals(parentType)) {
			Page page = new Page();
			page.id = parent.id;

			page = PageValidator.lookup(page, name);
			ensureReactable(page.reactionsEnabled, name);

			return page;
		}

		throwServiceError(InputValidationException.class,
				ApiError.TokenNoMatch, name + "Type");

		return null;
	}

	private static void ensureReactable (Boolean enabled, String name)
			throws ServiceException {
		if (!Boolean.TRUE.equals(enabled))
			throwServiceError(InputValidationException.class,
					ApiError.PropertyDisallowed, name + ".reactionsEnabled");
	}

	public static boolean isModerated (DataType parent) {
		return parent instanceof Post
				? Boolean.TRUE.equals(((Post) parent).commentsModerated)
				: Boolean.TRUE.equals(((Page) parent).commentsModerated);
	}
}
