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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.cell.blog.ResultSummaryCell;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.SearchController;
import com.willshex.blogwt.client.controller.SearchController.SearchResult;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.BootstrapGwtCellList;
import com.willshex.blogwt.client.part.LoadingPanel;
import com.willshex.blogwt.client.part.NoneFoundPanel;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SearchPage extends Page {

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
	@UiField LoadingPanel pnlLoading;

	public SearchPage () {
		initWidget(uiBinder.createAndBindUi(this));

		pnlNoResults.removeFromParent();
		clResults.setEmptyListWidget(pnlNoResults);

		pnlLoading.removeFromParent();
		clResults.setLoadingIndicator(pnlLoading);

		SearchController.get().addDataDisplay(clResults);
	}
	@Override
	protected void onAttach () {
		register(PostHelper.handlePluginContentReady());

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				(p, c) -> {
					String query;
					if ((query = c.getAction()) == null) {
						query = "";
					}

					elHeading.setInnerSafeHtml(
							SearchPageTemplates.INSTANCE.heading(query));

					SearchController.get().setQuery(query);

					refresh();
				}));

		super.onAttach();
	}

	private void refresh () {
		clResults.setVisibleRangeAndClearData(clResults.getVisibleRange(),
				true);
	}

}
