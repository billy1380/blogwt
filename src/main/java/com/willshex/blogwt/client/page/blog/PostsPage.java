//
//  PostsPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.cell.blog.PostSummaryCell;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.PageController;
import com.willshex.blogwt.client.controller.PostController;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.BootstrapGwtCellList;
import com.willshex.blogwt.client.part.LoadingPanel;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostsPage extends Page {

	private static PostsPageUiBinder uiBinder = GWT
			.create(PostsPageUiBinder.class);

	interface PostsPageUiBinder extends UiBinder<Widget, PostsPage> {}

	@UiField Element elTitle;
	@UiField Element elExtendedTitle;
	@UiField Element elJumbotron;
	@UiField Image imgLargeBrand;

	@UiField NoneFoundPanel pnlNoPosts;
	@UiField LoadingPanel pnlLoading;

	@UiField InlineHyperlink lnkNewPost;
	@UiField(provided = true) CellList<Post> clPosts = new CellList<Post>(
			new PostSummaryCell(), BootstrapGwtCellList.INSTANCE);

	public PostsPage () {
		initWidget(uiBinder.createAndBindUi(this));

		elTitle.setInnerText(PropertyController.get().title());
		elExtendedTitle.setInnerText(PropertyController.get().extendedTitle());
		imgLargeBrand.setAltText(PropertyController.get().title());

		String largeLogo = PropertyController.get()
				.stringProperty(PropertyHelper.LARGE_LOGO_URL);
		if (largeLogo != null && !PropertyHelper.NONE_VALUE.equals(largeLogo)) {
			imgLargeBrand.getElement().removeAttribute("width");
			imgLargeBrand.getElement().removeAttribute("height");
			imgLargeBrand.addStyleName("img-responsive");
			imgLargeBrand.addStyleName("center-block");
			imgLargeBrand.setUrl(largeLogo);
		} else {
			imgLargeBrand.setResource(Resources.RES.largeBrand());
		}

		pnlNoPosts.removeFromParent();
		clPosts.setEmptyListWidget(pnlNoPosts);

		clPosts.setPageSize(PagerHelper.DEFAULT_COUNT);

		pnlLoading.removeFromParent();
		clPosts.setLoadingIndicator(pnlLoading);

		PostController.get().addDataDisplay(clPosts);

		if (PageController.get().homePage() != null) {
			elJumbotron.removeFromParent();
		}
	}

	@Override
	protected void onAttach () {
		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				(p, c) -> {
					if (PageType.LogoutPageType.equals(c.getPage())) {
						SessionController.get().logout();
					} else {
						refresh();
					}

					lnkNewPost.setVisible(SessionController.get()
							.isAuthorised(Arrays.asList(PermissionHelper
									.create(PermissionHelper.MANAGE_POSTS))));
				}));

		register(PostHelper.handlePluginContentReady());

		super.onAttach();
	}

	private void refresh () {
		PostController.get().clearTag();
		clPosts.setVisibleRangeAndClearData(clPosts.getVisibleRange(), true);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#getTitle() */
	@Override
	public String getTitle () {
		return UiHelper.pageTitle("Blog");
	}

}
