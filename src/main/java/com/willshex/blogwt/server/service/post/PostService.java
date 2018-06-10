//  
//  PostService.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//  
//
package com.willshex.blogwt.server.service.post;

import static com.willshex.blogwt.server.helper.PersistenceHelper.id;
import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.helper.SearchHelper;
import com.willshex.blogwt.server.service.archiveentry.ArchiveEntryServiceProvider;
import com.willshex.blogwt.server.service.search.ISearch;
import com.willshex.blogwt.server.service.tag.TagServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostContent;
import com.willshex.blogwt.shared.api.datatype.PostSortType;
import com.willshex.blogwt.shared.api.datatype.Tag;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PostHelper;
import com.willshex.blogwt.shared.helper.UserHelper;

/**
 * 
 * @author William Shakour (billy1380)
 *
 */
final class PostService implements IPostService, ISearch<Post> {

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.service.IService#getName() */
	@Override
	public String getName () {
		return NAME;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.post.IPostService#getPost(java.lang
	 * .Long) */
	@Override
	public Post getPost (Long id) {
		return id(load(), id);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.post.IPostService#addPost(com.willshex
	 * .blogwt.shared.api.datatype.Post) */
	@Override
	public Post addPost (Post post) {
		if (post.created == null) {
			post.created = new Date();
		}

		post.slug = nextPostSlug(PostHelper.slugify(post.title));

		post.authorKey = Key.create(post.author);

		if (post.content.created == null) {
			post.content.created = post.created;
		}

		post.contentKey = provide().save().entity(post.content).now();

		Post previousPost = null;
		// just been published
		if (post.published != null && post.previousSlug == null) {
			previousPost = getLastPublishedPost();

			if (previousPost != null) {
				post.previousSlug = previousPost.slug;
				previousPost.nextSlug = post.slug;
			}
		}

		Key<Post> key = provide().save().entity(post).now();
		post.id = keyToId(key);

		post.content.postKey = key;
		provide().save().entity(post.content).now();

		updateTags(post);

		ArchiveEntryServiceProvider.provide().archivePost(post);

		SearchHelper.queueToIndex(getName(), post.id);

		if (previousPost != null) {
			updatePost(previousPost, null);
		}

		return post;
	}

	/**
	 * @param slug
	 * @return
	 */
	private String nextPostSlug (String slug) {
		List<Post> posts = getPartialSlugPosts(slug, Boolean.TRUE,
				Boolean.FALSE, 0, Integer.valueOf(Integer.MAX_VALUE), null,
				null);
		return PostHelper.nextPostSlug(posts, slug);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.search.IIndex#toDocument(java.lang.
	 * Object) */
	@Override
	public Document toDocument (Post post) {
		Document document = null;

		if (Boolean.TRUE.equals(post.listed) && post.published != null) {

			if (post.author == null) {
				post.author = UserServiceProvider.provide()
						.getUser(keyToId(post.authorKey));
			}

			Document.Builder documentBuilder = Document.newBuilder();
			documentBuilder.setId(getName() + post.id.toString())
					.addField(Field.newBuilder().setName("author")
							.setAtom(post.author.username))
					.addField(Field.newBuilder().setName("author")
							.setText(UserHelper.name(post.author)))
					.addField(Field.newBuilder().setName("created")
							.setDate(post.created))
					.addField(Field.newBuilder().setName("published")
							.setDate(post.published))
					.addField(Field.newBuilder().setName("slug")
							.setAtom(post.slug))
					.addField(Field.newBuilder().setName("summary")
							.setText(post.summary))
					.addField(Field.newBuilder().setName("title")
							.setText(post.title));

			if (post.content != null) {
				documentBuilder.addField(Field.newBuilder().setName("body")
						.setText(post.content.body));
			}

			if (post.tags != null) {
				for (String tag : post.tags) {
					documentBuilder.addField(
							Field.newBuilder().setName("tag").setText(tag));
				}
			}

			document = documentBuilder.build();
		}

		return document;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.post.IPostService#updatePost(com.
	 * willshex.blogwt.shared.api.datatype.Post, java.lang.Iterable) */
	@Override
	public Post updatePost (Post post, Iterable<String> removedTags) {
		post.slug = PostHelper.slugify(post.title);

		if (post.content != null) {
			if (post.content.created == null) {
				post.content.created = post.created;
			}

			if (post.content.postKey == null) {
				post.content.postKey = Key.create(post);
			}

			provide().save().entity(post.content).now();
		}

		String previousSlug = null, nextSlug = null;
		Post previousPost = null, nextPost = null;

		if (Boolean.TRUE.equals(post.listed) && post.published != null) {
			if (post.previousSlug == null && post.nextSlug == null) {
				previousPost = getPreviousPost(post);

				nextPost = getNextPost(post);

				if (previousPost != null) {
					post.previousSlug = previousPost.slug;
					previousPost.nextSlug = post.slug;
				}

				if (nextPost != null) {
					post.nextSlug = nextPost.slug;
					nextPost.previousSlug = post.slug;
				}
			}
		} else {
			previousSlug = post.previousSlug;
			nextSlug = post.nextSlug;

			post.previousSlug = null;
			post.nextSlug = null;
		}

		provide().save().entity(post).now();

		updateTags(post);

		if (removedTags != null) {
			deleteFromTags(post, removedTags);
		}

		if (Boolean.TRUE.equals(post.listed)) {
			ArchiveEntryServiceProvider.provide().archivePost(post);
		} else {
			deleteFromArchive(post);

			if (previousSlug != null && nextSlug != null) {
				previousPost = getSlugPost(previousSlug);
				nextPost = getSlugPost(nextSlug);

				previousPost.nextSlug = nextPost.slug;
				nextPost.previousSlug = previousPost.slug;

				updatePost(previousPost, null);
				updatePost(nextPost, null);
			} else if (previousSlug != null) {
				previousPost = getSlugPost(previousSlug);
				previousPost.nextSlug = null;
				updatePost(previousPost, null);
			} else if (nextSlug != null) {
				nextPost = getSlugPost(nextSlug);
				nextPost.previousSlug = null;
				updatePost(nextPost, null);
			}
		}

		SearchHelper.queueToIndex(getName(), post.id);

		if (previousPost != null) {
			updatePost(previousPost, null);
		}

		return post;
	}

