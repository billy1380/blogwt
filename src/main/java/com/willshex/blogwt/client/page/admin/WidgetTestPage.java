//
//  WidgetTestPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 26 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.markdown.plugin.part.GalleryPart;
import com.willshex.blogwt.client.page.Page;

/**
 * @author William Shakour (billy1380)
 *
 */
public class WidgetTestPage extends Page {

	private static WidgetTestPageUiBinder uiBinder = GWT
			.create(WidgetTestPageUiBinder.class);

	interface WidgetTestPageUiBinder extends UiBinder<Widget, WidgetTestPage> {}

	@UiField GalleryPart gallery;

	public WidgetTestPage () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		Map<String, String> params = new HashMap<>();
		params.put("caption", "This is a test!");

		gallery.setParams(params);
		gallery.addImageWithLine("url=http://lorempixel.com/400/200/");

	}

}
