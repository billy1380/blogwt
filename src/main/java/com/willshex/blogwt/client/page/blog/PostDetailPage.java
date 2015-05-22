//
//  PostDetailPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.controller.PostController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostResponse;
import com.willshex.blogwt.shared.api.blog.call.event.GetPostEventHandler;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostDetailPage extends Page implements
		NavigationChangedEventHandler, GetPostEventHandler {

	private static PostDetailPageUiBinder uiBinder = GWT
			.create(PostDetailPageUiBinder.class);

	interface PostDetailPageUiBinder extends UiBinder<Widget, PostDetailPage> {}

	private Post post;

	@UiField Element elTitle;
	@UiField Element elDate;
	@UiField Element elAuthor;

	@UiField HTMLPanel pnlTags;
	@UiField HTMLPanel pnlLoading;

	Element elComments;

	@UiField HTMLPanel pnlContent;

	public PostDetailPage () {
		super(PageType.PostDetailPageType);
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));
		register(DefaultEventBus.get().addHandlerToSource(
				GetPostEventHandler.TYPE, PostController.get(), this));

		elComments = Document.get().getElementById("disqus_thread");

		super.onAttach();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.blog.call.event.GetPostEventHandler#
	 * getPostSuccess(com.willshex.blogwt.shared.api.blog.call.GetPostRequest,
	 * com.willshex.blogwt.shared.api.blog.call.GetPostResponse) */
	@Override
	public void getPostSuccess (GetPostRequest input, GetPostResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {

		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.blog.call.event.GetPostEventHandler#
	 * getPostFailure(com.willshex.blogwt.shared.api.blog.call.GetPostRequest,
	 * java.lang.Throwable) */
	@Override
	public void getPostFailure (GetPostRequest input, Throwable caught) {}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.event.NavigationChangedEventHandler#
	 * navigationChanged
	 * (com.willshex.blogwt.client.controller.NavigationController.Stack,
	 * com.willshex.blogwt.client.controller.NavigationController.Stack) */
	@Override
	public void navigationChanged (Stack previous, Stack current) {
		post = null;
		pnlLoading.setVisible(true);
	}
}
