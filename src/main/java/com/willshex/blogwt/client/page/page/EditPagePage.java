//
//  EditPagePage.java
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
public class EditPagePage extends Page {

	private static EditPagePageUiBinder uiBinder = GWT
			.create(EditPagePageUiBinder.class);

	interface EditPagePageUiBinder extends UiBinder<Widget, EditPagePage> {}

	public EditPagePage () {
		super(PageType.EditPagePageType);
		initWidget(uiBinder.createAndBindUi(this));
	}

}
