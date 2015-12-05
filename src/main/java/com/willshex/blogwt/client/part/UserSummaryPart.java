//
//  UserSummaryPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 4 Dec 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.UserHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UserSummaryPart extends Composite {

	private static UserSummaryPartUiBinder uiBinder = GWT
			.create(UserSummaryPartUiBinder.class);

	interface UserSummaryPartUiBinder extends UiBinder<Widget, UserSummaryPart> {}

	@UiField Image imgAvatar;
	@UiField Element elName;
	@UiField Element elUsername;
	@UiField Element elSummary;

	public UserSummaryPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setUser (User user) {
		show(user);
	}

	private void show (User user) {
		String username = "@" + user.username;
		imgAvatar.setAltText(username);
		imgAvatar.setUrl(user.avatar + "?s=80&default=retro");
		elName.setInnerHTML(UserHelper.name(user));
		elUsername.setInnerHTML(username);

		if (user.summary != null) {
			elSummary.setInnerHTML(PostHelper.makeMarkup(user.summary));
		} else {
			elSummary.setInnerHTML("<p class=\"text-muted text-justify\">"
					+ UserHelper.name(user)
					+ " has not entered a user summary.</p>");
		}
	}
}