	/**
	 * @param post
	 * @return
	 */
	private Post getNextPost (Post post) {
		return PersistenceHelper
				.one(load().order("published").filter("listed =", true)
						.filter("published >", post.published));
	}

	/**
	 * @param post
	 * @return
	 */
	private Post getPreviousPost (Post post) {
		return PersistenceHelper
				.one(load().order("-published").filter("listed =", true)
						.filter("published <", post.published));
	}

	private LoadType<Post> load () {
		return provide().load().type(Post.class);
	}

	private LoadType<PostContent> loadContent () {
		return provide().load().type(PostContent.class);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.post.IPostService#deletePost(com.
	 * willshex.blogwt.shared.api.datatype.Post) */
	@Override
	public void deletePost (Post post) {
		deleteFromTags(post, post.tags);

		deleteFromArchive(post);

		String previousSlug = null, nextSlug = null;
		Post previousPost = null, nextPost = null;

		if (post.published != null && Boolean.TRUE.equals(post.listed)) {
			previousSlug = post.previousSlug;
			nextSlug = post.nextSlug;
		}

		provide().delete().entity(post).now();

		provide().delete().key(post.contentKey).now();

		SearchHelper.deleteSearch(getName() + post.id.toString());

		if (previousSlug != null && nextSlug != null) {
			previousPost = getSlugPost(previousSlug);
			nextPost = getSlugPost(nextSlug);

			previousPost.nextSlug = nextPost.slug;
			nextPost.previousSlug = previousPost.slug;

			updatePost(previousPost, null);
			updatePost(nextPost, null);
		} else if (previousSlug != null) {
			previousPost = getSlugPost(previousSlug);
			previousPost.nextSlug = null;
			updatePost(previousPost, null);
		} else if (nextSlug != null) {
			nextPost = getSlugPost(nextSlug);
			nextPost.previousSlug = null;
			updatePost(nextPost, null);
		}
	}

