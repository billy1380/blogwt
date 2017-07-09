//
//  UserOracle.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 26 Aug 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.oracle;

import java.util.Collections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.client.helper.UserHelper;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.GetUsersRequest;
import com.willshex.blogwt.shared.api.user.call.GetUsersResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UserOracle extends SuggestOracle<User> {

	public interface Templates extends SafeHtmlTemplates {
		public Templates INSTANCE = GWT.create(Templates.class);

		@Template("<img class=\"img-circle\" src=\"{0}\" alt=\"{1}\" /> {1}")
		SafeHtml displayString (SafeUri avatarUrl, String name);
	}

	private User user;
	private com.google.gwt.http.client.Request getUsersRequest;

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#lookup(com.google.gwt
	 * .user.client.ui.SuggestOracle.Request,
	 * com.google.gwt.user.client.ui.SuggestOracle.Callback) */
	@Override
	protected void lookup (final Request request, final Callback callback) {
		final GetUsersRequest input = ApiHelper
				.setAccessCode(new GetUsersRequest());
		input.session = SessionController.get().sessionForApiCall();
		input.query = request.getQuery();
		input.pager = PagerHelper.createDefaultPager();
		input.pager.count = Integer.valueOf(request.getLimit());

		if (getUsersRequest != null) {
			getUsersRequest.cancel();
		}

		clearSelected();

		getUsersRequest = ApiHelper.createUserClient().getUsers(input,
				new AsyncCallback<GetUsersResponse>() {

					@Override
					public void onSuccess (GetUsersResponse output) {
						if (output.status == StatusType.StatusTypeSuccess
								&& output.pager != null) {
							foundItems(request, callback, output.users);
						} else {
							foundItems(request, callback,
									Collections.<User> emptyList());
						}
					}

					@Override
					public void onFailure (Throwable caught) {
						GWT.log("Error getting users with query " + input.query,
								caught);

						foundItems(request, callback,
								Collections.<User> emptyList());
					}
				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getDisplayString(java
	 * .lang.Object) */
	@Override
	protected String getDisplayString (User item) {
		return Templates.INSTANCE
				.displayString(UserHelper.avatar(item), UserHelper.name(item))
				.asString();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.oracle.SuggestOracle#getReplacementString
	 * (java.lang.Object) */
	@Override
	protected String getReplacementString (User item) {
		user = item;
		return item.getClass().getSimpleName() + ":" + item.id;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.SuggestOracle#isDisplayStringHTML() */
	@Override
	public boolean isDisplayStringHTML () {
		return true;
	}

	/**
	 * @param value
	 * @return
	 */
	public User selected () {
		return user;
	}

	public void clearSelected () {
		user = null;
	}

}
