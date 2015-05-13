//
//  EditPostPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class EditPostPage extends Page {

	private static EditPostPageUiBinder uiBinder = GWT
			.create(EditPostPageUiBinder.class);

	interface EditPostPageUiBinder extends UiBinder<Widget, EditPostPage> {}

	public EditPostPage () {
		super(PageType.EditPostPageType);
		initWidget(uiBinder.createAndBindUi(this));
	}

}
