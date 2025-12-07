//
//  NotFoundPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 9 Sep 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog.notfound;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.PageController;
import com.willshex.blogwt.client.page.Page;

/**
 * @author William Shakour (billy1380)
 *
 */
public class NotFoundPage extends Page {

	private static NotFoundPageUiBinder uiBinder = GWT
			.create(NotFoundPageUiBinder.class);

	interface NotFoundPageUiBinder extends UiBinder<Widget, NotFoundPage> {}

	@UiField AnchorElement elHome;

	public NotFoundPage () {
		initWidget(uiBinder.createAndBindUi(this));

		elHome.setHref(PageController.get().homePageUri());
	}

}
