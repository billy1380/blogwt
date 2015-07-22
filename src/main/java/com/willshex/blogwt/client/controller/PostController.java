//
//  PostController.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 15 May 2015.
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
import com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostResponse;
import com.willshex.blogwt.shared.api.blog.call.event.CreatePostEventHandler.CreatePostFailure;
import com.willshex.blogwt.shared.api.blog.call.event.CreatePostEventHandler.CreatePostSuccess;
import com.willshex.blogwt.shared.api.blog.call.event.DeletePostEventHandler.DeletePostFailure;
import com.willshex.blogwt.shared.api.blog.call.event.DeletePostEventHandler.DeletePostSuccess;
import com.willshex.blogwt.shared.api.blog.call.event.GetPostEventHandler.GetPostFailure;
import com.willshex.blogwt.shared.api.blog.call.event.GetPostEventHandler.GetPostSuccess;
import com.willshex.blogwt.shared.api.blog.call.event.GetPostsEventHandler.GetPostsFailure;
import com.willshex.blogwt.shared.api.blog.call.event.GetPostsEventHandler.GetPostsSuccess;
import com.willshex.blogwt.shared.api.blog.call.event.UpdatePostEventHandler.UpdatePostFailure;
import com.willshex.blogwt.shared.api.blog.call.event.UpdatePostEventHandler.UpdatePostSuccess;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostContent;
import com.willshex.blogwt.shared.api.datatype.PostSortType;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.helper.TagHelper;
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

	private String tag;

	private Pager pager = PagerHelper.createDefaultPager().sortBy(
			PostSortType.PostSortTypeCreated.toString());
	private Request getPostsRequest;
	private Request getPostRequest;

	private void fetchPosts () {
		final GetPostsRequest input = ApiHelper
				.setAccessCode(new GetPostsRequest());
		input.pager = pager;
		input.session = SessionController.get().sessionForApiCall();
		input.includePostContents = Boolean.FALSE;
		input.tag = tag;

		if (getPostsRequest != null) {
			getPostsRequest.cancel();
		}

		getPostsRequest = ApiHelper.createBlogClient().getPosts(input,
				new AsyncCallback<GetPostsResponse>() {

					@Override
					public void onSuccess (GetPostsResponse output) {
						getPostsRequest = null;

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
						getPostsRequest = null;

						DefaultEventBus.get().fireEventFromSource(
								new GetPostsFailure(input, caught),
								PostController.this);
					}

				});
	}

	public void updatePost (Post post, String title, Boolean listed,
			Boolean commentsEnabled, String summary, String content,
			Boolean publish, String tags) {
		final UpdatePostRequest input = SessionController
				.get()
				.setSession(ApiHelper.setAccessCode(new UpdatePostRequest()))
				.post(post.title(title).summary(summary).listed(listed)
						.commentsEnabled(commentsEnabled)
						.tags(TagHelper.convertToTagList(tags)))
				.publish(publish);
		input.post.content.body = content;

		ApiHelper.createBlogClient().updatePost(input,
				new AsyncCallback<UpdatePostResponse>() {

					@Override
					public void onSuccess (UpdatePostResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {
							fetchPosts();
						}

						DefaultEventBus.get().fireEventFromSource(
								new UpdatePostSuccess(input, output),
								PostController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new UpdatePostFailure(input, caught),
								PostController.this);
					}
				});
	}

	public void deletePost (Post post) {

		final DeletePostRequest input = SessionController.get()
				.setSession(ApiHelper.setAccessCode(new DeletePostRequest()))
				.post(post);

		ApiHelper.createBlogClient().deletePost(input,
				new AsyncCallback<DeletePostResponse>() {

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

	public void createPost (String title, Boolean listed,
			Boolean commentsEnabled, String summary, String content,
			Boolean publish, String tags) {
		BlogService blogService = ApiHelper.createBlogClient();

		final CreatePostRequest input = SessionController
				.get()
				.setSession(ApiHelper.setAccessCode(new CreatePostRequest()))
				.post(new Post().title(title).summary(summary)
						.content(new PostContent().body(content))
						.tags(TagHelper.convertToTagList(tags)).listed(listed)
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
		final GetPostRequest input = SessionController.get()
				.setSession(ApiHelper.setAccessCode(new GetPostRequest()))
				.post(setPostReference(new Post(), reference));

		if (getPostRequest != null) {
			getPostRequest.cancel();
		}

		getPostRequest = ApiHelper.createBlogClient().getPost(input,
				new AsyncCallback<GetPostResponse>() {

					@Override
					public void onSuccess (GetPostResponse output) {
						getPostRequest = null;

						if (output.status == StatusType.StatusTypeSuccess) {
							if (output.post != null) {}
						}

						DefaultEventBus.get().fireEventFromSource(
								new GetPostSuccess(input, output),
								PostController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						getPostRequest = null;

						DefaultEventBus.get().fireEventFromSource(
								new GetPostFailure(input, caught),
								PostController.this);
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
		return PropertyController.get().stringProperty(
				PropertyHelper.POST_DISQUS_ID);
	};

	/**
	 * @return
	 */
	public String categoryId () {
		return PropertyController.get().stringProperty(
				PropertyHelper.POST_CATEGORY_ID);
	};

	/**
	 * @param tag
	 */
	public void setTag (String tag) {
		if (tag != this.tag) {
			this.tag = tag;

			PagerHelper.reset(pager);
		}
	}

	public void clearTag () {
		tag = null;

		PagerHelper.reset(pager);
	}

}
