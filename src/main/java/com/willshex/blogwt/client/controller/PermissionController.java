//
//  PermissionController.java
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
import com.willshex.blogwt.client.api.user.event.GetPermissionsEventHandler.GetPermissionsFailure;
import com.willshex.blogwt.client.api.user.event.GetPermissionsEventHandler.GetPermissionsSuccess;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.client.oracle.PermissionOracle;
import com.willshex.blogwt.client.oracle.SuggestOracle;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.PermissionSortType;
import com.willshex.blogwt.shared.api.user.call.GetPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetPermissionsResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PermissionController extends AsyncDataProvider<Permission> {

	private static PermissionController one = null;

	public static PermissionController get () {
		if (one == null) {
			one = new PermissionController();
		}

		return one;
	}

	private Pager pager = PagerHelper.createDefaultPager().sortBy(
			PermissionSortType.PermissionSortTypeCode.toString());

	private Request getPermissionsRequest;
	private PermissionOracle oracle;

	private void fetchPermissions () {
		final GetPermissionsRequest input = ApiHelper
				.setAccessCode(new GetPermissionsRequest());
		input.pager = pager;
		input.session = SessionController.get().sessionForApiCall();

		if (getPermissionsRequest != null) {
			getPermissionsRequest.cancel();
		}

		getPermissionsRequest = ApiHelper.createUserClient().getPermissions(
				input, new AsyncCallback<GetPermissionsResponse>() {

					@Override
					public void onSuccess (GetPermissionsResponse output) {
						getPermissionsRequest = null;

						if (output.status == StatusType.StatusTypeSuccess) {
							if (output.permissions != null
									&& output.permissions.size() > 0) {
								pager = output.pager;
								updateRowCount(
										input.pager.count == null ? 0
												: input.pager.count.intValue(),
										input.pager.count == null
												|| input.pager.count.intValue() == 0);
								updateRowData(input.pager.start.intValue(),
										output.permissions);
							} else {
								updateRowCount(input.pager.start.intValue(),
										true);
								updateRowData(input.pager.start.intValue(),
										Collections.<Permission> emptyList());
							}
						}

						DefaultEventBus.get().fireEventFromSource(
								new GetPermissionsSuccess(input, output),
								PermissionController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						getPermissionsRequest = null;

						DefaultEventBus.get().fireEventFromSource(
								new GetPermissionsFailure(input, caught),
								PermissionController.this);
					}

				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData) */
	@Override
	protected void onRangeChanged (HasData<Permission> display) {
		Range range = display.getVisibleRange();
		pager.start(Integer.valueOf(range.getStart())).count(
				Integer.valueOf(range.getLength()));

		fetchPermissions();
	}

	public SuggestOracle<Permission> oracle () {
		if (oracle == null) {
			oracle = new PermissionOracle();
		}
		return oracle;
	}

}
