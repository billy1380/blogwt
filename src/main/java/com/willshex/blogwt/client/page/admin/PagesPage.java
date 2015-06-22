//
//  PagesPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PagesPage extends Page {

	private static PagesPageUiBinder uiBinder = GWT
			.create(PagesPageUiBinder.class);

	interface PagesPageUiBinder extends UiBinder<Widget, PagesPage> {}

	public PagesPage () {
		super(PageType.PagesPageType);
		initWidget(uiBinder.createAndBindUi(this));
	}

}