	/**
	 * @param post
	 */
	private void deleteFromArchive (Post post) {
		if (post.published != null) {
			ArchiveEntry archiveEntry = ArchiveEntryServiceProvider.provide()
					.getDateArchiveEntry(post.published);

			if (archiveEntry != null) {
				ArchiveEntryServiceProvider.provide()
						.deleteArchiveEntryPost(archiveEntry, post);
			}
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.post.IPostService#getUserViewablePosts
	 * (com.willshex.blogwt.shared.api.datatype.User, java.lang.Boolean,
	 * java.lang.Boolean, java.lang.Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.PostSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Post> getUserViewablePosts (User user, Boolean showAll,
			Boolean includeContents, Integer start, Integer count,
			PostSortType sortBy, SortDirectionType sortDirection) {

		Query<Post> query = load();

		if (user != null && user.id != null) {
			query = query.filter(
					PostSortType.PostSortTypeAuthor.toString() + "Key", user);
		}

		if (showAll == null || !showAll.booleanValue()) {
			query = query.filter(PostSortType.PostSortTypeListed.toString(),
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

		if (Boolean.TRUE.equals(includeContents)) {
			List<Long> postContentIds = new ArrayList<Long>();

			for (Post post : posts) {
				postContentIds.add(keyToId(post.contentKey));
			}

			Map<Long, PostContent> contents = loadContent().ids(postContentIds);

			for (Post post : posts) {
				post.content = contents.get(keyToId(post.contentKey));
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
		return PersistenceHelper.one(
				load().filter(PostSortType.PostSortTypeSlug.toString(), slug));
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
	 * @see com.willshex.blogwt.server.service.post.IPostService#getPostContent(
	 * com.willshex.blogwt.shared.api.datatype.Post) */
	@Override
	public PostContent getPostContent (Post post) {
		return loadContent().id(post.contentKey.getId()).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.post.IPostService#getIdPostBatch(java.
	 * lang.Iterable) */
	@Override
	public List<Post> getIdPostBatch (Iterable<Long> ids) {
		return new ArrayList<Post>(load().ids(ids).values());
	}

	/**
	 * @param post
	 */
	private void updateTags (Post post) {
		if (post.published != null && post.tags != null) {
			Tag tag;
			for (String name : post.tags) {
				tag = TagServiceProvider.provide()
						.getSlugTag(PostHelper.slugify(name));

				if (tag == null) {
					tag = TagServiceProvider.provide().addTag(
							new Tag().name(name).posts(Arrays.asList(post)));
				}

				if (Boolean.TRUE.equals(post.listed)) {
					TagServiceProvider.provide().addTagPost(tag, post);
				} else {
					TagServiceProvider.provide().removeTagPost(tag, post);
				}
			}
		}
	}

	/**
	 * @param post
	 * @param tags 
	 */
	private void deleteFromTags (Post post, Iterable<String> tags) {
		if (tags != null) {
			Tag tag;
			for (String name : tags) {
				tag = TagServiceProvider.provide()
						.getSlugTag(PostHelper.slugify(name));

				if (tag != null) {
					TagServiceProvider.provide().removeTagPost(tag, post);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.search.IIndex#indexAll() */
	@Override
	public void indexAll () {
		Pager pager = PagerHelper.createDefaultPager();

		List<Post> posts = null;
		do {
			posts = getPosts(Boolean.FALSE, Boolean.FALSE, pager.start,
					pager.count, null, null);

			for (Post post : posts) {
				SearchHelper.queueToIndex(getName(), post.id);
			}

			PagerHelper.moveForward(pager);
		} while (posts != null && posts.size() >= pager.count.intValue());
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.post.IPostService#getPartialSlugPosts
	 * (java.lang.String, java.lang.Boolean, java.lang.Boolean,
	 * java.lang.Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.PostSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Post> getPartialSlugPosts (String partialSlug, Boolean showAll,
			Boolean includeContents, Integer start, Integer count,
			PostSortType sortBy, SortDirectionType sortDirection) {
		return getUserViewablePartialSlugPosts(partialSlug, null, showAll,
				includeContents, start, count, sortBy, sortDirection);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.post.IPostService#
	 * getUserViewablePartialSlugPosts(java.lang.String,
	 * com.willshex.blogwt.shared.api.datatype.User, java.lang.Boolean,
	 * java.lang.Boolean, java.lang.Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.PostSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Post> getUserViewablePartialSlugPosts (String partialSlug,
			User user, Boolean showAll, Boolean includeContents, Integer start,
			Integer count, PostSortType sortBy,
			SortDirectionType sortDirection) {
		Query<Post> query = load();

		if (user != null && user.id != null) {
			query = query.filter(
					PostSortType.PostSortTypeAuthor.toString() + "Key", user);
		}

		if (showAll == null || !showAll.booleanValue()) {
			query = query.filter(PostSortType.PostSortTypeListed.toString(),
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

		if (partialSlug != null) {
			query = SearchHelper.addStartsWith("slug",
					partialSlug.toLowerCase(), query);
		}

		List<Post> posts = query.list();

		if (Boolean.TRUE.equals(includeContents)) {
			List<Long> postContentIds = new ArrayList<Long>();

			for (Post post : posts) {
				postContentIds.add(keyToId(post.contentKey));
			}

			Map<Long, PostContent> contents = loadContent().ids(postContentIds);

			for (Post post : posts) {
				post.content = contents.get(keyToId(post.contentKey));
			}
		}

		return posts;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.post.IPostService#getLastPublishedPost
	 * () */
	@Override
	public Post getLastPublishedPost () {
		Post last = null;

		List<Post> posts = getPosts(Boolean.FALSE, Boolean.FALSE,
				Integer.valueOf(0), Integer.valueOf(1),
				PostSortType.PostSortTypePublished,
				SortDirectionType.SortDirectionTypeDescending);

		if (posts != null && posts.size() != 0) {
			last = posts.get(0);
		}

		return last;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.post.IPostService#linkAll() */
	@Override
	public void linkAll () {
		Pager pager = PagerHelper.createDefaultPager();

		List<Post> posts = null;
		Post last = null;
		do {
			posts = getPosts(Boolean.FALSE, Boolean.FALSE, pager.start,
					pager.count, PostSortType.PostSortTypePublished,
					SortDirectionType.SortDirectionTypeDescending);

			if (posts != null && posts.size() > 0) {
				for (int i = 0, next = -1, previous = 1; i < posts
						.size(); i++, previous++, next++) {
					if (i == 0 && last != null) {
						posts.get(i).nextSlug = last.slug;
						last.previousSlug = posts.get(i).slug;

						updatePost(last, null);

						last = null;
					}

					if (next >= 0) {
						posts.get(i).nextSlug = posts.get(next).slug;
					}

					if (previous < posts.size()) {
						posts.get(i).previousSlug = posts.get(previous).slug;
					} else if (previous == posts.size()) {
						last = posts.get(i);
					}

					// last will get updated twice for all but last page 
					updatePost(posts.get(i), null);
				}
			}

			PagerHelper.moveForward(pager);
		} while (posts != null && posts.size() >= pager.count.intValue());
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.post.IPostService#clearLinks() */
	@Override
	public void clearLinks () {
		Pager pager = PagerHelper.createDefaultPager();

		List<Post> posts = null;
		do {
			posts = getPosts(Boolean.FALSE, Boolean.FALSE, pager.start,
					pager.count, PostSortType.PostSortTypePublished,
					SortDirectionType.SortDirectionTypeDescending);

			if (posts != null) {
				for (Post post : posts) {
					post.nextSlug = null;
					post.previousSlug = null;
				}

				provide().save().entities(posts).now();
			}

			PagerHelper.moveForward(pager);
		} while (posts != null && posts.size() >= pager.count.intValue());
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.search.IIndex#index(java.lang.Long) */
	@Override
	public void index (Long id) {
		Post post = getPost(id);

		if (post.authorKey != null) {
			post.author = UserServiceProvider.provide()
					.getUser(keyToId(post.authorKey));
		}

		if (post.contentKey != null) {
			post.content = loadContent().id(keyToId(post.contentKey)).now();
		}

		SearchHelper.indexDocument(toDocument(post));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.search.ISearch#search(java.lang.
	 * String, java.lang.Integer, java.lang.Integer, java.lang.String,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Post> search (String query, Integer start, Integer count,
			String sortBy, SortDirectionType direction) {
		Results<ScoredDocument> matches = SearchHelper.getIndex().search(query);
		List<Post> posts = new ArrayList<Post>();
		String id;
		Post post;
		int limit = SearchHelper.SHORT_SEARCH_LIMIT;
		final String postServiceName = getName();
		for (ScoredDocument scoredDocument : matches) {
			if (limit == 0) {
				break;
			}

			if ((id = scoredDocument.getId()).startsWith(postServiceName)) {
				post = getPost(Long.valueOf(id.replace(postServiceName, "")));
				if (post != null) {
					posts.add(post);
				}
			}

			limit--;
		}

		return posts;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.search.ISearch#search(java.util.
	 * Collection, java.lang.String, java.lang.String, java.lang.Integer,
	 * java.lang.String, com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public String search (Collection<Post> resultHolder, String query,
			String next, Integer count, String sortBy,
			SortDirectionType direction) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.persistence.batch.Batcher.BatchGetter#
	 * get(java.lang.Iterable) */
	@Override
	public List<Post> get (Iterable<Long> ids) {
		return getIdPostBatch(ids);
	}

}
