//
//  UserSummaryCell.java
//  blogwt
//
//  Created by billy1380 on 5 Jan 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.cell.user;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiRenderer;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.UserHelper;

/**
 * @author billy1380
 *
 */
public class UserSummaryCell extends AbstractCell<User> {

	interface UserSummaryCellRenderer extends UiRenderer {
		void render (SafeHtmlBuilder sb, SafeHtml name);
	}

	private static UserSummaryCellRenderer RENDERER = GWT
			.create(UserSummaryCellRenderer.class);

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client
	 * .Cell.Context, java.lang.Object,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder) */
	@Override
	public void render (com.google.gwt.cell.client.Cell.Context context,
			User user, SafeHtmlBuilder sb) {

		RENDERER.render(sb, SafeHtmlUtils.fromString(UserHelper.name(user)));

	}
}
