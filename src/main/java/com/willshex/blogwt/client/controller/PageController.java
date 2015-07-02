//
//  PageController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 25 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.Collections;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.helper.PagerHelper;
import com.willshex.blogwt.shared.api.page.call.CreatePageRequest;
import com.willshex.blogwt.shared.api.page.call.CreatePageResponse;
import com.willshex.blogwt.shared.api.page.call.GetPagesRequest;
import com.willshex.blogwt.shared.api.page.call.GetPagesResponse;
import com.willshex.blogwt.shared.api.page.call.event.CreatePageEventHandler.CreatePageFailure;
import com.willshex.blogwt.shared.api.page.call.event.CreatePageEventHandler.CreatePageSuccess;
import com.willshex.blogwt.shared.api.page.call.event.GetPagesEventHandler.GetPagesFailure;
import com.willshex.blogwt.shared.api.page.call.event.GetPagesEventHandler.GetPagesSuccess;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PageController extends AsyncDataProvider<Page> {

	private static PageController one = null;

	public static PageController get () {
		if (one == null) {
			one = new PageController();
		}

		return one;
	}

	private Pager pager = PagerHelper.createDefaultPager();

	private Request getPagesRequest;

	private void fetchPages () {
		final GetPagesRequest input = ApiHelper
				.setAccessCode(new GetPagesRequest());
		input.pager = pager;
		input.session = SessionController.get().sessionForApiCall();

		if (getPagesRequest != null) {
			getPagesRequest.cancel();
		}

		getPagesRequest = ApiHelper.createPageClient().getPages(input,
				new AsyncCallback<GetPagesResponse>() {

					@Override
					public void onSuccess (GetPagesResponse output) {
						getPagesRequest = null;

						if (output.status == StatusType.StatusTypeSuccess) {
							if (output.pages != null && output.pages.size() > 0) {
								pager = output.pager;
								updateRowCount(
										input.pager.count == null ? 0
												: input.pager.count.intValue(),
										input.pager.count == null
												|| input.pager.count.intValue() == 0);
								updateRowData(input.pager.start.intValue(),
										output.pages);
							} else {
								updateRowCount(input.pager.start.intValue(),
										true);
								updateRowData(input.pager.start.intValue(),
										Collections.<Page> emptyList());
							}
						}

						DefaultEventBus.get().fireEventFromSource(
								new GetPagesSuccess(input, output),
								PageController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						getPagesRequest = null;

						DefaultEventBus.get().fireEventFromSource(
								new GetPagesFailure(input, caught),
								PageController.this);
					}

				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData) */
	@Override
	protected void onRangeChanged (HasData<Page> display) {
		Range range = display.getVisibleRange();
		pager.start(Integer.valueOf(range.getStart())).count(
				Integer.valueOf(range.getLength()));

		fetchPages();
	}

	/**
	 * @param page
	 */
	public void createPage (Page page) {
		final CreatePageRequest input = SessionController
				.get()
				.setSession(ApiHelper.setAccessCode(new CreatePageRequest()))
				.page(page);

		ApiHelper.createPageClient().createPage(input, new AsyncCallback<CreatePageResponse>() {

			@Override
			public void onSuccess (CreatePageResponse output) {
				if (output.status == StatusType.StatusTypeSuccess) {}

				DefaultEventBus.get().fireEventFromSource(
						new CreatePageSuccess(input, output),
						PageController.this);
			}

			@Override
			public void onFailure (Throwable caught) {
				DefaultEventBus.get().fireEventFromSource(
						new CreatePageFailure(input, caught),
						PageController.this);
			}
		});
	}
}
