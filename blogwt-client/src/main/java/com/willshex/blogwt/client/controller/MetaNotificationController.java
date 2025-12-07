//
//  MetaNotificationController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 16 Mar 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.Collections;

import com.google.gwt.http.client.Request;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.notification.event.AddMetaNotificationEventHandler.AddMetaNotificationFailure;
import com.willshex.blogwt.client.api.notification.event.AddMetaNotificationEventHandler.AddMetaNotificationSuccess;
import com.willshex.blogwt.client.api.notification.event.GetMetaNotificationEventHandler.GetMetaNotificationFailure;
import com.willshex.blogwt.client.api.notification.event.GetMetaNotificationEventHandler.GetMetaNotificationSuccess;
import com.willshex.blogwt.client.api.notification.event.GetMetaNotificationsEventHandler.GetMetaNotificationsFailure;
import com.willshex.blogwt.client.api.notification.event.GetMetaNotificationsEventHandler.GetMetaNotificationsSuccess;
import com.willshex.blogwt.client.api.notification.event.UpdateMetaNotificationEventHandler.UpdateMetaNotificationFailure;
import com.willshex.blogwt.client.api.notification.event.UpdateMetaNotificationEventHandler.UpdateMetaNotificationSuccess;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.MetaNotificationSortType;
import com.willshex.blogwt.shared.api.notification.call.AddMetaNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationsRequest;
import com.willshex.blogwt.shared.api.notification.call.UpdateMetaNotificationRequest;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class MetaNotificationController
		extends AsyncDataProvider<MetaNotification> {

	private static MetaNotificationController one = null;

	public static MetaNotificationController get () {
		if (one == null) {
			one = new MetaNotificationController();
		}

		return one;
	}

	private MetaNotificationController () {}

	private Pager pager = PagerHelper.createDefaultPager()
			.sortBy(MetaNotificationSortType.MetaNotificationSortTypeCreated
					.toString());

	private Request getMetaNotificationsRequest;
	private Request getMetaNotificationRequest;

	private void fetchMetaNotifications () {
		if (getMetaNotificationsRequest != null) {
			getMetaNotificationsRequest.cancel();
		}

		getMetaNotificationsRequest = ApiHelper.createNotificationClient()
				.getMetaNotifications(SessionController.get()
						.setSession(ApiHelper
								.setAccessCode(
										new GetMetaNotificationsRequest())
								.pager(pager)),
						(i, o) -> {
							getMetaNotificationsRequest = null;

							if (o.status == StatusType.StatusTypeSuccess) {
								if (o.metas != null && o.metas.size() > 0) {
									pager = o.pager;
									updateRowCount(
											i.pager.count == null ? 0
													: i.pager.count.intValue(),
											i.pager.count == null
													|| i.pager.count
															.intValue() == 0);
									updateRowData(i.pager.start.intValue(),
											o.metas);
								} else {
									updateRowCount(i.pager.start.intValue(),
											true);
									updateRowData(i.pager.start.intValue(),
											Collections
													.<MetaNotification> emptyList());
								}
							}

							DefaultEventBus.get().fireEventFromSource(
									new GetMetaNotificationsSuccess(i, o),
									this);
						}, (i, c) -> {
							getMetaNotificationsRequest = null;

							DefaultEventBus.get().fireEventFromSource(
									new GetMetaNotificationsFailure(i, c),
									this);
						});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData) */
	@Override
	protected void onRangeChanged (HasData<MetaNotification> display) {
		Range range = display.getVisibleRange();
		pager.start(Integer.valueOf(range.getStart()))
				.count(Integer.valueOf(range.getLength()));

		fetchMetaNotifications();
	}

	public void getMetaNotification (MetaNotification metaNotification) {
		if (getMetaNotificationRequest != null) {
			getMetaNotificationRequest.cancel();
		}

		getMetaNotificationRequest = ApiHelper.createNotificationClient()
				.getMetaNotification(SessionController.get()
						.setSession(ApiHelper
								.setAccessCode(new GetMetaNotificationRequest())
								.meta(metaNotification)),
						(i, o) -> {
							getMetaNotificationRequest = null;

							if (o.status == StatusType.StatusTypeSuccess) {}

							DefaultEventBus.get().fireEventFromSource(
									new GetMetaNotificationSuccess(i, o), this);
						}, (i, c) -> {
							getMetaNotificationRequest = null;

							DefaultEventBus.get().fireEventFromSource(
									new GetMetaNotificationFailure(i, c), this);
						});
	}

	public void addMetaNotification (MetaNotification metaNotification) {
		ApiHelper.createNotificationClient()
				.addMetaNotification(SessionController.get()
						.setSession(ApiHelper
								.setAccessCode(new AddMetaNotificationRequest())
								.meta(metaNotification)),
						(i, o) -> {
							if (o.status == StatusType.StatusTypeSuccess) {}

							DefaultEventBus.get().fireEventFromSource(
									new AddMetaNotificationSuccess(i, o), this);
						}, (i, c) -> {
							DefaultEventBus.get().fireEventFromSource(
									new AddMetaNotificationFailure(i, c), this);
						});
	}

	public void updateMetaNotification (MetaNotification metaNotification) {
		ApiHelper.createNotificationClient()
				.updateMetaNotification(SessionController.get()
						.setSession(ApiHelper
								.setAccessCode(
										new UpdateMetaNotificationRequest())
								.meta(metaNotification)),
						(i, o) -> {
							if (o.status == StatusType.StatusTypeSuccess) {}

							DefaultEventBus.get().fireEventFromSource(
									new UpdateMetaNotificationSuccess(i, o),
									this);
						}, (i, c) -> {
							DefaultEventBus.get().fireEventFromSource(
									new UpdateMetaNotificationFailure(i, c),
									this);
						});
	}

}
