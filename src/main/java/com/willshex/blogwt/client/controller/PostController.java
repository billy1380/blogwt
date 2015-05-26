//
//  PostController.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 15 May 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.Collections;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.blog.BlogService;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.blog.call.CreatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.CreatePostResponse;
import com.willshex.blogwt.shared.api.blog.call.DeletePostRequest;
import com.willshex.blogwt.shared.api.blog.call.DeletePostResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostsResponse;
import com.willshex.blogwt.shared.api.blog.call.event.CreatePostEventHandler.CreatePostFailure;
import com.willshex.blogwt.shared.api.blog.call.event.CreatePostEventHandler.CreatePostSuccess;
import com.willshex.blogwt.shared.api.blog.call.event.DeletePostEventHandler.DeletePostFailure;
import com.willshex.blogwt.shared.api.blog.call.event.DeletePostEventHandler.DeletePostSuccess;
import com.willshex.blogwt.shared.api.blog.call.event.GetPostEventHandler.GetPostFailure;
import com.willshex.blogwt.shared.api.blog.call.event.GetPostEventHandler.GetPostSuccess;
import com.willshex.blogwt.shared.api.blog.call.event.GetPostsEventHandler.GetPostsFailure;
import com.willshex.blogwt.shared.api.blog.call.event.GetPostsEventHandler.GetPostsSuccess;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostContent;
import com.willshex.blogwt.shared.api.datatype.PostSortType;
import com.willshex.blogwt.shared.api.helper.PagerHelper;
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

	private Pager pager = PagerHelper.createDefaultPager().sortBy(
			PostSortType.PostSortTypeCreated.toString());

	private void fetchPosts () {
		final GetPostsRequest input = ApiHelper
				.setAccessCode(new GetPostsRequest());
		input.pager = pager;
		input.session = SessionController.get().sessionForApiCall();
		input.summaryOnly = Boolean.TRUE;

		ApiHelper.createBlogClient().getPosts(input,
				new AsyncCallback<GetPostsResponse>() {

					@Override
					public void onSuccess (GetPostsResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {
							if (output.posts != null && output.posts.size() > 0) {
								pager = output.pager;
								updateRowCount(
										input.pager.count == null ? 0
												: input.pager.count.intValue(),
										input.pager.count == null
												|| input.pager.count.intValue() == 0);
								updateRowData(input.pager.start.intValue(),
										output.posts);
							} else {
								updateRowCount(input.pager.start.intValue(),
										true);
								updateRowData(input.pager.start.intValue(),
										Collections.<Post> emptyList());
							}
						}

						DefaultEventBus.get().fireEventFromSource(
								new GetPostsSuccess(input, output),
								PostController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new GetPostsFailure(input, caught),
								PostController.this);
					}

				});
	}

	public void deletePost (final String slug) {
		BlogService service = ApiHelper.createBlogClient();

		final DeletePostRequest input = SessionController.get()
				.setSession(ApiHelper.setAccessCode(new DeletePostRequest()))
				.post(new Post().slug(slug));

		if (input.post != null) {
			service.deletePost(input, new AsyncCallback<DeletePostResponse>() {

				@Override
				public void onSuccess (DeletePostResponse output) {
					if (output.status == StatusType.StatusTypeSuccess) {
						if (input.post != null) {
							fetchPosts();
						}
					}

					DefaultEventBus.get().fireEventFromSource(
							new DeletePostSuccess(input, output),
							PostController.this);
				}

				@Override
				public void onFailure (Throwable caught) {
					DefaultEventBus.get().fireEventFromSource(
							new DeletePostFailure(input, caught),
							PostController.this);
				}
			});
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData) */
	@Override
	protected void onRangeChanged (HasData<Post> display) {
		Range range = display.getVisibleRange();
		pager.start(Integer.valueOf(range.getStart())).count(
				Integer.valueOf(range.getLength()));

		fetchPosts();
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

	/**
	 * @param reference
	 * @return
	 */
	public void getPost (String reference) {
		BlogService service = ApiHelper.createBlogClient();

		final GetPostRequest input = SessionController.get()
				.setSession(ApiHelper.setAccessCode(new GetPostRequest()))
				.post(setPostReference(new Post(), reference));

		service.getPost(input, new AsyncCallback<GetPostResponse>() {

			@Override
			public void onSuccess (GetPostResponse output) {
				if (output.status == StatusType.StatusTypeSuccess) {
					if (output.post != null) {}
				}

				DefaultEventBus.get().fireEventFromSource(
						new GetPostSuccess(input, output), PostController.this);
			}

			@Override
			public void onFailure (Throwable caught) {
				DefaultEventBus.get().fireEventFromSource(
						new GetPostFailure(input, caught), PostController.this);
			}
		});
	}

	/**
	 * 
	 * @param post
	 * @param reference
	 * @return
	 */
	private Post setPostReference (Post post, String reference) {
		Long id = null;
		try {
			// check if the slug is actually an id
			id = Long.parseLong(reference);
		} catch (NumberFormatException e) {}

		if (id == null) {
			post.slug = reference;
		} else {
			post.id = id;
		}

		return post;
	}

	/**
	 * @return
	 */
	public String disqusId () {
		return "";
	}

	/**
	 * @return
	 */
	public String categoryId () {
		return "";
	}

}
