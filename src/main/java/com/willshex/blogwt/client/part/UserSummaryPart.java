//
//  UserSummaryPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 4 Dec 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.shared.api.datatype.User;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UserSummaryPart extends Composite {

	private static UserSummaryPartUiBinder uiBinder = GWT
			.create(UserSummaryPartUiBinder.class);

	interface UserSummaryPartUiBinder extends UiBinder<Widget, UserSummaryPart> {}

	public UserSummaryPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setUser (User user) {
		show(user);
	}

	private void show (User user) {

	}
}
