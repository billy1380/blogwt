//
//  SearchController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.search.event.SearchAllEventHandler.SearchAllFailure;
import com.willshex.blogwt.client.api.search.event.SearchAllEventHandler.SearchAllSuccess;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.search.call.SearchAllRequest;
import com.willshex.blogwt.shared.api.search.call.SearchAllResponse;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SearchController extends
		AsyncDataProvider<SearchController.SearchResult> {
	private static SearchController one = null;

	/**
	 * @return
	 */
	public static SearchController get () {
		if (one == null) {
			one = new SearchController();
		}

		return one;
	}

	public static class SearchResult {

		private User user;
		private Page page;
		private Post post;

		/**
		 * @param post
		 */
		public SearchResult (Post post) {
			this.post = post;
		}

		/**
		 * @param page
		 */
		public SearchResult (Page page) {
			this.page = page;
		}

		/**
		 * @param user
		 */
		public SearchResult (User user) {
			this.user = user;
		}

		public boolean isUser () {
			return user != null;
		}

		public boolean isPage () {
			return page != null;
		}

		public boolean isPost () {
			return post != null;
		}

		/**
		 * @return the user
		 */
		public User getUser () {
			return user;
		}

		/**
		 * @return the page
		 */
		public Page getPage () {
			return page;
		}

		/**
		 * @return the post
		 */
		public Post getPost () {
			return post;
		}
	}

	private String query;
	private Request searchAllRequest;

	private void fetchSearchResults (String query) {
		final SearchAllRequest input = ApiHelper
				.setAccessCode(new SearchAllRequest());
		input.session = SessionController.get().sessionForApiCall();
		input.query = query;

		if (searchAllRequest != null) {
			searchAllRequest.cancel();
		}

		searchAllRequest = ApiHelper.createSearchClient().searchAll(input,
				new AsyncCallback<SearchAllResponse>() {

					@Override
					public void onSuccess (SearchAllResponse output) {
						searchAllRequest = null;

						if (output.status == StatusType.StatusTypeSuccess) {
							List<SearchResult> results = new ArrayList<SearchController.SearchResult>();

							if (output.posts != null) {
								for (Post post : output.posts) {
									results.add(new SearchResult(post));
								}
							}

							if (output.pages != null) {
								for (Page page : output.pages) {
									results.add(new SearchResult(page));
								}
							}

							if (output.users != null) {
								for (User user : output.users) {
									results.add(new SearchResult(user));
								}
							}

							updateRowCount(results.size(), true);
							updateRowData(0, results);
						}

						DefaultEventBus.get().fireEventFromSource(
								new SearchAllSuccess(input, output),
								SearchController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						searchAllRequest = null;

						DefaultEventBus.get().fireEventFromSource(
								new SearchAllFailure(input, caught),
								SearchController.this);
					}

				});
	}

	public void setQuery (String value) {
		if (value != null) {
			query = value;

			fetchSearchResults(query);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData) */
	@Override
	protected void onRangeChanged (HasData<SearchResult> display) {}

}
