//
//  VerifyAccountPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 6 Sep 2015.
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
public class VerifyAccountPage extends Page {

	private static VerifyAccountPageUiBinder uiBinder = GWT
			.create(VerifyAccountPageUiBinder.class);

	interface VerifyAccountPageUiBinder extends
			UiBinder<Widget, VerifyAccountPage> {}

	
	public VerifyAccountPage () {
		super(PageType.VerifyAccountPageType);
		initWidget(uiBinder.createAndBindUi(this));
	}

}
