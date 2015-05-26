//  
//  PostService.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//  
//
package com.willshex.blogwt.server.service.post;

import static com.willshex.blogwt.server.service.PersistenceService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostContent;
import com.willshex.blogwt.shared.api.datatype.PostSortType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.helper.PostHelper;

/**
 * 
 * @author William Shakour (billy1380)
 *
 */
final class PostService implements IPostService {

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.service.IService#getName() */
	@Override
	public String getName () {
		return NAME;
	}

	@Override
	public Post getPost (Long id) {
		return ofy().load().type(Post.class).id(id.longValue()).now();
	}

	@Override
	public Post addPost (Post post) {
		if (post.created == null) {
			post.created = new Date();
		}

		post.slug = PostHelper.slugify(post.title);
		post.authorKey = Key.create(post.author);

		post.contentKey = ofy().save().entity(post.content).now();

		Key<Post> postKey = ofy().save().entity(post).now();
		post.id = Long.valueOf(postKey.getId());

		return post;
	}

	@Override
	public Post updatePost (Post post) {
		post.slug = PostHelper.slugify(post.title);

		if (post.content != null) {
			ofy().save().entity(post.content).now();
		}

		ofy().save().entity(post).now();
		return post;
	}

	@Override
	public void deletePost (Post post) {
		ofy().delete().entity(post).now();
	}

	@Override
	public List<Post> getUserViewablePosts (User user, Boolean showAll,
			Boolean includeContents, Integer start, Integer count,
			PostSortType sortBy, SortDirectionType sortDirection) {

		Query<Post> query = ofy().load().type(Post.class);

		if (user != null && user.id != null) {
			query = query.filter(PostSortType.PostSortTypeAuthor.toString()
					+ "Key", user);
		}

		if (showAll == null || !showAll.booleanValue()) {
			query = query.filter(PostSortType.PostSortTypeVisible.toString(),
					Boolean.TRUE);
		}

		if (start != null) {
			query = query.offset(start.intValue());
		}

		if (count != null) {
			query = query.limit(count.intValue());
		}

		if (sortBy != null) {
			String condition = sortBy.toString();

			if (sortDirection != null) {
				switch (sortDirection) {
				case SortDirectionTypeDescending:
					condition = "-" + condition;
					break;
				default:
					break;
				}
			}

			query = query.order(condition);
		}

		List<Post> posts = query.list();

		if (includeContents) {
			List<Long> postContentIds = new ArrayList<Long>();

			for (Post post : posts) {
				postContentIds.add(Long.valueOf(post.contentKey.getId()));
			}

			Map<Long, PostContent> contents = ofy().load()
					.type(PostContent.class).ids(postContentIds);

			for (Post post : posts) {
				post.content = contents.get(post.id);
			}
		}

		return posts;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.post.IPostService#getPosts(java.lang
	 * .Boolean, java.lang.Boolean, java.lang.Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.PostSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Post> getPosts (Boolean showAll, Boolean includeContents,
			Integer start, Integer count, PostSortType sortBy,
			SortDirectionType sortDirection) {
		return getUserViewablePosts(null, showAll, includeContents, start,
				count, sortBy, sortDirection);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.post.IPostService#getSlugPost(java
	 * .lang.String) */
	@Override
	public Post getSlugPost (String slug) {
		return ofy().load().type(Post.class)
				.filter(PostSortType.PostSortTypeSlug.toString(), slug).first()
				.now();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.post.IPostService#
	 * getUserViewablePostsCount(com.willshex.blogwt.shared.api.datatype.User,
	 * java.lang.Boolean) */
	@Override
	public Long getUserViewablePostsCount (User user, Boolean showAll) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.post.IPostService#getPostsCount(java
	 * .lang.Boolean) */
	@Override
	public Long getPostsCount (Boolean showAll) {
		return getUserViewablePostsCount(null, showAll);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.post.IPostService#getPostContent(
	 * com.willshex.blogwt.shared.api.datatype.Post) */
	@Override
	public PostContent getPostContent (Post post) {
		return ofy().load().type(PostContent.class).id(post.contentKey.getId())
				.now();
	}

}