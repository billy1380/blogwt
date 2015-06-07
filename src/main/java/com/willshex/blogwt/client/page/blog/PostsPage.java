//
//  PostsPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.cell.blog.PostSummaryCell;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.controller.PostController;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.part.BootstrapGwtCellList;
import com.willshex.blogwt.shared.api.datatype.Post;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostsPage extends Page implements NavigationChangedEventHandler {

	private static PostsPageUiBinder uiBinder = GWT
			.create(PostsPageUiBinder.class);

	interface PostsPageUiBinder extends UiBinder<Widget, PostsPage> {}

	@UiField Element elTitle;
	@UiField Element elExtendedTitle;
	@UiField HTMLPanel pnlNoPosts;
	@UiField(provided = true) CellList<Post> clPosts = new CellList<Post>(
			new PostSummaryCell(), BootstrapGwtCellList.INSTANCE);

	public PostsPage () {
		super(PageType.PostsPageType);
		initWidget(uiBinder.createAndBindUi(this));

		elTitle.setInnerText(PropertyController.get().title());
		elExtendedTitle.setInnerText(PropertyController.get().extendedTitle());

		pnlNoPosts.removeFromParent();
		clPosts.setEmptyListWidget(pnlNoPosts);

		HTMLPanel loadingWidget = new HTMLPanel(SafeHtmlUtils.EMPTY_SAFE_HTML);
		loadingWidget.addStyleName("text-center");
		loadingWidget
				.add(new Image(Resources.CELL_TABLE_RES.cellTableLoading()));

		clPosts.setLoadingIndicator(loadingWidget);

		PostController.get().addDataDisplay(clPosts);
		// refresh();
	}

	@Override
	protected void onAttach () {
		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));
		super.onAttach();
	}

	@Override
	public void navigationChanged (Stack previous, Stack current) {
		if (PageType.LogoutPageType.equals(current.getPage())) {
			SessionController.get().logout();
		}
		
		refresh();
	}

	private void refresh () {
		clPosts.setVisibleRangeAndClearData(clPosts.getVisibleRange(), true);
	}

}
