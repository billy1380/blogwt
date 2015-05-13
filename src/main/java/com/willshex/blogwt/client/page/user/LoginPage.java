//
//  LoginPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.wizard.WizardDialog;

/**
 * @author William Shakour (billy1380)
 *
 */
public class LoginPage extends Page {

	private static LoginPageUiBinder uiBinder = GWT
			.create(LoginPageUiBinder.class);

	interface LoginPageUiBinder extends UiBinder<Widget, LoginPage> {}

	@UiField Button btnSignIn;
	@UiField TextBox txtUsername;
	@UiField PasswordTextBox txtPassword;
	@UiField CheckBox cbxRememberMe;
	
	public LoginPage () {
		super(PageType.LoginPageType);
		initWidget(uiBinder.createAndBindUi(this));
		
		txtUsername = UiHelper.swap(txtUsername, "login-username");
		txtPassword = UiHelper.swap(txtPassword, "login-password");

		txtUsername.getElement().setAttribute("placeholder", "Username");
		txtUsername.getElement().setAttribute("autofocus", "");
		txtPassword.getElement().setAttribute("placeholder", "Password");

		btnSignIn.getElement().setInnerSafeHtml(WizardDialog.WizardDialogTemplates.INSTANCE.nextButton("Sign in"));
	}

}
