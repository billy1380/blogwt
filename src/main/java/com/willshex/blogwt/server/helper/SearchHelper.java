//
//  SearchHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.api.taskqueue.TransientFailureException;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.User;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SearchHelper {

	public static final String ENTITY_NAME_KEY = "name";
	public static final String ENTITY_ID_KEY = "id";

	private final static String ALL_INDEX_NAME = "all";
	private static final int SEARCH_LIMIT = 3;
	private static final String INDEX_SEARCH_URL = "/searchindexer";
	private static SearchService searchService;

	public static void indexDocument (Document document) {
		if (document != null) {
			indexDocument(document, 0);
		}
	}

	private static void indexDocument (Document document, int count) {
		try {
			getIndex().put(document);
		} catch (PutException e) {
			if (StatusCode.TRANSIENT_ERROR
					.equals(e.getOperationResult().getCode())) {
				if (count < 2) {
					indexDocument(document, count++);
				}
			}
		}
	}

	private static SearchService ensureSearchService () {
		if (searchService == null) {
			searchService = SearchServiceFactory.getSearchService();
		}
		return searchService;
	}

	public static List<Post> searchPosts (String query) {
		Results<ScoredDocument> matches = getIndex().search(query);
		List<Post> posts = new ArrayList<Post>();
		String id;
		Post post;
		int limit = SEARCH_LIMIT;
		final String postServiceName = PostServiceProvider.provide().getName();
		for (ScoredDocument scoredDocument : matches) {
			if (limit == 0) {
				break;
			}

			if ((id = scoredDocument.getId()).startsWith(postServiceName)) {
				post = PostServiceProvider.provide()
						.getPost(Long.valueOf(id.replace(postServiceName, "")));
				if (post != null) {
					posts.add(post);
				}
			}

			limit--;
		}

		return posts;
	}

	public static List<Page> searchPages (String query) {
		Results<ScoredDocument> matches = getIndex().search(query);
		List<Page> pages = new ArrayList<Page>();
		String id;
		Page page;
		int limit = SEARCH_LIMIT;
		final String pageServiceName = PageServiceProvider.provide().getName();
		for (ScoredDocument scoredDocument : matches) {
			if (limit == 0) {
				break;
			}

			if ((id = scoredDocument.getId()).startsWith(pageServiceName)) {
				page = PageServiceProvider.provide()
						.getPage(Long.valueOf(id.replace(pageServiceName, "")));
				if (page != null) {
					pages.add(page);
				}
			}

			limit--;
		}

		return pages;
	}

	public static List<User> searchUsers (String query) {
		Results<ScoredDocument> matches = getIndex().search(query);
		List<User> users = new ArrayList<User>();
		String id;
		User user;
		int limit = SEARCH_LIMIT;
		final String userServiceName = UserServiceProvider.provide().getName();
		for (ScoredDocument scoredDocument : matches) {
			if (limit == 0) {
				break;
			}

			if ((id = scoredDocument.getId()).startsWith(userServiceName)) {
				user = UserServiceProvider.provide()
						.getUser(Long.valueOf(id.replace(userServiceName, "")));
				if (user != null) {
					users.add(user);
				}
			}

			limit--;
		}

		return users;
	}

	public static void deleteSearch (String documentId) {
		getIndex().delete(documentId);
	}

	private static Index getIndex () {
		return ensureSearchService().getIndex(
				IndexSpec.newBuilder().setName(ALL_INDEX_NAME).build());
	}

	public static <T> Query<T> addStartsWith (String field, String text,
			Query<T> query) {
		return query.filter(field + " >=", text).filter(field + " <",
				text + "\ufffd");
	}

	/**
	 * @param name
	 * @param id
	 */
	public static void queueToIndex (String name, Long id) {
		Queue queue = QueueFactory.getDefaultQueue();

		TaskOptions options = TaskOptions.Builder.withMethod(Method.POST)
				.url(INDEX_SEARCH_URL).param(ENTITY_NAME_KEY, name)
				.param(ENTITY_ID_KEY, id.toString());

		try {
			queue.add(options);
		} catch (TransientFailureException ex) {
			// retry once
			try {
				queue.add(options);
			} catch (TransientFailureException reEx) {
				// failed :( what can you do
			}
		}
	}
}
