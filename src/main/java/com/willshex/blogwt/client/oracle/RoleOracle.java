//
//  RoleOracle.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 3 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.oracle;

import java.util.Collections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.user.call.GetRolesRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RoleOracle extends SuggestOracle<Role> {

	public interface NameDescriptionTemplates extends SafeHtmlTemplates {

		NameDescriptionTemplates INSTANCE = GWT
				.create(NameDescriptionTemplates.class);

		@Template("{0}<div class=\"small text-muted\">{1}</div>")
		SafeHtml description (String name, String description);
	}

	private com.google.gwt.http.client.Request getRolesRequest;

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#lookup(com.google.gwt
	 * .user.client.ui.SuggestOracle.Request,
	 * com.google.gwt.user.client.ui.SuggestOracle.Callback) */
	@Override
	protected void lookup (final Request request, final Callback callback) {
		final GetRolesRequest input = ApiHelper
				.setAccessCode(new GetRolesRequest());
		input.session = SessionController.get().sessionForApiCall();
		input.query = request.getQuery();
		input.pager = PagerHelper.createDefaultPager();
		input.pager.count = Integer.valueOf(request.getLimit());

		if (getRolesRequest != null) {
			getRolesRequest.cancel();
		}

		getRolesRequest = ApiHelper.createUserClient().getRoles(input,
				new AsyncCallback<GetRolesResponse>() {

					@Override
					public void onSuccess (GetRolesResponse output) {
						if (output.status == StatusType.StatusTypeSuccess
								&& output.pager != null) {
							foundItems(request, callback, output.roles);
						} else {
							foundItems(request, callback,
									Collections.<Role> emptyList());
						}
					}

					@Override
					public void onFailure (Throwable caught) {
						GWT.log("Error getting roles with query " + input.query,
								caught);

						foundItems(request, callback,
								Collections.<Role> emptyList());
					}
				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getDisplayString(java
	 * .lang.Object) */
	@Override
	protected String getDisplayString (Role item) {
		return NameDescriptionTemplates.INSTANCE.description(item.name,
				item.description).asString();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getReplacementString
	 * (java.lang.Object) */
	@Override
	protected String getReplacementString (Role item) {
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
