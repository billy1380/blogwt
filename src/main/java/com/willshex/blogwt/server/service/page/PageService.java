//  
//  PageService.java
//  blogwt
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

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.server.helper.SearchHelper;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.PersistenceService;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.PageSortType;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.helper.PagerHelper;

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

		SearchHelper.queueToIndex(getName(), page.id);

		return page;
	}

	/**
	 * @param page
	 * @return
	 */
	private Document toDocument (Page page) {
		Document document = null;

		if (page != null) {
			Document.Builder documentBuilder = Document.newBuilder();

			documentBuilder.setId(getName() + page.id.toString())
					.addField(Field.newBuilder().setName("owner")
							.setAtom(page.owner.username))
					.addField(Field.newBuilder().setName("owner")
							.setText(UserHelper.name(page.owner)))

					.addField(Field.newBuilder().setName("created")
							.setDate(page.created))
					.addField(Field.newBuilder().setName("title")
							.setText(page.title));

			if (page.posts != null) {
				StringBuilder body = new StringBuilder();
				for (Post post : page.posts) {
					body.append(post.content.body).append("\n\n");
				}

				documentBuilder.addField(Field.newBuilder().setName("body")
						.setText(body.toString()));
			}

			document = documentBuilder.build();
		}

		return document;
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

		SearchHelper.queueToIndex(getName(), page.id);

		return page;
	}

	@Override
	public void deletePage (Page page) {
		ofy().delete().entity(page).now();

		SearchHelper.deleteSearch(getName() + page.id.toString());
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

		if (Boolean.TRUE.equals(includePostContents)) {
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
			Integer count, PageSortType sortBy,
			SortDirectionType sortDirection) {
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

		if (Boolean.TRUE.equals(includePostContents)) {
			populatePostContents(pages);
		}

		return pages;
	}

	private void populatePostContents (List<Page> pages) {
		List<Post> posts = new ArrayList<Post>();

		for (Page page : pages) {
			posts.addAll(PostServiceProvider.provide()
					.getIdPostBatch(PersistenceService.keysToIds(page.postKeys)));
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

		if (Boolean.TRUE.equals(includePostContents)) {
			populatePostContents(Arrays.asList(page));
		}

		return page;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.page.IPageService#indexAll() */
	@Override
	public void indexAll () {
		Pager pager = PagerHelper.createDefaultPager();

		List<Page> pages = null;
		do {
			pages = getPages(Boolean.FALSE, pager.start, pager.count, null,
					null);

			for (Page page : pages) {
				SearchHelper.queueToIndex(getName(), page.id);
			}

			PagerHelper.moveForward(pager);
		} while (pages != null && pages.size() >= pager.count.intValue());
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.page.IPageService#getPartialSlugPages
	 * (java.lang.String, java.lang.Boolean, java.lang.Integer,
	 * java.lang.Integer, com.willshex.blogwt.shared.api.datatype.PageSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Page> getPartialSlugPages (String partialSlug,
			Boolean includePostContents, Integer start, Integer count,
			PageSortType sortBy, SortDirectionType sortDirection) {
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

		if (partialSlug != null) {
			query = SearchHelper.addStartsWith("slug",
					partialSlug.toLowerCase(), query);
		}

		List<Page> pages = query.list();

		if (Boolean.TRUE.equals(includePostContents)) {
			populatePostContents(pages);
		}

		return pages;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.page.IPageService#indexPage(java.lang.
	 * Long) */
	@Override
	public void indexPage (Long id) {
		Page page = getPage(id);

		page.owner = UserServiceProvider.provide()
				.getUser(Long.valueOf(page.ownerKey.getId()));

		populatePostContents(Arrays.asList(page));

		SearchHelper.indexDocument(toDocument(page));
	}
}