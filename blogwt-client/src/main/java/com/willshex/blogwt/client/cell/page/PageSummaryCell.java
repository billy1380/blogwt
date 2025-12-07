//
//  PageSummaryCell.java
//  blogwt
//
//  Created by billy1380 on 5 Jan 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.cell.page;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiRenderer;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.shared.api.datatype.Page;

/**
 * @author billy1380
 *
 */
public class PageSummaryCell extends AbstractCell<Page> {

	interface PageSummaryCellRenderer extends UiRenderer {
		void render (SafeHtmlBuilder sb, SafeUri link, SafeHtml title);
	}

	private static PageSummaryCellRenderer RENDERER = GWT
			.create(PageSummaryCellRenderer.class);
	@Override
	public void render (com.google.gwt.cell.client.Cell.Context context,
			Page page, SafeHtmlBuilder sb) {
		RENDERER.render(sb, PageTypeHelper.slugToHref(page.slug), SafeHtmlUtils
				.fromTrustedString(PostHelper.makeHeading(page.title)));
	}

}
