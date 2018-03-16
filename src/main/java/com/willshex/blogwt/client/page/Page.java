//
//  Page.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Composite;
import com.willshex.blogwt.client.gwt.RegisteringComposite;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.part.FooterPart;
import com.willshex.blogwt.client.part.HeaderPart;
import com.willshex.utility.StringUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
public abstract class Page extends RegisteringComposite {
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

	public boolean hasHeader () {
		return getHeader() != null;
	}

	public boolean hasFooter () {
		return getFooter() != null;
	}

	public Composite getHeader () {
		return HeaderPart.get();
	}

	public Composite getFooter () {
		return FooterPart.get();
	}
}
