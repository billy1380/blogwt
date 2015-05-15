//
//  Page.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.willshex.blogwt.client.controller.PropertyController;

/**
 * @author William Shakour (billy1380)
 *
 */
public abstract class Page extends Composite {
	private List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();
	private PageType pageType = null;

	/**
	 * 
	 */
	public Page (PageType pageType) {
		this.pageType = pageType;
	}
	
	protected void register (HandlerRegistration registration) {
		handlers.add(registration);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		Document.get().setTitle(getTitle());
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onDetach() */
	@Override
	protected void onDetach () {
		super.onDetach();

		for (HandlerRegistration handler : handlers) {
			handler.removeHandler();
		}
	}

	public String getTitle () {
		return PropertyController.get().title();
	}

	protected PageType getPageType () {
		return pageType;
	}
}
