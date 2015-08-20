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
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SearchHelper {

	private final static String ALL_INDEX_NAME = "all";

	public static void indexDocument (Document document) {
		if (document != null) {
			indexDocument(document, 0);
		}
	}

	private static void indexDocument (Document document, int count) {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(ALL_INDEX_NAME)
				.build();
		Index index = SearchServiceFactory.getSearchService().getIndex(
				indexSpec);

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
}
