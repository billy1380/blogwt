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
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.utility.StringUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
public abstract class Page extends Composite {
	private List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

	protected void register (HandlerRegistration registration) {
		handlers.add(registration);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		refreshTitle();
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

		reset();
	}

	public String getTitle () {
		String name = this.getClass().getSimpleName();
		int i = name.lastIndexOf("Page");
		return UiHelper.pageTitle(StringUtils.expandByCase(
				i > 0 ? name.substring(0, i) : name, true, true, " ", ""));
	}

	protected void reset () {}

	protected void refreshTitle () {
		Document.get().setTitle(getTitle());
	}
}
