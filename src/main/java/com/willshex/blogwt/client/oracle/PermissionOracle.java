//
//  PermissionOracle.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 3 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.oracle;

import java.util.Collections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.client.oracle.RoleOracle.NameDescriptionTemplates;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.user.call.GetPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetPermissionsResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PermissionOracle extends SuggestOracle<Permission> {

	private com.google.gwt.http.client.Request getPermissionsRequest;

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#lookup(com.google.gwt
	 * .user.client.ui.SuggestOracle.Request,
	 * com.google.gwt.user.client.ui.SuggestOracle.Callback) */
	@Override
	protected void lookup (final Request request, final Callback callback) {
		final GetPermissionsRequest input = ApiHelper
				.setAccessCode(new GetPermissionsRequest());
		input.session = SessionController.get().sessionForApiCall();
		input.query = request.getQuery();
		input.pager = PagerHelper.createDefaultPager();
		input.pager.count = Integer.valueOf(request.getLimit());

		if (getPermissionsRequest != null) {
			getPermissionsRequest.cancel();
		}

		getPermissionsRequest = ApiHelper.createUserClient().getPermissions(
				input, new AsyncCallback<GetPermissionsResponse>() {

					@Override
					public void onSuccess (GetPermissionsResponse output) {
						if (output.status == StatusType.StatusTypeSuccess
								&& output.pager != null) {
							foundItems(request, callback, output.permissions);
						} else {
							foundItems(request, callback,
									Collections.<Permission> emptyList());
						}
					}

					@Override
					public void onFailure (Throwable caught) {
						GWT.log("Error getting permissions with query "
								+ input.query, caught);

						foundItems(request, callback,
								Collections.<Permission> emptyList());
					}
				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getDisplayString(java
	 * .lang.Object) */
	@Override
	protected String getDisplayString (Permission item) {
		return NameDescriptionTemplates.INSTANCE.description(item.name,
				item.description).asString();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getReplacementString
	 * (java.lang.Object) */
	@Override
	protected String getReplacementString (Permission item) {
		return item.code;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.SuggestOracle#isDisplayStringHTML() */
	@Override
	public boolean isDisplayStringHTML () {
		return true;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.oracle.SuggestOracle#getQueryMinLength() */
	@Override
	protected int getQueryMinLength () {
		return 0;
	}
}
