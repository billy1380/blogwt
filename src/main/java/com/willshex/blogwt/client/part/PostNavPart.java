//
//  PostNavPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Dec 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostNavPart extends Composite {

	private static PostNavPartUiBinder uiBinder = GWT
			.create(PostNavPartUiBinder.class);

	interface PostNavPartUiBinder extends UiBinder<Widget, PostNavPart> {}

	@UiField AnchorElement btnPrevious;
	@UiField Element elPrevious;

	@UiField AnchorElement btnNext;
	@UiField Element elNext;

	public PostNavPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * @param previousSlug
	 * @param nextSlug
	 */
	public void setSlugs (String previousSlug, String nextSlug) {
		setVisible(nextSlug != null || previousSlug != null);

		if (previousSlug == null) {
			elPrevious.addClassName("hidden");
			btnPrevious.setHref(PageTypeHelper.slugToHref(""));
		} else {
			elPrevious.removeClassName("hidden");
			btnPrevious.setHref(PageTypeHelper.asHref(
					PageType.PostDetailPageType, previousSlug));
		}

		if (nextSlug == null) {
			elNext.addClassName("hidden");
			btnNext.setHref(PageTypeHelper.slugToHref(""));
		} else {
			elNext.removeClassName("hidden");
			btnNext.setHref(PageTypeHelper.asHref(PageType.PostDetailPageType,
					nextSlug));
		}
	}

}
