//
//  SearchHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.search.Cursor;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Document.Builder;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortOptions;
import com.google.appengine.api.search.StatusCode;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.api.taskqueue.TransientFailureException;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.server.service.search.ISearch;
import com.willshex.blogwt.server.service.search.ITenancy;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.utility.StringUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SearchHelper {

	private static final Logger LOG = Logger
			.getLogger(SearchHelper.class.getName());

	public static final String ENTITY_NAME_KEY = "name";
	public static final String ENTITY_ID_KEY = "id";

	private final static String ALL_INDEX_NAME = "all";
	public static final Integer SHORT_SEARCH_LIMIT = Integer.valueOf(3);
	private static final String INDEX_SEARCH_URL = "/searchindexer";

	private static final int RETRY_COUNT = 3;

	public static final String EMPTY_CURSOR = "-";
	public static final int SORT_LIMIT = 10000;

	private static SearchService searchService;
	private static final SimpleDateFormat QUERY_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static interface GetAll<T, E extends Enum<E>> {
		List<T> get (Integer start, Integer count, E sortBy,
				SortDirectionType sortDirection);
	}

	public static void indexDocument (ITenancy tenancy, Document document) {
		indexDocument(tenancy, ALL_INDEX_NAME, document);
	}

	public static <T> void indexDocument (ITenancy tenancy, String name,
			Document document) {
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Indexing document with document [" + document + "] and ["
					+ name + "]");
		}

		if (document != null) {
			indexDocument(tenancy, name, document, RETRY_COUNT);
		}
	}

	public static <T extends DataType, E extends Enum<E>> List<T> indexAll (
			String name, GetAll<T, E> pagedGetter) {
		Pager pager = PagerHelper.createDefaultPager();

		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Indexing all [" + name + "]");
		}

		List<T> lines = null;
		do {
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine("Getting pager start [" + pager.start + "] count ["
						+ pager.count + "]");
			}

			lines = pagedGetter.get(pager.start, pager.count, null, null);

			for (T line : lines) {
				SearchHelper.queueToIndex(name, line.id);
			}

			PagerHelper.moveForward(pager);
		} while (lines != null && lines.size() >= pager.count.intValue());

		return lines;
	}

	private static void indexDocument (ITenancy tenancy, String name,
			Document document, int retry) {
		do {
			try {
				getIndex(tenancy, name).put(document);
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
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine("Search service is null, creating now.");
			}

			searchService = SearchServiceFactory.getSearchService();
		}

		return searchService;
	}

	public static void deleteSearch (ITenancy tenancy, String documentId) {
		deleteSearch(tenancy, ALL_INDEX_NAME, documentId);
	}

	public static void deleteSearch (ITenancy tenancy, String name,
			String documentId) {
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Deleting name [" + name + "] document id [" + documentId
					+ "]");
		}

		getIndex(tenancy, name).delete(documentId);
	}

	public static Index getIndex (ITenancy tenancy) {
		return getIndex(tenancy, ALL_INDEX_NAME);
	}

	public static Index getIndex (ITenancy tenancy, String name) {
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Getting index [" + name + "]");
		}

		EnvironmentHelper.selectNamespace(tenancy.isShared());
		return ensureSearchService()
				.getIndex(IndexSpec.newBuilder().setName(name).build());
	}

	public static <T> Query<T> addStartsWith (String field, String text,
			Query<T> query) {
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Searching for [" + text + "] in field [" + field
					+ "] with query [" + query + "]");
		}

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
				if (LOG.isLoggable(Level.FINE)) {
					LOG.fine("Enqueueing name [" + name + "] and id [" + id
							+ "] for indexing on [" + queue.getQueueName()
							+ "]");
				}

				queue.add(options);

				// success no need to retry
				retry = 0;
			} catch (TransientFailureException ex) {
				retry--;

				if (LOG.isLoggable(Level.FINE)) {
					LOG.log(Level.FINE,
							"Failed, retrying [" + retry + "] retrys remaining",
							ex);
				}
			}
		} while (retry > 0);
	}

	/**
	 * @param query
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param direction
	 * @return 
	 */
	public static List<Long> search (ITenancy tenancy, String query,
			String indexName, Integer start, Integer count, String sortBy,
			SortDirectionType direction) {
		QueryOptions.Builder queryOptionsBuilder = QueryOptions.newBuilder()
				.setOffset(start == null ? PagerHelper.DEFAULT_START.intValue()
						: start.intValue())
				.setLimit(count == null ? PagerHelper.DEFAULT_COUNT.intValue()
						: count.intValue())
				.setReturningIdsOnly(true);

		if (sortBy != null) {
			queryOptionsBuilder.setSortOptions(SortOptions.newBuilder()
					.addSortExpression(SortExpression.newBuilder()
							.setExpression(sortBy)
							.setDirection(direction == null
									|| direction == SortDirectionType.SortDirectionTypeDescending
											? SortExpression.SortDirection.DESCENDING
											: SortExpression.SortDirection.ASCENDING))
					.setLimit(SearchHelper.SORT_LIMIT).build());
		}

		QueryOptions options = queryOptionsBuilder.build();

		com.google.appengine.api.search.Query apiQuery = com.google.appengine.api.search.Query
				.newBuilder().setOptions(options).build(query);

		Results<ScoredDocument> matches = getIndex(tenancy, indexName)
				.search(apiQuery);

		String id;
		List<Long> ids = new ArrayList<Long>();
		for (ScoredDocument scoredDocument : matches) {
			if ((id = scoredDocument.getId()) != null) {
				ids.add(Long.valueOf(id));
			}
		}

		return ids;
	}

	public static String search (ITenancy tenancy, Collection<Long> resultsIds,
			String query, String indexName, String next, Integer count,
			String sortBy, SortDirectionType direction) {
		Cursor cursor = null;

		if (!SearchHelper.EMPTY_CURSOR.equals(next)) {
			// build options and query
			QueryOptions.Builder queryOptionsBuilder = QueryOptions.newBuilder()
					.setCursor(next == null ? Cursor.newBuilder().build()
							: Cursor.newBuilder().build(next))
					.setLimit(
							count == null ? PagerHelper.DEFAULT_COUNT.intValue()
									: count.intValue())
					.setReturningIdsOnly(true);

			if (sortBy != null) {
				queryOptionsBuilder = queryOptionsBuilder
						.setSortOptions(SortOptions.newBuilder()
								.addSortExpression(SortExpression.newBuilder()
										.setExpression(sortBy)
										.setDirection(direction == null
												|| direction == SortDirectionType.SortDirectionTypeDescending
														? SortExpression.SortDirection.DESCENDING
														: SortExpression.SortDirection.ASCENDING))
								.setLimit(SearchHelper.SORT_LIMIT).build());
			}

			QueryOptions options = queryOptionsBuilder.build();

			com.google.appengine.api.search.Query apiQuery = com.google.appengine.api.search.Query
					.newBuilder().setOptions(options).build(query);

			Results<ScoredDocument> matches = SearchHelper
					.getIndex(tenancy, indexName).search(apiQuery);

			String id;
			if (resultsIds == null) {
				resultsIds = new ArrayList<>();
			} else {
				resultsIds.clear();
			}

			for (ScoredDocument scoredDocument : matches) {
				if ((id = scoredDocument.getId()) != null) {
					resultsIds.add(Long.valueOf(id));
				}
			}

			cursor = matches.getCursor();
		}

		return cursor == null ? SearchHelper.EMPTY_CURSOR
				: cursor.toWebSafeString();
	}

	public static <T> T one (String query, ISearch<T> search) {
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Searching for one with query [" + query + "]");
		}

		List<T> result = pagedAndSorted(search, query, Integer.valueOf(0),
				Integer.valueOf(1), null, null);

		return result == null || result.isEmpty() ? null : result.get(0);
	}

	public static String queryDate (Date date) {
		return QUERY_DATE_FORMAT.format(date);
	}

	public static void addSearchTokens (Builder documentBuilder,
			List<String> searchable) {
		addSearchTokens("search", documentBuilder, searchable);
	}

	public static void addSearchTokens (String fieldName,
			Builder documentBuilder, List<String> searchable) {
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Adding search tokens to field [" + fieldName + "]");

			if (LOG.isLoggable(Level.FINEST)) {
				LOG.finest("Tokens [" + searchable + "]");
			}
		}

		Collection<String> tokens = StringUtils.matchParts(searchable);

		if (tokens != null && !tokens.isEmpty()) {
			for (String token : tokens) {
				documentBuilder.addField(
						Field.newBuilder().setName(fieldName).setText(token));
			}
		}
	}

	public static <T> List<T> pagedAndSorted (ISearch<T> search, String query,
			Integer start, Integer count, String sortBy,
			SortDirectionType sortDirection) {
		EnvironmentHelper.selectNamespace(search.isShared());
		return search.search(query, start, count, sortBy, sortDirection);
	}

	public static <T> String pagedAndSorted (ISearch<T> search, List<T> results,
			String query, String next, Integer count, String sortBy,
			SortDirectionType sortDirection) {
		EnvironmentHelper.selectNamespace(search.isShared());
		return search.search(results, query, next, count, sortBy,
				sortDirection);
	}

	public static <T> void indexAll (ISearch<T> search) {
		EnvironmentHelper.selectNamespace(search.isShared());
		search.indexAll();
	}

	public static <T> void index (ISearch<T> search, Long id) {
		EnvironmentHelper.selectNamespace(search.isShared());
		search.index(id);
	}
}
