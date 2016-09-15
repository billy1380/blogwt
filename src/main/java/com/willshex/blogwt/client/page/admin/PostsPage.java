//
//  PostsPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 Jan 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.blog.event.DeletePostEventHandler;
import com.willshex.blogwt.client.cell.PrettyButtonCell;
import com.willshex.blogwt.client.controller.PostController;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.admin.PagesPage.PagesPageTemplates;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.shared.api.blog.call.DeletePostRequest;
import com.willshex.blogwt.shared.api.blog.call.DeletePostResponse;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.helper.DateTimeHelper;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostsPage extends Page implements DeletePostEventHandler {

	private static PostsPageUiBinder uiBinder = GWT
			.create(PostsPageUiBinder.class);

	interface PostsPageUiBinder extends UiBinder<Widget, PostsPage> {}

	@UiField(provided = true) CellTable<Post> tblPosts = new CellTable<Post>(
			PagerHelper.DEFAULT_COUNT.intValue(),
			BootstrapGwtCellTable.INSTANCE);
	@UiField SimplePager pgrPosts;
	@UiField NoneFoundPanel pnlNoPosts;
	@UiField Button btnRefresh;
	private SafeHtmlCell safeHtmlPrototype = new SafeHtmlCell();
	private ButtonCell actionButtonPrototype = new PrettyButtonCell();

	public PostsPage () {
		initWidget(uiBinder.createAndBindUi(this));

		createColumns();

		tblPosts.setEmptyTableWidget(pnlNoPosts);
	}

	private void createColumns () {
		Column<Post, SafeHtml> title = new Column<Post, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (Post object) {
				return PagesPageTemplates.INSTANCE.title(
						PageTypeHelper.asHref(PageType.PostDetailPageType,
								object.slug),
						SafeHtmlUtils.fromTrustedString(
								PostHelper.makeMarkup(object.title)));
			}
		};

		TextColumn<Post> author = new TextColumn<Post>() {

			@Override
			public String getValue (Post object) {
				return UserHelper.name(object.author);
			}
		};

		TextColumn<Post> created = new TextColumn<Post>() {

			@Override
			public String getValue (Post object) {
				return DateTimeHelper.ago(object.created);
			}
		};

		Column<Post, SafeHtml> published = new Column<Post, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (Post object) {
				return object.published == null
						? PagesPageTemplates.INSTANCE.no()
						: SafeHtmlUtils.fromString(
								DateTimeHelper.ago(object.published));
			}
		};

		Column<Post, SafeHtml> listed = new Column<Post, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (Post object) {
				return Boolean.TRUE.equals(object.listed)
						? PagesPageTemplates.INSTANCE.yes()
						: PagesPageTemplates.INSTANCE.no();
			}
		};

		Column<Post, SafeHtml> commentsEnabled = new Column<Post, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (Post object) {
				return Boolean.TRUE.equals(object.commentsEnabled)
						? PagesPageTemplates.INSTANCE.yes()
						: PagesPageTemplates.INSTANCE.no();
			}
		};

		Column<Post, SafeHtml> edit = new Column<Post, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (Post object) {
				return PagesPageTemplates.INSTANCE.edit(PageTypeHelper
						.asHref(PageType.EditPostPageType, object.slug));
			}
		};

		Column<Post, String> delete = new Column<Post, String>(
				actionButtonPrototype) {

			@Override
			public String getValue (Post object) {
				return "delete";
			}
		};

		delete.setFieldUpdater(new FieldUpdater<Post, String>() {

			@Override
			public void update (int index, Post object, String value) {
				if (Window.confirm("Are you sure you want to delete post: "
						+ object.title + "?")) {
					PostController.get().deletePost(object);
				}
			}
		});

		tblPosts.addColumn(title, "Title");
		tblPosts.addColumn(author, "Author");
		tblPosts.addColumn(created, "Created");
		tblPosts.addColumn(published, "Published");
		tblPosts.addColumn(listed, "Listed");
		tblPosts.addColumn(commentsEnabled, "Comments");
		tblPosts.addColumn(edit);
		tblPosts.addColumn(delete);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				DeletePostEventHandler.TYPE, PostController.get(), this));

		PostController.get().setAdminMode(true);
		PostController.get().addDataDisplay(tblPosts);
		pgrPosts.setDisplay(tblPosts);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onDetach() */
	@Override
	protected void onDetach () {
		super.onDetach();

		PostController.get().setAdminMode(false);
		PostController.get().removeDataDisplay(tblPosts);
		pgrPosts.setDisplay(null);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.DeletePostEventHandler#
	 * deletePostSuccess(com.willshex.blogwt.shared.api.blog.call.
	 * DeletePostRequest,
	 * com.willshex.blogwt.shared.api.blog.call.DeletePostResponse) */
	@Override
	public void deletePostSuccess (DeletePostRequest input,
			DeletePostResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			tblPosts.setVisibleRangeAndClearData(tblPosts.getVisibleRange(),
					true);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.DeletePostEventHandler#
	 * deletePostFailure(com.willshex.blogwt.shared.api.blog.call.
	 * DeletePostRequest, java.lang.Throwable) */
	@Override
	public void deletePostFailure (DeletePostRequest input, Throwable caught) {}

	@UiHandler("btnRefresh")
	void onRefreshClicked (ClickEvent ce) {
		tblPosts.setVisibleRangeAndClearData(tblPosts.getVisibleRange(), true);
	}

}
