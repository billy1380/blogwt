//
//  PostsPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.cell.blog.PostSummaryCell;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.part.BootstrapGwtCellList;
import com.willshex.blogwt.shared.api.datatype.Post;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostsPage extends Page {

	private static PostsPageUiBinder uiBinder = GWT
			.create(PostsPageUiBinder.class);

	interface PostsPageUiBinder extends UiBinder<Widget, PostsPage> {}

	@UiField Element elTitle;
	@UiField Element elExtendedTitle;
	@UiField(provided = true) CellList<Post> posts = new CellList<Post>(
			new PostSummaryCell(), BootstrapGwtCellList.INSTANCE);

	public PostsPage () {
		super(PageType.PostsPageType);
		initWidget(uiBinder.createAndBindUi(this));

		elTitle.setInnerText(PropertyController.get().title());
		elExtendedTitle.setInnerText(PropertyController.get().extendedTitle());
	}

}
