//
//  PageController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 25 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gwt.http.client.Request;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.page.event.CreatePageEventHandler.CreatePageFailure;
import com.willshex.blogwt.client.api.page.event.CreatePageEventHandler.CreatePageSuccess;
import com.willshex.blogwt.client.api.page.event.DeletePageEventHandler.DeletePageFailure;
import com.willshex.blogwt.client.api.page.event.DeletePageEventHandler.DeletePageSuccess;
import com.willshex.blogwt.client.api.page.event.GetPageEventHandler.GetPageFailure;
import com.willshex.blogwt.client.api.page.event.GetPageEventHandler.GetPageSuccess;
import com.willshex.blogwt.client.api.page.event.GetPagesEventHandler.GetPagesFailure;
import com.willshex.blogwt.client.api.page.event.GetPagesEventHandler.GetPagesSuccess;
import com.willshex.blogwt.client.api.page.event.UpdatePageEventHandler.UpdatePageFailure;
import com.willshex.blogwt.client.api.page.event.UpdatePageEventHandler.UpdatePageSuccess;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.oracle.PageOracle;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.page.call.CreatePageRequest;
import com.willshex.blogwt.shared.api.page.call.CreatePageResponse;
import com.willshex.blogwt.shared.api.page.call.DeletePageRequest;
import com.willshex.blogwt.shared.api.page.call.DeletePageResponse;
import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageResponse;
import com.willshex.blogwt.shared.api.page.call.GetPagesRequest;
import com.willshex.blogwt.shared.api.page.call.GetPagesResponse;
import com.willshex.blogwt.shared.api.page.call.UpdatePageRequest;
import com.willshex.blogwt.shared.api.page.call.UpdatePageResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.gson.web.service.shared.StatusType;

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

	private PageOracle oracle;
	private Pager pager = PagerHelper.createDefaultPager();

	private Request getPagesRequest;
	private Request getPageRequest;

	private List<Page> headerPages = null;
	private Page home;

	private PageController () {
		String pagesJson = headerPages();

		if (pagesJson != null) {
			JsonArray jsonPageArray = (new JsonParser()).parse(pagesJson)
					.getAsJsonArray();
			if (headerPages == null) {
				headerPages = new ArrayList<Page>();
			} else {
				headerPages.clear();
			}

			Page item = null;
			for (int i = 0; i < jsonPageArray.size(); i++) {
				if (jsonPageArray.get(i).isJsonObject()) {
					(item = new Page()).fromJson(jsonPageArray.get(i)
							.getAsJsonObject());
					headerPages.add(item);
					if (item.priority != null
							&& item.priority.floatValue() == 0.0f) {
						home = item;
					}
				}
			}
		}
	}

	private void fetchPages () {
		final GetPagesRequest input = ApiHelper
				.setAccessCode(new GetPagesRequest());
		input.pager = pager;
		input.session = SessionController.get().sessionForApiCall();
		input.includePosts = Boolean.FALSE;

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
		final CreatePageRequest input = SessionController.get()
				.setSession(ApiHelper.setAccessCode(new CreatePageRequest()))
				.page(page);

		ApiHelper.createPageClient().createPage(input,
				new AsyncCallback<CreatePageResponse>() {

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

	/**
	 * @param page
	 */
	public void deletePage (Page page) {
		final DeletePageRequest input = SessionController.get()
				.setSession(ApiHelper.setAccessCode(new DeletePageRequest()))
				.page(page);

		ApiHelper.createPageClient().deletePage(input,
				new AsyncCallback<DeletePageResponse>() {

					@Override
					public void onSuccess (DeletePageResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {
							if (input.page != null) {}
						}

						DefaultEventBus.get().fireEventFromSource(
								new DeletePageSuccess(input, output),
								PageController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new DeletePageFailure(input, caught),
								PageController.this);
					}
				});
	}

	/**
	 * @param page
	 */
	public void getPage (Page page, boolean includePosts) {
		final GetPageRequest input = ApiHelper
				.setAccessCode(new GetPageRequest());
		input.session = SessionController.get().sessionForApiCall();
		input.page = page;
		input.includePosts = Boolean.valueOf(includePosts);

		if (getPageRequest != null) {
			getPageRequest.cancel();
		}

		getPageRequest = ApiHelper.createPageClient().getPage(input,
				new AsyncCallback<GetPageResponse>() {

					@Override
					public void onSuccess (GetPageResponse output) {
						getPageRequest = null;

						if (output.status == StatusType.StatusTypeSuccess) {

						}

						DefaultEventBus.get().fireEventFromSource(
								new GetPageSuccess(input, output),
								PageController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						getPageRequest = null;

						DefaultEventBus.get().fireEventFromSource(
								new GetPageFailure(input, caught),
								PageController.this);
					}

				});
	}

	/**
	 * @param page
	 */
	public void updatePage (Page page) {
		final UpdatePageRequest input = SessionController.get()
				.setSession(ApiHelper.setAccessCode(new UpdatePageRequest()))
				.page(page);

		ApiHelper.createPageClient().updatePage(input,
				new AsyncCallback<UpdatePageResponse>() {

					@Override
					public void onSuccess (UpdatePageResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {}

						DefaultEventBus.get().fireEventFromSource(
								new UpdatePageSuccess(input, output),
								PageController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new UpdatePageFailure(input, caught),
								PageController.this);
					}
				});
	}

	public Page homePage () {
		return home;
	}

	public String homePageSlug () {
		return home == null ? PageType.PostsPageType.toString() : home.slug;
	}

	public SafeUri homePageUri () {
		return PageTypeHelper.slugToHref(homePageSlug());
	}

	public String homePageTargetHistoryToken () {
		return home == null ? PageType.PostsPageType.asTargetHistoryToken()
				: ("!" + home.slug);
	}

	public List<Page> getHeaderPages () {
		return headerPages;
	}

	private static native String headerPages ()
	/*-{
		return $wnd['pages'];
	}-*/;

	public SuggestOracle oracle () {
		if (oracle == null) {
			oracle = new PageOracle();
		}

		return oracle;
	}

}
