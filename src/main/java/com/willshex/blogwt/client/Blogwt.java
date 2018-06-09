//
//  Blogwt.java
//  blogwt
//
//  Created by billy1380 on 31 Jul 2013.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.gwt.RunAsync;
import com.willshex.blogwt.client.markdown.Processor;
import com.willshex.blogwt.client.part.CookieNoticePart;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Blogwt extends ErrorHandlingEntryPoint implements EntryPoint {

	private HTMLPanel content;

	public void onModuleLoad () {
		super.onModuleLoad();

		RunAsync.run(Blogwt.this::start);
	}

	private void start () {
		History.addValueChangeHandler(NavigationController.get());
		createContentAndPages();
		Processor.init(e -> History.fireCurrentHistoryState());
	}

	private void createContentAndPages () {
		content = new HTMLPanel(SafeHtmlUtils.EMPTY_SAFE_HTML);
		RootPanel.get().add(content);

		content.add(NavigationController.get()
				.setPageHolder(new HTMLPanel(SafeHtmlUtils.EMPTY_SAFE_HTML)));
		
		content.add(new CookieNoticePart());
	}
}
