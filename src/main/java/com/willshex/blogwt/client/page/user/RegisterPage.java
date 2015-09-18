//
//  RegisterPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RegisterPage extends Page {

	private static RegisterPageUiBinder uiBinder = GWT
			.create(RegisterPageUiBinder.class);

	interface RegisterPageUiBinder extends UiBinder<Widget, RegisterPage> {}

	@UiField HTMLPanel pnlAlert;

	public RegisterPage () {
		super(PageType.RegisterPageType);
		initWidget(uiBinder.createAndBindUi(this));

		pnlAlert.setVisible(!PropertyController.get().booleanProperty(
				PropertyHelper.ALLOW_USER_REGISTRATION, false));
	}

}
