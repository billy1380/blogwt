//
//  ChangePasswordPage.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ChangePasswordPage extends Page {

	private static ChangePasswordPageUiBinder uiBinder = GWT
			.create(ChangePasswordPageUiBinder.class);

	interface ChangePasswordPageUiBinder extends
			UiBinder<Widget, ChangePasswordPage> {}

	public ChangePasswordPage () {
		super(PageType.ChangePasswordPageType);
		initWidget(uiBinder.createAndBindUi(this));
	}

}
