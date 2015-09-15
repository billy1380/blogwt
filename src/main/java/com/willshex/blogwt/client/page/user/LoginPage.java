//
//  LoginPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.user.call.LoginRequest;
import com.willshex.blogwt.shared.api.user.call.LoginResponse;
import com.willshex.blogwt.shared.api.user.call.event.LoginEventHandler;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class LoginPage extends Page implements NavigationChangedEventHandler,
		LoginEventHandler {

	private static LoginPageUiBinder uiBinder = GWT
			.create(LoginPageUiBinder.class);

	interface LoginPageUiBinder extends UiBinder<Widget, LoginPage> {}

	@UiField HTMLPanel pnlUsername;
	@UiField TextBox txtUsername;
	@UiField HTMLPanel pnlUsernameNote;

	@UiField HTMLPanel pnlPassword;
	@UiField PasswordTextBox txtPassword;
	@UiField HTMLPanel pnlPasswordNote;

	@UiField CheckBox cbxRememberMe;

	@UiField Button btnSignIn;
	@UiField FormPanel frmLogin;

	public LoginPage () {
		super(PageType.LoginPageType);
		initWidget(uiBinder.createAndBindUi(this));

		txtUsername = UiHelper.swap(txtUsername, "login-username");
		txtPassword = UiHelper.swap(txtPassword, "login-password");

		UiHelper.addPlaceholder(txtUsername, "Username");
		UiHelper.autoFocus(txtUsername);

		UiHelper.addPlaceholder(txtPassword, "Password");
	}

	private void ready () {
		btnSignIn.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE
						.nextButton("Sign in"));

		btnSignIn.setEnabled(true);
		txtUsername.setEnabled(true);
		txtPassword.setEnabled(true);
		cbxRememberMe.setEnabled(true);

		txtPassword.setValue("");
		txtUsername.setFocus(true);
	}

	private void loading () {
		btnSignIn.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE.loadingButton(
						"Signing in... ", Resources.RES.primaryLoader()
								.getSafeUri()));

		btnSignIn.setEnabled(false);
		txtUsername.setEnabled(false);
		txtPassword.setEnabled(false);
		cbxRememberMe.setEnabled(false);

		pnlUsername.removeStyleName("has-error");
		pnlUsernameNote.setVisible(false);
		pnlPassword.removeStyleName("has-error");
		pnlPasswordNote.setVisible(false);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				LoginEventHandler.TYPE, SessionController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));

		ready();
	}

	@UiHandler("btnSignIn")
	void onBtnSignIn (ClickEvent event) {
		if (isValid()) {
			loading();
			SessionController.get().login(txtUsername.getText(),
					txtPassword.getText(),
					cbxRememberMe.getValue().booleanValue());
		} else {
			showErrors();
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.LoginEventHandler#loginSuccess
	 * (com.willshex.blogwt.shared.api.user.call.LoginRequest,
	 * com.willshex.blogwt.shared.api.user.call.LoginResponse) */
	@Override
	public void loginSuccess (LoginRequest input, LoginResponse output) {
		if (output.status == StatusType.StatusTypeFailure) {
			if (ApiHelper.isError(output.error, ApiError.AuthenticationFailed)) {
				pnlUsername.addStyleName("has-error");
				pnlUsernameNote.getElement().setInnerText(output.error.message);
				pnlUsernameNote.setVisible(true);
				pnlPassword.addStyleName("has-error");
			}
		} else {
			NavigationController.get().showNext();
		}

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.LoginEventHandler#loginFailure
	 * (com.willshex.blogwt.shared.api.user.call.LoginRequest,
	 * java.lang.Throwable) */
	@Override
	public void loginFailure (LoginRequest input, Throwable caught) {
		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.event.NavigationChangedEventHandler#
	 * navigationChanged
	 * (com.willshex.blogwt.client.controller.NavigationController.Stack,
	 * com.willshex.blogwt.client.controller.NavigationController.Stack) */
	@Override
	public void navigationChanged (Stack previous, Stack current) {
		String action = current.getAction();
		if (action != null) {
			String[] parts = action.split("=");

			if (parts != null && parts.length > 1) {
				if ("username".equals(parts[0])) {
					txtUsername.setValue(parts[1]);
				}
			}
		}
	}

	private boolean isValid () {
		// do client validation
		return true;
	}

	private void showErrors () {

	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#reset() */
	@Override
	protected void reset () {
		frmLogin.reset();

		pnlUsername.removeStyleName("has-error");
		pnlUsernameNote.setVisible(false);
		pnlPassword.removeStyleName("has-error");
		pnlPasswordNote.setVisible(false);

	}

}
