//
//  SearchPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Aug 2015.
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
import com.willshex.blogwt.client.cell.blog.ResultSummaryCell;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.controller.SearchController;
import com.willshex.blogwt.client.controller.SearchController.SearchResult;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.BootstrapGwtCellList;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SearchPage extends Page implements NavigationChangedEventHandler {

	private static SearchPageUiBinder uiBinder = GWT
			.create(SearchPageUiBinder.class);

	interface SearchPageUiBinder extends UiBinder<Widget, SearchPage> {}

	interface SearchPageTemplates extends SafeHtmlTemplates {
		SearchPageTemplates INSTANCE = GWT.create(SearchPageTemplates.class);

		@Template("Matches for <strong>&apos;{0}&apos;</strong>")
		SafeHtml heading (String tag);
	}

	@UiField Element elHeading;
	@UiField(provided = true) CellList<SearchResult> clResults = new CellList<SearchResult>(
			new ResultSummaryCell(), BootstrapGwtCellList.INSTANCE);
	@UiField NoneFoundPanel pnlNoResults;

	public SearchPage () {
		super(PageType.SearchPostsPageType);

		initWidget(uiBinder.createAndBindUi(this));

		pnlNoResults.removeFromParent();
		clResults.setEmptyListWidget(pnlNoResults);

		HTMLPanel loadingWidget = new HTMLPanel(SafeHtmlUtils.EMPTY_SAFE_HTML);
		loadingWidget.addStyleName("text-center");
		loadingWidget
				.add(new Image(Resources.CELL_TABLE_RES.cellTableLoading()));

		clResults.setLoadingIndicator(loadingWidget);

		SearchController.get().addDataDisplay(clResults);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
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
		String query;
		if ((query = current.getAction()) == null) {
			query = "";
		}

		elHeading.setInnerSafeHtml(SearchPageTemplates.INSTANCE.heading(query));

		SearchController.get().setQuery(query);

		refresh();
	}

	private void refresh () {
		clResults
				.setVisibleRangeAndClearData(clResults.getVisibleRange(), true);
	}

}
