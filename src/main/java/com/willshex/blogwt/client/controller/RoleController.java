//
//  RoleController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 23 Jun 2015.
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
import com.willshex.blogwt.shared.api.datatype.PermissionSortType;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.helper.PagerHelper;
import com.willshex.blogwt.shared.api.user.call.GetRolesRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesResponse;
import com.willshex.blogwt.shared.api.user.call.event.GetRolesEventHandler.GetRolesFailure;
import com.willshex.blogwt.shared.api.user.call.event.GetRolesEventHandler.GetRolesSuccess;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RoleController extends AsyncDataProvider<Role> {
	private static RoleController one = null;

	public static RoleController get () {
		if (one == null) {
			one = new RoleController();
		}

		return one;
	}

	private Pager pager = PagerHelper.createDefaultPager().sortBy(
			PermissionSortType.PermissionSortTypeCode.toString());

	private Request getRolesRequest;

	private void fetchRoles () {
		final GetRolesRequest input = ApiHelper
				.setAccessCode(new GetRolesRequest());
		input.pager = pager;
		input.session = SessionController.get().sessionForApiCall();

		if (getRolesRequest != null) {
			getRolesRequest.cancel();
		}

		getRolesRequest = ApiHelper.createUserClient().getRoles(input,
				new AsyncCallback<GetRolesResponse>() {

					@Override
					public void onSuccess (GetRolesResponse output) {
						getRolesRequest = null;

						if (output.status == StatusType.StatusTypeSuccess) {
							if (output.roles != null && output.roles.size() > 0) {
								pager = output.pager;
								updateRowCount(
										input.pager.count == null ? 0
												: input.pager.count.intValue(),
										input.pager.count == null
												|| input.pager.count.intValue() == 0);
								updateRowData(input.pager.start.intValue(),
										output.roles);
							} else {
								updateRowCount(input.pager.start.intValue(),
										true);
								updateRowData(input.pager.start.intValue(),
										Collections.<Role> emptyList());
							}
						}

						DefaultEventBus.get().fireEventFromSource(
								new GetRolesSuccess(input, output),
								RoleController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						getRolesRequest = null;

						DefaultEventBus.get().fireEventFromSource(
								new GetRolesFailure(input, caught),
								RoleController.this);
					}

				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData) */
	@Override
	protected void onRangeChanged (HasData<Role> display) {
		Range range = display.getVisibleRange();
		pager.start(Integer.valueOf(range.getStart())).count(
				Integer.valueOf(range.getLength()));

		fetchRoles();
	}

}
