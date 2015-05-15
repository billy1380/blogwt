//
//  SetupController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.blog.BlogService;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogRequest;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogResponse;
import com.willshex.blogwt.shared.api.blog.call.event.SetupBlogEventHandler.SetupBlogFailure;
import com.willshex.blogwt.shared.api.blog.call.event.SetupBlogEventHandler.SetupBlogSuccess;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.User;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SetupController {

	private static SetupController one = null;

	public static SetupController get () {
		if (one == null) {
			one = new SetupController();
		}

		return one;
	}

	public void setupBlog (List<Property> properties, List<User> users) {
		final SetupBlogRequest input = ApiHelper
				.setAccessCode(new SetupBlogRequest()).properties(properties)
				.users(users);

		BlogService blogService = ApiHelper.createBlogClient();
		blogService.setupBlog(input, new AsyncCallback<SetupBlogResponse>() {

			@Override
			public void onSuccess (SetupBlogResponse output) {
				DefaultEventBus.get().fireEventFromSource(
						new SetupBlogSuccess(input, output),
						SetupController.this);
			}

			@Override
			public void onFailure (Throwable caught) {
				DefaultEventBus.get().fireEventFromSource(
						new SetupBlogFailure(input, caught),
						SetupController.this);
			}
		});
	}

}
