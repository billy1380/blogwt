//
//  CookieNoticePart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class CookieNoticePart extends Composite {

	private static CookieNoticePartUiBinder uiBinder = GWT
			.create(CookieNoticePartUiBinder.class);

	interface CookieNoticePartUiBinder extends
			UiBinder<Widget, CookieNoticePart> {}

	public CookieNoticePart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
