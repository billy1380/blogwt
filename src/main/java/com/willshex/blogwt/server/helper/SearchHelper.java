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
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Post;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SearchHelper {

	private final static String ALL_INDEX_NAME = "all";
	private static final int SEARCH_LIMIT = 3;
	private static SearchService searchService;

	public static void indexDocument (Document document) {
		if (document != null) {
			indexDocument(document, 0);
		}
	}

	private static void indexDocument (Document document, int count) {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(ALL_INDEX_NAME)
				.build();
		Index index = ensureSearchService().getIndex(indexSpec);

		try {
			index.put(document);
		} catch (PutException e) {
			if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult()
					.getCode())) {
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
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(ALL_INDEX_NAME)
				.build();
		Index index = ensureSearchService().getIndex(indexSpec);

		Results<ScoredDocument> matches = index.search(query);
		List<Post> posts = new ArrayList<Post>();
		String id;
		Post post;
		int limit = SEARCH_LIMIT;
		final String postName = PostServiceProvider.provide().getName();
		for (ScoredDocument scoredDocument : matches) {
			if (limit == 0) {
				break;
			}

			if ((id = scoredDocument.getId()).startsWith(postName)) {
				post = PostServiceProvider.provide().getPost(
						Long.valueOf(id.replace(postName, "")));
				if (post != null) {
					posts.add(post);
				}
			}

			limit--;
		}

		return posts;
	}
}
