//
//  TagPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Jul 2015.
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
public class TagPage extends Page {

	private static TagPageUiBinder uiBinder = GWT.create(TagPageUiBinder.class);

	interface TagPageUiBinder extends UiBinder<Widget, TagPage> {}

	public TagPage () {
		super(PageType.TagPagePageType);
		initWidget(uiBinder.createAndBindUi(this));
	}

}
