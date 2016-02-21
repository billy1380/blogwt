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
import com.willshex.blogwt.client.markdown.Processor;
import com.willshex.blogwt.client.part.CookieNoticePart;
import com.willshex.blogwt.client.part.FooterPart;
import com.willshex.blogwt.client.part.HeaderPart;

import emoji.gwt.emoji.Emoji;
import emoji.gwt.emoji.Emoji.Ready;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Blogwt extends ErrorHandlingEntryPoint implements EntryPoint {

	private HTMLPanel content;

	public void onModuleLoad () {
		super.onModuleLoad();

		Processor.init(new Ready() {
			@Override
			public void ready (Emoji emoji) {
				start();
			}
		});
	}

	private void start () {
		History.addValueChangeHandler(NavigationController.get());
		createContentAndPages();
		History.fireCurrentHistoryState();
	}

	private void createContentAndPages () {
		content = new HTMLPanel("<!-- content -->");
		RootPanel.get().add(content);

		content.add(new CookieNoticePart());
		content.add(new HeaderPart());
		content.add(NavigationController.get()
				.setPageHolder(new HTMLPanel("<!-- pages -->")));
		content.add(new FooterPart());
	}
}
