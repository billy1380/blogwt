//
//  CommentService.java
//  blogwt
//
//  Created by William Shakour on October 24, 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.comment;

import static com.willshex.blogwt.server.helper.PersistenceHelper.id;
import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Comment;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.Reaction;
import com.willshex.blogwt.shared.api.datatype.User;

final class CommentService implements ICommentService {

	private static final String PARENT_KEY = "parentKey";
	private static final String OWNER_KEY = "ownerKey";
	private static final String EMOTICON = "emoticon";
	private static final String APPROVED = "approved";

	public String getName () {
		return NAME;
	}

	private LoadType<Comment> load () {
		return provide().load().type(Comment.class);
	}

	private LoadType<Reaction> loadReactions () {
		return provide().load().type(Reaction.class);
	}

	@SuppressWarnings("unchecked")
	private static Key<DataType> parentKey (Object parent) {
		return (Key<DataType>) (Key<?>) Key.create(parent);
	}

	@Override
	public Comment getComment (Long id) {
		return id(load(), id);
	}

	@Override
	public Comment addComment (Comment comment) {
		if (comment.created == null) {
			comment.created = new Date();
		}

		comment.ownerKey = Key.create(comment.owner);
		comment.parentKey = parentKey(comment.parent);

		Key<Comment> key = provide().save().entity(comment).now();
		comment.id = keyToId(key);

		return comment;
	}

	@Override
	public void deleteComment (Comment comment) {
		List<Reaction> reactions = loadReactions()
				.filter(PARENT_KEY, Key.create(comment)).list();

		if (!reactions.isEmpty()) {
			provide().delete().entities(reactions).now();
		}

		provide().delete().entity(comment).now();
	}

	@Override
	public Comment updateComment (Comment comment) {
		provide().save().entity(comment).now();

		return comment;
	}

	@Override
	public List<Comment> getVisibleComments (DataType parent, User viewer,
			boolean moderator) {
		List<Comment> visible = new ArrayList<Comment>();

		for (Comment comment : load().filter(PARENT_KEY, Key.create(parent))
				.list()) {
			boolean own = viewer != null && comment.ownerKey != null
					&& keyToId(comment.ownerKey).equals(viewer.id);

			if (moderator || own || Boolean.TRUE.equals(comment.approved)) {
				visible.add(comment);
			}
		}

		// sorted here rather than in the query, so that no composite index
		// of parent and created is needed
		Collections.sort(visible, (a, b) -> a.created.compareTo(b.created));

		return visible;
	}

	@Override
	public List<Comment> getComments (DataType parent, User owner,
			Boolean pending) {
		Query<Comment> query = load();

		if (parent != null) {
			query = query.filter(PARENT_KEY, Key.create(parent));
		}

		if (owner != null) {
			query = query.filter(OWNER_KEY, Key.create(owner));
		}

		if (Boolean.TRUE.equals(pending)) {
			query = query.filter(APPROVED, Boolean.FALSE);
		}

		List<Comment> comments = new ArrayList<Comment>(query.list());

		Collections.sort(comments, (a, b) -> b.created.compareTo(a.created));

		return comments;
	}

	@Override
	public List<Comment> inflateAll (List<Comment> comments, Integer start,
			Integer count) {
		int from = Math.min(start.intValue(), comments.size());
		int to = (int) Math.min((long) from + count.intValue(),
				comments.size());

		List<Comment> page = new ArrayList<Comment>(comments.subList(from, to));

		for (Comment comment : page) {
			inflate(comment);
		}

		return page;
	}

	@Override
	public Comment inflateParent (Comment comment) {
		Object parent = provide().load().key(comment.parentKey).now();

		if (parent instanceof Post) {
			Post post = (Post) parent;
			comment.parent = new Post().title(post.title).slug(post.slug);
			comment.parent.id = post.id;
		} else if (parent instanceof Page) {
			Page page = (Page) parent;
			comment.parent = new Page().title(page.title).slug(page.slug);
			comment.parent.id = page.id;
		}

		return comment;
	}

	@Override
	public Comment inflate (Comment comment) {
		if (comment.ownerKey != null) {
			comment.owner = UserServiceProvider.provide()
					.getUser(keyToId(comment.ownerKey));
		}

		comment.reactions = getReactions(comment);

		return comment;
	}

	@Override
	public Reaction getReaction (Long id) {
		return id(loadReactions(), id);
	}

	@Override
	public List<Reaction> getReactions (DataType parent) {
		return getReactions(parentKey(parent));
	}

	@Override
	public List<Reaction> getReactions (Key<DataType> parentKey) {
		List<Reaction> reactions = loadReactions()
				.filter(PARENT_KEY, parentKey).list();

		Collections.sort(reactions, (a, b) -> a.created.compareTo(b.created));

		for (Reaction reaction : reactions) {
			if (reaction.ownerKey != null) {
				reaction.owner = UserServiceProvider.provide()
						.getUser(keyToId(reaction.ownerKey));
			}
		}

		return reactions;
	}

	@Override
	public Reaction getReaction (DataType parent, User owner,
			String emoticon) {
		return loadReactions().filter(PARENT_KEY, Key.create(parent))
				.filter(OWNER_KEY, Key.create(owner))
				.filter(EMOTICON, emoticon).first().now();
	}

	@Override
	public Reaction addReaction (Reaction reaction) {
		if (reaction.created == null) {
			reaction.created = new Date();
		}

		reaction.ownerKey = Key.create(reaction.owner);
		reaction.parentKey = parentKey(reaction.parent);

		Key<Reaction> key = provide().save().entity(reaction).now();
		reaction.id = keyToId(key);

		return reaction;
	}

	@Override
	public void deleteReaction (Reaction reaction) {
		provide().delete().entity(reaction).now();
	}

}
