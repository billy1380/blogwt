//
//  SearchPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SearchPart extends Composite {

	private static SearchPartUiBinder uiBinder = GWT
			.create(SearchPartUiBinder.class);

	interface SearchPartUiBinder extends UiBinder<Widget, SearchPart> {}

	@UiField TextBox txtQuery;
	@UiField FormPanel frmSearch;

	public SearchPart () {
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtQuery, "Search...");
	}

	@UiHandler("frmSearch")
	void onSearchSubmit (SubmitEvent event) {
		if (isValid()) {
			PageTypeHelper.show(PageType.SearchPostsPageType,
					txtQuery.getValue());
			txtQuery.setValue("");
		} else {
			showErrors();
		}

		event.cancel();
	}

	private boolean isValid () {
		// do client validation
		return true;
	}

	private void showErrors () {

	}

}
