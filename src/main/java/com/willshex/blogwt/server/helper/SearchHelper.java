//
//  SearchHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.api.taskqueue.TransientFailureException;
import com.googlecode.objectify.cmd.Query;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SearchHelper {

	public static final String ENTITY_NAME_KEY = "name";
	public static final String ENTITY_ID_KEY = "id";

	private final static String ALL_INDEX_NAME = "all";
	public static final int SHORT_SEARCH_LIMIT = 3;
	private static final String INDEX_SEARCH_URL = "/searchindexer";

	private static final int RETRY_COUNT = 3;

	private static SearchService searchService;

	public static void indexDocument (Document document) {
		indexDocument(ALL_INDEX_NAME, document);
	}

	public static void indexDocument (String name, Document document) {
		if (document != null) {
			indexDocument(name, document, RETRY_COUNT);
		}
	}

	private static void indexDocument (String name, Document document,
			int retry) {
		do {
			try {
				getIndex(name).put(document);
				retry = 0;
			} catch (PutException e) {
				if (StatusCode.TRANSIENT_ERROR
						.equals(e.getOperationResult().getCode())) {
					retry--;
				} else {
					// if it is not a transient error we just stop
					retry = 0;
				}
			}
		} while (retry > 0);
	}

	private static SearchService ensureSearchService () {
		if (searchService == null) {
			searchService = SearchServiceFactory.getSearchService();
		}

		return searchService;
	}

	public static void deleteSearch (String documentId) {
		deleteSearch(ALL_INDEX_NAME);
	}

	public static void deleteSearch (String name, String documentId) {
		getIndex(name).delete(documentId);
	}

	public static Index getIndex () {
		return getIndex(ALL_INDEX_NAME);
	}

	public static Index getIndex (String name) {
		return ensureSearchService()
				.getIndex(IndexSpec.newBuilder().setName(name).build());
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

		int retry = RETRY_COUNT;
		do {
			try {
				queue.add(options);

				// success no need to retry
				retry = 0;
			} catch (TransientFailureException ex) {
				retry--;
			}
		} while (retry > 0);
	}
}
