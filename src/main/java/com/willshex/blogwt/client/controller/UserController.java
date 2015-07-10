//
//  UserController.java
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
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.datatype.UserSortType;
import com.willshex.blogwt.shared.api.helper.PagerHelper;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.GetUsersRequest;
import com.willshex.blogwt.shared.api.user.call.GetUsersResponse;
import com.willshex.blogwt.shared.api.user.call.event.GetUserDetailsEventHandler.GetUserDetailsFailure;
import com.willshex.blogwt.shared.api.user.call.event.GetUserDetailsEventHandler.GetUserDetailsSuccess;
import com.willshex.blogwt.shared.api.user.call.event.GetUsersEventHandler.GetUsersFailure;
import com.willshex.blogwt.shared.api.user.call.event.GetUsersEventHandler.GetUsersSuccess;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UserController extends AsyncDataProvider<User> {

	private static UserController one = null;

	public static UserController get () {
		if (one == null) {
			one = new UserController();
		}

		return one;
	}

	private Pager pager = PagerHelper.createDefaultPager().sortBy(
			UserSortType.UserSortTypeAdded.toString());

	private Request getUsersRequest;
	private Request getUserDetailsRequest;

	private void fetchUsers () {
		final GetUsersRequest input = ApiHelper
				.setAccessCode(new GetUsersRequest());
		input.pager = pager;
		input.session = SessionController.get().sessionForApiCall();

		if (getUsersRequest != null) {
			getUsersRequest.cancel();
		}

		getUsersRequest = ApiHelper.createUserClient().getUsers(input,
				new AsyncCallback<GetUsersResponse>() {

					@Override
					public void onSuccess (GetUsersResponse output) {
						getUsersRequest = null;

						if (output.status == StatusType.StatusTypeSuccess) {
							if (output.users != null && output.users.size() > 0) {
								pager = output.pager;
								updateRowCount(
										input.pager.count == null ? 0
												: input.pager.count.intValue(),
										input.pager.count == null
												|| input.pager.count.intValue() == 0);
								updateRowData(input.pager.start.intValue(),
										output.users);
							} else {
								updateRowCount(input.pager.start.intValue(),
										true);
								updateRowData(input.pager.start.intValue(),
										Collections.<User> emptyList());
							}
						}

						DefaultEventBus.get().fireEventFromSource(
								new GetUsersSuccess(input, output),
								UserController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						getUsersRequest = null;

						DefaultEventBus.get().fireEventFromSource(
								new GetUsersFailure(input, caught),
								UserController.this);
					}

				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData) */
	@Override
	protected void onRangeChanged (HasData<User> display) {
		Range range = display.getVisibleRange();
		pager.start(Integer.valueOf(range.getStart())).count(
				Integer.valueOf(range.getLength()));

		fetchUsers();
	}

	/**
	 * @param user
	 */
	public void getUser (User user) {
		final GetUserDetailsRequest input = ApiHelper
				.setAccessCode(new GetUserDetailsRequest());

		input.session = SessionController.get().sessionForApiCall();
		input.user = user;

		if (getUserDetailsRequest != null) {
			getUserDetailsRequest.cancel();
		}

		getUserDetailsRequest = ApiHelper.createUserClient().getUserDetails(
				input, new AsyncCallback<GetUserDetailsResponse>() {

					@Override
					public void onSuccess (GetUserDetailsResponse output) {
						getUserDetailsRequest = null;

						if (output.status == StatusType.StatusTypeSuccess) {

						}

						DefaultEventBus.get().fireEventFromSource(
								new GetUserDetailsSuccess(input, output),
								UserController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new GetUserDetailsFailure(input, caught),
								UserController.this);
					}

				});
	}

}
