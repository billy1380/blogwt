//
//  SectionPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 24 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SectionPart extends Composite {

	private static SectionPartUiBinder uiBinder = GWT
			.create(SectionPartUiBinder.class);

	interface SectionPartUiBinder extends UiBinder<Widget, SectionPart> {}

	@UiField Element elToolbar;
	@UiField Element elContent;
	@UiField Element elJumpTo;
	@UiField InlineHyperlink lnkEditPost;

	public SectionPart () {
		initWidget(uiBinder.createAndBindUi(this));

		boolean canChange = SessionController.get().isAuthorised(
				Arrays.asList(PermissionHelper
						.create(PermissionHelper.MANAGE_PAGES)));
		if (canChange) {
			getElement().insertFirst(elToolbar);
		} else {
			elToolbar.removeFromParent();
		}
	}

	/**
	 * 
	 * @param pageSlug
	 * @param post
	 */
	public void setContent (String pageSlug, Post post) {
		if (post.content != null && post.content.body != null) {
			elContent.setInnerHTML(PostHelper.makeMarkup(post.content.body));
		}

		elJumpTo.setAttribute("name", "!" + pageSlug + "/" + post.slug);

		lnkEditPost.setTargetHistoryToken(PageType.EditPostPageType
				.asTargetHistoryToken(post.slug));
	}

}
