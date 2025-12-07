//
//  ResourceController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jun 2015.
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
import com.willshex.blogwt.client.api.blog.event.DeleteResourceEventHandler.DeleteResourceFailure;
import com.willshex.blogwt.client.api.blog.event.DeleteResourceEventHandler.DeleteResourceSuccess;
import com.willshex.blogwt.client.api.blog.event.GetResourceEventHandler.GetResourceFailure;
import com.willshex.blogwt.client.api.blog.event.GetResourceEventHandler.GetResourceSuccess;
import com.willshex.blogwt.client.api.blog.event.GetResourcesEventHandler.GetResourcesFailure;
import com.willshex.blogwt.client.api.blog.event.GetResourcesEventHandler.GetResourcesSuccess;
import com.willshex.blogwt.client.api.blog.event.UpdateResourceEventHandler.UpdateResourceFailure;
import com.willshex.blogwt.client.api.blog.event.UpdateResourceEventHandler.UpdateResourceSuccess;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceResponse;
import com.willshex.blogwt.shared.api.blog.call.GetResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourceResponse;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesResponse;
import com.willshex.blogwt.shared.api.blog.call.UpdateResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdateResourceResponse;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.ResourceSortType;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ResourceController extends AsyncDataProvider<Resource> {

	private static ResourceController one = null;

	public static ResourceController get () {
		if (one == null) {
			one = new ResourceController();
		}

		return one;
	}

	private Pager pager = PagerHelper.createDefaultPager()
			.sortBy(ResourceSortType.ResourceSortTypeCreated.toString());
	private Request getResourcesRequest;

	private void fetchResources () {
		final GetResourcesRequest input = ApiHelper
				.setAccessCode(new GetResourcesRequest());
		input.pager = pager;
		input.session = SessionController.get().sessionForApiCall();

		if (getResourcesRequest != null) {
			getResourcesRequest.cancel();
		}

		getResourcesRequest = ApiHelper.createBlogClient().getResources(input,
				new AsyncCallback<GetResourcesResponse>() {

					@Override
					public void onSuccess (GetResourcesResponse output) {
						getResourcesRequest = null;

						if (output.status == StatusType.StatusTypeSuccess) {
							if (output.resources != null
									&& output.resources.size() > 0) {
								pager = output.pager;
								updateRowCount(
										input.pager.count == null ? 0
												: input.pager.count.intValue(),
										input.pager.count == null
												|| input.pager.count
														.intValue() == 0);
								updateRowData(input.pager.start.intValue(),
										output.resources);
							} else {
								updateRowCount(input.pager.start.intValue(),
										true);
								updateRowData(input.pager.start.intValue(),
										Collections.<Resource> emptyList());
							}
						}

						DefaultEventBus.get().fireEventFromSource(
								new GetResourcesSuccess(input, output),
								ResourceController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						getResourcesRequest = null;

						DefaultEventBus.get().fireEventFromSource(
								new GetResourcesFailure(input, caught),
								ResourceController.this);
					}

				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData) */
	@Override
	protected void onRangeChanged (HasData<Resource> display) {
		Range range = display.getVisibleRange();
		pager.start(Integer.valueOf(range.getStart()))
				.count(Integer.valueOf(range.getLength()));

		fetchResources();
	}

	public void deleteResource (Resource resource) {
		final DeleteResourceRequest input = SessionController.get()
				.setSession(
						ApiHelper.setAccessCode(new DeleteResourceRequest()))
				.resource(resource);

		ApiHelper.createBlogClient().deleteResource(input,
				new AsyncCallback<DeleteResourceResponse>() {

					@Override
					public void onSuccess (DeleteResourceResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {
							if (input.resource != null) {}
						}

						DefaultEventBus.get().fireEventFromSource(
								new DeleteResourceSuccess(input, output),
								ResourceController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new DeleteResourceFailure(input, caught),
								ResourceController.this);
					}
				});
	}

	public void getResource (Resource resource) {
		final GetResourceRequest input = SessionController.get()
				.setSession(ApiHelper.setAccessCode(new GetResourceRequest()))
				.resource(resource);

		ApiHelper.createBlogClient().getResource(input,
				new AsyncCallback<GetResourceResponse>() {

					@Override
					public void onSuccess (GetResourceResponse output) {

						if (output.status == StatusType.StatusTypeSuccess) {
							if (input.resource != null) {}
						}

						DefaultEventBus.get().fireEventFromSource(
								new GetResourceSuccess(input, output),
								ResourceController.this);
					}

					@Override
					public void onFailure (Throwable caught) {

						DefaultEventBus.get().fireEventFromSource(
								new GetResourceFailure(input, caught),
								ResourceController.this);
					}
				});
	}

	public void updateResource (Resource resource) {
		final UpdateResourceRequest input = SessionController.get()
				.setSession(
						ApiHelper.setAccessCode(new UpdateResourceRequest()))
				.resource(resource);

		ApiHelper.createBlogClient().updateResource(input,
				new AsyncCallback<UpdateResourceResponse>() {

					@Override
					public void onSuccess (UpdateResourceResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {
							if (input.resource != null) {}
						}

						DefaultEventBus.get().fireEventFromSource(
								new UpdateResourceSuccess(input, output),
								ResourceController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new UpdateResourceFailure(input, caught),
								ResourceController.this);
					}
				});
	}

}
