//
//  PostController.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 15 May 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.blog.BlogService;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.blog.call.CreatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.CreatePostResponse;
import com.willshex.blogwt.shared.api.blog.call.event.CreatePostEventHandler.CreatePostFailure;
import com.willshex.blogwt.shared.api.blog.call.event.CreatePostEventHandler.CreatePostSuccess;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostContent;
import com.willshex.blogwt.shared.api.helper.TagHelper;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostController extends AsyncDataProvider<Post> {

	private static PostController one = null;

	public static PostController get () {
		if (one == null) {
			one = new PostController();
		}

		return one;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData) */
	@Override
	protected void onRangeChanged (HasData<Post> display) {
		// TODO Auto-generated method stub

	}

	public void createPost (String title, Boolean directOnly,
			Boolean commentsEnabled, String summary, String content,
			Boolean publish, String tags) {
		BlogService blogService = ApiHelper.createBlogClient();

		final CreatePostRequest input = SessionController
				.get()
				.setSession(ApiHelper.setAccessCode(new CreatePostRequest()))
				.post(new Post().title(title).summary(summary)
						.content(new PostContent().body(content))
						.tags(TagHelper.convertToTagList(tags))
						.directOnly(directOnly)
						.commentsEnabled(commentsEnabled)).publish(publish);

		blogService.createPost(input, new AsyncCallback<CreatePostResponse>() {

			@Override
			public void onSuccess (CreatePostResponse output) {
				if (output.status == StatusType.StatusTypeSuccess) {}

				DefaultEventBus.get().fireEventFromSource(
						new CreatePostSuccess(input, output),
						PostController.this);
			}

			@Override
			public void onFailure (Throwable caught) {
				DefaultEventBus.get().fireEventFromSource(
						new CreatePostFailure(input, caught),
						PostController.this);
			}
		});
	}

}
