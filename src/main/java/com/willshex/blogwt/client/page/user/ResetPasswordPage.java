//
//  ResetPasswordPage.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
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
public class ResetPasswordPage extends Page {

	private static ResetPasswordPageUiBinder uiBinder = GWT
			.create(ResetPasswordPageUiBinder.class);

	interface ResetPasswordPageUiBinder extends
			UiBinder<Widget, ResetPasswordPage> {}

	public ResetPasswordPage () {
		super(PageType.ResetPasswordPageType);
		initWidget(uiBinder.createAndBindUi(this));
	}

}
