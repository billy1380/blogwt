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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.cell.blog.PostSummaryCell;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.controller.PostController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.shared.api.blog.call.DeletePostRequest;
import com.willshex.blogwt.shared.api.blog.call.DeletePostResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostResponse;
import com.willshex.blogwt.shared.api.blog.call.event.DeletePostEventHandler;
import com.willshex.blogwt.shared.api.blog.call.event.GetPostEventHandler;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.helper.DateTimeHelper;
import com.willshex.blogwt.shared.api.helper.UserHelper;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostDetailPage extends Page implements
		NavigationChangedEventHandler, GetPostEventHandler,
		DeletePostEventHandler {

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
	boolean installed;
	@UiField InlineHyperlink lnkEditPost;
	@UiField Button btnDeletePost;

	public PostDetailPage () {
		super(PageType.PostDetailPageType);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("btnDeletePost")
	void onBtnDeletePost (ClickEvent event) {
		if (post != null
				&& Window.confirm("Are you sure you want to delete \""
						+ post.title + "\"")) {
			PostController.get().deletePost(post);
		}
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
		register(DefaultEventBus.get().addHandlerToSource(
				DeletePostEventHandler.TYPE, PostController.get(), this));

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
			show(post = output.post);
		} else {
			PageType.PostsPageType.show();
		}
	}

	private void show (Post post) {
		elTitle.setInnerHTML(PostHelper.makeHeading(post.title));
		elAuthor.setInnerText(UserHelper.handle(post.author));

		if (post.published != null) {
			elDate.setInnerSafeHtml(PostSummaryCell.DateTemplate.INSTANCE
					.publishedDate(DateTimeHelper.ago(post.published)));
		} else {
			elDate.setInnerSafeHtml(PostSummaryCell.DateTemplate.INSTANCE
					.notPublished(DateTimeHelper.ago(post.created)));
		}

		if (pnlTags.getWidgetCount() > 0) {
			pnlTags.clear();
		}

		lnkEditPost.setTargetHistoryToken(PageType.EditPostPageType
				.asTargetHistoryToken(post.slug));

		if (post.tags != null) {
			StringBuffer buffer = new StringBuffer();
			for (String tag : post.tags) {
				if (buffer.length() != 0) {
					buffer.append(" ");
				}

				buffer.append(tag);
			}
		}

		if (post.content != null) {
			String markup = PostHelper.makeMarkup(post.content.body);

			pnlContent.getElement().setInnerHTML(markup);

			if (post.commentsEnabled == Boolean.TRUE) {
				final String identifier = "post" + post.id.toString();
				final String url = GWT.getHostPageBaseURL()
						+ PageType.PostDetailPageType
								.asHref(NavigationController.VIEW_ACTION_PARAMETER_VALUE,
										post.slug).asString();
				final String title = post.title;
				final String tag = post.tags == null || post.tags.size() == 0 ? "None"
						: post.tags.get(0);

				(new Timer() {
					@Override
					public void run () {
						elComments = Document.get().getElementById(
								"disqus_thread");

						if (elComments != null) {
							if (!installed) {
								installDisqus(PostController.get().disqusId(),
										PostController.get().categoryId(),
										identifier, url, title, tag);
								installed = true;
							} else {
								resetDisqus(identifier, url, title, tag);
							}

							this.cancel();
						}
					}
				}).scheduleRepeating(100);
			}
		}

		pnlLoading.setVisible(false);
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
		if (current.getAction() != null) {
			String postParam;
			if ((postParam = current.getAction()) != null) {
				PostController.get().getPost(postParam);
				pnlLoading.setVisible(true);
			}
		}
	}

	private static native void installDisqus (String discusShortName,
			String categoryId, String postId, String url, String title,
			String category) /*-{
								$wnd.disqus_shortname = discusShortName;

								// $wnd.disqus_identifier = postId;
								$wnd.disqus_url = url;
								$wnd.disqus_title = title;
								$wnd.disqus_category_id = categoryId;

								($wnd.installDisqus = function() {
								var dsq = $wnd.document.createElement('script');
								dsq.type = 'text/javascript';
								dsq.async = true;
								dsq.src = '//' + $wnd.disqus_shortname + '.disqus.com/embed.js';
								($wnd.document.getElementsByTagName('head')[0] || $wnd.document
								.getElementsByTagName('body')[0]).appendChild(dsq);
								})();

								$wnd.reset = function(resetPostId, resetUrl, resetTitle, categoryId) {
								$wnd.DISQUS.reset({
								reload : true,
								config : function() {
								// this.page.identifier = resetPostId;
								this.page.url = resetUrl;
								this.page.title = resetTitle;
								this.page.category_id = categoryId;
								}
								});
								};
								}-*/;

	private static native void resetDisqus (String postId, String url,
			String title, String categoryId) /*-{
												$wnd.reset(postId, url, title, categoryId);
												}-*/;

	@Override
	public void deletePostSuccess (DeletePostRequest input,
			DeletePostResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			PageType.PostsPageType.show();
		} else {

		}
	}

	@Override
	public void deletePostFailure (DeletePostRequest input, Throwable caught) {}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#reset() */
	@Override
	protected void reset () {
		super.reset();

		elTitle.setInnerHTML("");
		elAuthor.setInnerHTML("");
		elDate.setInnerHTML("");
		pnlTags.clear();

		lnkEditPost.setTargetHistoryToken(PageType.EditPostPageType
				.asTargetHistoryToken(post.slug));

		pnlContent.getElement().setInnerHTML("");
		pnlLoading.setVisible(true);
	}
}
