//
//  Account.java
//  quickinvoice
//
//  Created by William Shakour (billy1380) on 20 Dec 2014.
//  Copyright © 2014 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.page.invoice;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.utility.JsonUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
public class Account extends Page {

	private static AccountUiBinder uiBinder = GWT.create(AccountUiBinder.class);

	interface AccountUiBinder extends UiBinder<Widget, Account> {}

	@UiField TextArea txtAccountJson;
	@UiField Image imgAvatar;
	@UiField HeadingElement h3Username;

	public Account() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach()
	 */
	@Override
	protected void onAttach() {
		super.onAttach();

		User user = SessionController.get().user();

		if (user != null) {
			txtAccountJson.setText(JsonUtils.beautifyJson(user.toString()));
			imgAvatar.setAltText(user.username);
			imgAvatar.setUrl(user.avatar + "?s=160&default=retro");
			h3Username.setInnerHTML(user.username);
		}
	}

}
