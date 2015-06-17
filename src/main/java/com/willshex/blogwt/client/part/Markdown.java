//
//  Markdown.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 16 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.dom.client.Document;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.PostHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class Markdown extends Widget implements HasHTML, HasSafeHtml {

	private String markdown;

	public Markdown () {
		setElement(Document.get().createDivElement());
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasText#getText() */
	@Override
	public String getText () {
		return markdown;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String) */
	@Override
	public void setText (String markdown) {
		this.markdown = markdown;
		getElement().setInnerHTML(getHTML());
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.safehtml.client.HasSafeHtml#setHTML(com.google.gwt.safehtml
	 * .shared.SafeHtml) */
	@Override
	public void setHTML (SafeHtml markdown) {
		this.markdown = markdown.asString();
		getElement().setInnerHTML(getHTML());
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasHTML#getHTML() */
	@Override
	public String getHTML () {
		return markdown == null ? SafeHtmlUtils.EMPTY_SAFE_HTML.asString()
				: PostHelper.makeMarkup(markdown);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasHTML#setHTML(java.lang.String) */
	@Override
	public void setHTML (String markdown) {
		this.markdown = markdown;
		getElement().setInnerHTML(getHTML());
	}

}