//
//  MarkdownToolbar.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 17 Jun 2015.
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
public class MarkdownToolbar extends Composite {

	private static MarkdownToolbarUiBinder uiBinder = GWT
			.create(MarkdownToolbarUiBinder.class);

	interface MarkdownToolbarUiBinder extends UiBinder<Widget, MarkdownToolbar> {}

	
	public MarkdownToolbar () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
