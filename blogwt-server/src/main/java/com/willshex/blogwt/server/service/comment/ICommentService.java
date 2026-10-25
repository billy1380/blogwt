//
//  ICommentService.java
//  blogwt
//
//  Created by William Shakour on October 24, 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.comment;

import java.util.List;

import com.googlecode.objectify.Key;

import com.willshex.blogwt.shared.api.datatype.Comment;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.api.datatype.Reaction;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.service.IService;

public interface ICommentService extends IService {

	public static final String NAME = "blogwt.comment";

	public Comment getComment (Long id);

	public Comment addComment (Comment comment);

	public Comment updateComment (Comment comment);

	public void deleteComment (Comment comment);

	/**
	 * Comments of a post or page, oldest first, that the viewer can see: the
	 * approved ones, their own, and all of them for a moderator
	 */
	public List<Comment> getVisibleComments (DataType parent, User viewer,
			boolean moderator);

	/**
	 * Comments for moderation, newest first, any of the filters can be null
	 */
	public List<Comment> getComments (DataType parent, User owner,
			Boolean pending);

	/**
	 * A window of the comments, with owner and reactions filled in
	 */
	public List<Comment> inflateAll (List<Comment> comments, Integer start,
			Integer count);

	/**
	 * Fills in the owner and reactions of the comment.
	 */
	public Comment inflate (Comment comment);

	/**
	 * Fills in a title only copy of the post or page the comment is on
	 */
	public Comment inflateParent (Comment comment);

	public Reaction getReaction (Long id);

	public Reaction getReaction (DataType parent, User owner, String emoticon);

	/**
	 * The reactions on a comment, post or page, oldest first, with owners
	 */
	public List<Reaction> getReactions (DataType parent);

	public List<Reaction> getReactions (Key<DataType> parentKey);

	public Reaction addReaction (Reaction reaction);

	public void deleteReaction (Reaction reaction);

}
