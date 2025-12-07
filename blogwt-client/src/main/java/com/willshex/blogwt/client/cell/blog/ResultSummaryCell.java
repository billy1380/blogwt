//
//  ResultSummaryCell.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.cell.blog;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.willshex.blogwt.client.cell.page.PageSummaryCell;
import com.willshex.blogwt.client.cell.user.UserSummaryCell;
import com.willshex.blogwt.client.controller.SearchController.SearchResult;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ResultSummaryCell extends AbstractCell<SearchResult> {

	private static final PostSummaryCell POST_CELL = new PostSummaryCell();
	private static final UserSummaryCell USER_CELL = new UserSummaryCell();
	private static final PageSummaryCell PAGE_CELL = new PageSummaryCell();
	@Override
	public void render (com.google.gwt.cell.client.Cell.Context context,
			SearchResult value, SafeHtmlBuilder sb) {

		if (value.isPost()) {
			POST_CELL.render(context, value.getPost(), sb);
		} else if (value.isPage()) {
			PAGE_CELL.render(context, value.getPage(), sb);
		} else if (value.isUser()) {
			USER_CELL.render(context, value.getUser(), sb);
		}

	}

}
