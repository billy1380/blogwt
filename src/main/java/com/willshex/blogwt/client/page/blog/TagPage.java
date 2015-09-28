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
import com.willshex.blogwt.client.controller.PostController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.BootstrapGwtCellList;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author William Shakour (billy1380)
 *
 */
public class TagPage extends Page implements NavigationChangedEventHandler {

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

	public TagPage () {
		initWidget(uiBinder.createAndBindUi(this));

		pnlNoPosts.removeFromParent();
		clPosts.setEmptyListWidget(pnlNoPosts);

		HTMLPanel loadingWidget = new HTMLPanel(SafeHtmlUtils.EMPTY_SAFE_HTML);
		loadingWidget.addStyleName("text-center");
		loadingWidget
				.add(new Image(Resources.CELL_TABLE_RES.cellTableLoading()));

		clPosts.setLoadingIndicator(loadingWidget);

		PostController.get().addDataDisplay(clPosts);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		register(PostHelper.handlePluginContentReady());

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));

		super.onAttach();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.event.NavigationChangedEventHandler#
	 * navigationChanged
	 * (com.willshex.blogwt.client.controller.NavigationController.Stack,
	 * com.willshex.blogwt.client.controller.NavigationController.Stack) */
	@Override
	public void navigationChanged (Stack previous, Stack current) {
		String tag;
		if ((tag = current.getAction()) != null) {
			elHeading.setInnerSafeHtml(TagPageTemplates.INSTANCE.heading(tag));

			PostController.get().setTag(tag);

			refresh();
		}
	}

	private void refresh () {
		clPosts.setVisibleRangeAndClearData(clPosts.getVisibleRange(), true);
	}

}
