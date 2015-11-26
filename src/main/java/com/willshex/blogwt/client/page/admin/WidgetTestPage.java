//
//  WidgetTestPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 26 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.Page;

/**
 * @author William Shakour (billy1380)
 *
 */
public class WidgetTestPage extends Page {

	private static WidgetTestPageUiBinder uiBinder = GWT
			.create(WidgetTestPageUiBinder.class);

	interface WidgetTestPageUiBinder extends UiBinder<Widget, WidgetTestPage> {}

	public WidgetTestPage () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
