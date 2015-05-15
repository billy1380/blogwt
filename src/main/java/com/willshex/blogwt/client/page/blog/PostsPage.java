//
//  PostsPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostsPage extends Page {

	private static PostsPageUiBinder uiBinder = GWT
			.create(PostsPageUiBinder.class);

	interface PostsPageUiBinder extends UiBinder<Widget, PostsPage> {}

	@UiField DivElement divTitle;
	@UiField DivElement divExtendedTitle;

	public PostsPage () {
		super(PageType.PostsPageType);
		initWidget(uiBinder.createAndBindUi(this));

		divTitle.setInnerText(PropertyController.get().title());
		divExtendedTitle.setInnerText(PropertyController.get().extendedTitle());
	}

}
