//  
//  PageService.java
//  xsdwsdl2code
//
//  Created by William Shakour on June 22, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.page;

import static com.willshex.blogwt.server.service.PersistenceService.ofy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.server.service.PersistenceService;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.PageSortType;
import com.willshex.blogwt.shared.api.datatype.Post;

final class PageService implements IPageService {
	public String getName () {
		return NAME;
	}

	@Override
	public Page getPage (Long id) {
		return ofy().load().type(Page.class).id(id).now();
	}

	@Override
	public Page addPage (Page page) {
		if (page.created == null) {
			page.created = new Date();
		}

		page.ownerKey = Key.create(page.owner);

		page.postKeys = new ArrayList<Key<Post>>();
		for (Post post : page.posts) {
			page.postKeys.add(Key.create(post));
		}

		if (page.parent != null) {
			page.parentKey = Key.create(page.parent);
		}

		Key<Page> pageKey = ofy().save().entity(page).now();
		page.id = Long.valueOf(pageKey.getId());

		return page;
	}

	@Override
	public Page updatePage (Page page) {
		page.postKeys = new ArrayList<Key<Post>>();
		for (Post post : page.posts) {
			page.postKeys.add(Key.create(post));
		}

		if (page.parent != null) {
			page.parentKey = Key.create(page.parent);
		} else {
			page.parentKey = null;
		}

		ofy().save().entity(page).now();
		return page;
	}

	@Override
	public void deletePage (Page page) {
		ofy().delete().entity(page).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.page.IPageService#getSlugPage(java
	 * .lang.String, java.lang.Boolean) */
	@Override
	public Page getSlugPage (String slug, Boolean includePostContents) {
		Page page = ofy().load().type(Page.class)
				.filter(PageSortType.PageSortTypeSlug.toString(), slug).first()
				.now();

		if (includePostContents == Boolean.TRUE) {
			populatePostContents(Arrays.asList(page));
		}

		return page;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.page.IPageService#getPages(java.lang
	 * .Boolean, java.lang.Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.PageSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Page> getPages (Boolean includePostContents, Integer start,
			Integer count, PageSortType sortBy, SortDirectionType sortDirection) {
		Query<Page> query = ofy().load().type(Page.class);

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

		List<Page> pages = query.list();

		if (includePostContents == Boolean.TRUE) {
			populatePostContents(pages);
		}

		return pages;
	}

	private void populatePostContents (List<Page> pages) {
		List<Post> posts = new ArrayList<Post>();

		for (Page page : pages) {
			posts.addAll(PostServiceProvider.provide().getPostBatch(
					PersistenceService.keysToIds(page.postKeys)));
		}

		for (Post post : posts) {
			post.content = PostServiceProvider.provide().getPostContent(post);
		}

		Map<Long, Post> postLookup = new HashMap<Long, Post>();
		for (Post post : posts) {
			postLookup.put(post.id, post);
		}

		List<Post> pagePosts;
		for (Page page : pages) {
			pagePosts = new ArrayList<Post>();

			for (Key<Post> key : page.postKeys) {
				pagePosts.add(postLookup.get(Long.valueOf(key.getId())));
			}

			page.posts = pagePosts;
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.page.IPageService#getPage(java.lang
	 * .Long, java.lang.Boolean) */
	@Override
	public Page getPage (Long id, Boolean includePostContents) {
		Page page = getPage(id);

		if (includePostContents == Boolean.TRUE) {
			populatePostContents(Arrays.asList(page));
		}

		return page;
	}
}