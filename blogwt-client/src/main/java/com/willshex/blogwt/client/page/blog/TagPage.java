//
//  TagPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.cell.blog.PostSummaryCell;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.PostController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.BootstrapGwtCellList;
import com.willshex.blogwt.client.part.LoadingPanel;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.shared.api.datatype.Post;

/**
 * @author William Shakour (billy1380)
 *
 */
public class TagPage extends Page {

	private static TagPageUiBinder uiBinder = GWT.create(TagPageUiBinder.class);

	interface TagPageUiBinder extends UiBinder<Widget, TagPage> {}

	interface TagPageTemplates extends SafeHtmlTemplates {
		TagPageTemplates INSTANCE = GWT.create(TagPageTemplates.class);

		@Template("Posts tagged with <strong>&apos;{0}&apos;</strong>")
		SafeHtml heading (String tag);
	}

	@UiField Element elHeading;
	@UiField(provided = true) CellList<Post> clPosts = new CellList<Post>(
			new PostSummaryCell(), BootstrapGwtCellList.INSTANCE);
	@UiField NoneFoundPanel pnlNoPosts;
	@UiField LoadingPanel pnlLoading;

	public TagPage () {
		initWidget(uiBinder.createAndBindUi(this));

		pnlNoPosts.removeFromParent();
		clPosts.setEmptyListWidget(pnlNoPosts);

		pnlLoading.removeFromParent();
		clPosts.setLoadingIndicator(pnlLoading);

		PostController.get().addDataDisplay(clPosts);
	}
	@Override
	protected void onAttach () {
		register(PostHelper.handlePluginContentReady());

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				(p, c) -> {
					String tag;
					if ((tag = c.getAction()) != null) {
						elHeading.setInnerSafeHtml(
								TagPageTemplates.INSTANCE.heading(tag));

						PostController.get().setTag(tag);

						refresh();
					}
				}));

		super.onAttach();
	}

	private void refresh () {
		clPosts.setVisibleRangeAndClearData(clPosts.getVisibleRange(), true);
	}

}
