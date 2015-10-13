//
//  PostOracle.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.oracle;

import java.util.Collections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostsResponse;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostOracle extends SuggestOracle<Post> {

	private com.google.gwt.http.client.Request getPostsRequest;

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#lookup(com.google.gwt
	 * .user.client.ui.SuggestOracle.Request,
	 * com.google.gwt.user.client.ui.SuggestOracle.Callback) */
	@Override
	protected void lookup (final Request request, final Callback callback) {
		final GetPostsRequest input = ApiHelper
				.setAccessCode(new GetPostsRequest());
		input.session = SessionController.get().sessionForApiCall();
		input.includePostContents = Boolean.FALSE;
		input.query = request.getQuery();
		input.pager = PagerHelper.createDefaultPager();
		input.pager.count = Integer.valueOf(request.getLimit());
		input.showAll = Boolean.TRUE;

		if (getPostsRequest != null) {
			getPostsRequest.cancel();
		}

		getPostsRequest = ApiHelper.createBlogClient().getPosts(input,
				new AsyncCallback<GetPostsResponse>() {

					@Override
					public void onSuccess (GetPostsResponse output) {
						if (output.status == StatusType.StatusTypeSuccess
								&& output.pager != null) {
							foundItems(request, callback, output.posts);
						} else {
							foundItems(request, callback,
									Collections.<Post> emptyList());
						}
					}

					@Override
					public void onFailure (Throwable caught) {
						GWT.log("Error getting posts with query " + input.query,
								caught);

						foundItems(request, callback,
								Collections.<Post> emptyList());
					}
				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getDisplayString(java
	 * .lang.Object) */
	@Override
	protected String getDisplayString (Post item) {
		return item.title;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getReplacementString
	 * (java.lang.Object) */
	@Override
	protected String getReplacementString (Post item) {
		return item.slug;
	}

}
