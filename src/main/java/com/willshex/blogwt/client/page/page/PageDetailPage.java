//
//  PageDetailPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.page;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PageDetailPage extends Page {

	private static PageDetailPageUiBinder uiBinder = GWT
			.create(PageDetailPageUiBinder.class);

	interface PageDetailPageUiBinder extends UiBinder<Widget, PageDetailPage> {}

	public PageDetailPage () {
		super(PageType.PageDetailPageType);
		initWidget(uiBinder.createAndBindUi(this));
	}

}
