//
//  SearchPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SearchPart extends Composite {

	private static SearchPartUiBinder uiBinder = GWT
			.create(SearchPartUiBinder.class);

	interface SearchPartUiBinder extends UiBinder<Widget, SearchPart> {}

	@UiField TextBox txtSearch;

	public SearchPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("btnSubmit")
	void onSubmitClicked (ClickEvent event) {
		if (isValid()) {
			PageType.SearchPostsPageType.show(txtSearch.getValue());
			txtSearch.setValue("");
		} else {
			showErrors();
		}
	}

	private boolean isValid () {
		// do client validation
		return true;
	}

	private void showErrors () {

	}

}
