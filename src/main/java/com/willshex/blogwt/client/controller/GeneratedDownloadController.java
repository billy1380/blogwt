//
//  GeneratedDownloadController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 27 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.Arrays;
import java.util.Collections;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.download.event.DeleteGeneratedDownloadsEventHandler.DeleteGeneratedDownloadsFailure;
import com.willshex.blogwt.client.api.download.event.DeleteGeneratedDownloadsEventHandler.DeleteGeneratedDownloadsSuccess;
import com.willshex.blogwt.client.api.download.event.GenerateDownloadEventHandler;
import com.willshex.blogwt.client.api.download.event.GetGeneratedDownloadsEventHandler.GetGeneratedDownloadsFailure;
import com.willshex.blogwt.client.api.download.event.GetGeneratedDownloadsEventHandler.GetGeneratedDownloadsSuccess;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownloadSortType;
import com.willshex.blogwt.shared.api.download.call.DeleteGeneratedDownloadsRequest;
import com.willshex.blogwt.shared.api.download.call.DeleteGeneratedDownloadsResponse;
import com.willshex.blogwt.shared.api.download.call.GenerateDownloadRequest;
import com.willshex.blogwt.shared.api.download.call.GenerateDownloadResponse;
import com.willshex.blogwt.shared.api.download.call.GetGeneratedDownloadsRequest;
import com.willshex.blogwt.shared.api.download.call.GetGeneratedDownloadsResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.page.search.Filter;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class GeneratedDownloadController
		extends AsyncDataProvider<GeneratedDownload> {
	private static GeneratedDownloadController one = null;

	public static GeneratedDownloadController get () {
		if (one == null) {
			one = new GeneratedDownloadController();
		}

		return one;
	}

	private Pager pager = PagerHelper.createDefaultPager()
			.sortBy(GeneratedDownloadSortType.GeneratedDownloadSortTypeCreated
					.toString());
	private Request getGeneratedDownloadsRequest = null;

	private void fetchGeneratedDownloads () {
		final GetGeneratedDownloadsRequest input = ApiHelper
				.setAccessCode(new GetGeneratedDownloadsRequest()).pager(pager);
		input.session(SessionController.get().sessionForApiCall());

		if (getGeneratedDownloadsRequest != null) {
			getGeneratedDownloadsRequest.cancel();
		}

		getGeneratedDownloadsRequest = ApiHelper.createDownloadClient()
				.getGeneratedDownloads(input,
						new AsyncCallback<GetGeneratedDownloadsResponse>() {

							@Override
							public void onSuccess (
									GetGeneratedDownloadsResponse output) {
								getGeneratedDownloadsRequest = null;

								if (output.status == StatusType.StatusTypeSuccess) {
									if (output.downloads != null
											&& output.downloads.size() > 0) {
										pager = output.pager;
										updateRowCount(
												input.pager.count == null ? 0
														: input.pager.count
																.intValue(),
												input.pager.count == null
														|| input.pager.count
																.intValue() == 0);
										updateRowData(
												input.pager.start.intValue(),
												output.downloads);
									} else {
										updateRowCount(
												input.pager.start.intValue(),
												true);
										updateRowData(
												input.pager.start.intValue(),
												Collections
														.<GeneratedDownload> emptyList());
									}
								}

								DefaultEventBus.get().fireEventFromSource(
										new GetGeneratedDownloadsSuccess(input,
												output),
										GeneratedDownloadController.this);
							}

							@Override
							public void onFailure (Throwable caught) {
								getGeneratedDownloadsRequest = null;

								DefaultEventBus.get().fireEventFromSource(
										new GetGeneratedDownloadsFailure(input,
												caught),
										GeneratedDownloadController.this);
							}
						});
	}

	public void generateDownload (Filter filter) {
		final GenerateDownloadRequest input = ApiHelper
				.setAccessCode(new GenerateDownloadRequest())
				.download(new GeneratedDownload().parameters(filter.url()));
		input.session = SessionController.get().sessionForApiCall();

		ApiHelper.createDownloadClient().generateDownload(input,
				new AsyncCallback<GenerateDownloadResponse>() {

					@Override
					public void onSuccess (GenerateDownloadResponse output) {
						DefaultEventBus.get().fireEventFromSource(
								new GenerateDownloadEventHandler.GenerateDownloadSuccess(
										input, output),
								GeneratedDownloadController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new GenerateDownloadEventHandler.GenerateDownloadFailure(
										input, caught),
								GeneratedDownloadController.this);
					}
				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData) */
	@Override
	protected void onRangeChanged (HasData<GeneratedDownload> display) {
		Range range = display.getVisibleRange();
		pager.start(Integer.valueOf(range.getStart()))
				.count(Integer.valueOf(range.getLength()));

		fetchGeneratedDownloads();
	}

	public void deleteGeneratedDownload (GeneratedDownload generatedDownload) {

		final DeleteGeneratedDownloadsRequest input = ApiHelper
				.setAccessCode(new DeleteGeneratedDownloadsRequest());

		input.session = SessionController.get().sessionForApiCall();
		input.downloads = Arrays.asList(generatedDownload);

		ApiHelper.createDownloadClient().deleteGeneratedDownloads(input,
				new AsyncCallback<DeleteGeneratedDownloadsResponse>() {

					@Override
					public void onSuccess (
							DeleteGeneratedDownloadsResponse output) {
						DefaultEventBus.get().fireEventFromSource(
								new DeleteGeneratedDownloadsSuccess(input,
										output),
								GeneratedDownloadController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new DeleteGeneratedDownloadsFailure(input,
										caught),
								GeneratedDownloadController.this);
					}
				});
	}
}