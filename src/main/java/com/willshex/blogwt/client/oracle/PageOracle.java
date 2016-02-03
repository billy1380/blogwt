//
//  PageOracle.java
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
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.page.call.GetPagesRequest;
import com.willshex.blogwt.shared.api.page.call.GetPagesResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PageOracle extends SuggestOracle<Page> {

	private com.google.gwt.http.client.Request getPagesRequest;

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#lookup(com.google.gwt
	 * .user.client.ui.SuggestOracle.Request,
	 * com.google.gwt.user.client.ui.SuggestOracle.Callback) */
	@Override
	protected void lookup (final Request request, final Callback callback) {
		final GetPagesRequest input = ApiHelper
				.setAccessCode(new GetPagesRequest());
		input.session = SessionController.get().sessionForApiCall();
		input.includePosts = Boolean.FALSE;
		input.query = request.getQuery();
		input.pager = PagerHelper.createDefaultPager();
		input.pager.count = Integer.valueOf(request.getLimit());

		if (getPagesRequest != null) {
			getPagesRequest.cancel();
		}

		getPagesRequest = ApiHelper.createPageClient().getPages(input,
				new AsyncCallback<GetPagesResponse>() {

					@Override
					public void onSuccess (GetPagesResponse output) {
						if (output.status == StatusType.StatusTypeSuccess
								&& output.pager != null) {
							foundItems(request, callback, output.pages);
						} else {
							foundItems(request, callback,
									Collections.<Page> emptyList());
						}
					}

					@Override
					public void onFailure (Throwable caught) {
						GWT.log("Error getting pages with query " + input.query,
								caught);

						foundItems(request, callback,
								Collections.<Page> emptyList());
					}
				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getDisplayString(java
	 * .lang.Object) */
	@Override
	protected String getDisplayString (Page item) {
		return item.title;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getReplacementString
	 * (java.lang.Object) */
	@Override
	protected String getReplacementString (Page item) {
		return item.slug;
	}
}
