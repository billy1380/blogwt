//
//  Blogwt.java
//  blogwt
//
//  Created by billy1380 on 31 Jul 2013.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.willshex.blogwt.client.controller.NavigationController;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Blogwt extends ErrorHandlingEntryPoint implements EntryPoint {

	private HTMLPanel content;

	public void onModuleLoad() {
		super.onModuleLoad();

		History.addValueChangeHandler(NavigationController.get());

		createContent();

		content.add(NavigationController.get().getHeader());
		content.add(NavigationController.get().getPageHolder());
		content.add(NavigationController.get().getFooter());

		History.fireCurrentHistoryState();
	}

	private void createContent() {
		content = new HTMLPanel("<!-- content -->");
		content.getElement().setId("content");
		content.getElement().setClassName("container");
		RootPanel.get().add(content);
	}
}
