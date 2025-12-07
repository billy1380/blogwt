//
//  LoginPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.api.user.event.LoginEventHandler;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.PageController;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.loginheader.LoginHeaderPart;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.user.call.LoginRequest;
import com.willshex.blogwt.shared.api.user.call.LoginResponse;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class LoginPage extends Page implements LoginEventHandler {

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
	@UiField HTMLPanel pnlAlternative;

	@UiField AnchorElement btnRegister;

	public LoginPage () {
		initWidget(uiBinder.createAndBindUi(this));

		txtUsername = UiHelper.swap(txtUsername, "login-username");
		txtPassword = UiHelper.swap(txtPassword, "login-password");

		UiHelper.addPlaceholder(txtUsername, "Username");
		UiHelper.autoFocus(txtUsername);

		UiHelper.addPlaceholder(txtPassword, "Password");
	}

	private void ready () {
		btnSignIn.getElement()
				.setInnerSafeHtml(WizardDialog.WizardDialogTemplates.INSTANCE
						.nextButton("Sign in"));

		btnSignIn.setEnabled(true);
		txtUsername.setEnabled(true);
		txtPassword.setEnabled(true);
		cbxRememberMe.setEnabled(true);

		txtPassword.setValue("");
		txtUsername.setFocus(true);
	}

	private void loading () {
		btnSignIn.getElement()
				.setInnerSafeHtml(WizardDialog.WizardDialogTemplates.INSTANCE
						.loadingButton("Signing in... ",
								Resources.RES.primaryLoader().getSafeUri()));

		btnSignIn.setEnabled(false);
		txtUsername.setEnabled(false);
		txtPassword.setEnabled(false);
		cbxRememberMe.setEnabled(false);

		UiHelper.removeError(pnlUsername, pnlUsernameNote);
		UiHelper.removeError(pnlPassword, pnlPasswordNote);
	}
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				LoginEventHandler.TYPE, SessionController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				(p, c) -> {
					btnRegister.setHref(PageTypeHelper.REGISTER_PAGE_HREF);

					if (SessionController.get().isValidSession()) {
						if (c.hasNext()) {
							NavigationController.get().showNext();
						} else {
							PageTypeHelper.show(PageController.get()
									.homePageTargetHistoryToken());
						}
					} else {
						processParameter(c.getAction());

						if (c.hasNext()) {
							btnRegister.setHref(PageTypeHelper.asHref(
									PageType.RegisterPageType,
									c.getNext().asNextParameter()));
						}
					}
				}));

		pnlAlternative.setVisible(PropertyController.get().booleanProperty(
				PropertyHelper.ALLOW_USER_REGISTRATION, false));

		ready();
	}

	private void processParameter (String parameter) {
		if (parameter != null) {
			String[] parts = parameter.split("=");

			if (parts != null && parts.length > 1) {
				if ("username".equals(parts[0])) {
					txtUsername.setValue(parts[1]);
				} else if (isNext(parts[0])) {

				}
			}
		}
	}

	private boolean isNext (String part) {
		return Stack.NEXT_KEY.substring(0, Stack.NEXT_KEY.length() - 1)
				.equals(part);
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
	@Override
	public void loginSuccess (LoginRequest input, LoginResponse output) {
		if (output.status == StatusType.StatusTypeFailure) {
			if (ApiHelper.isError(output.error,
					ApiError.AuthenticationFailed)) {
				UiHelper.showError(pnlUsername, pnlPasswordNote,
						output.error.message);
				pnlPassword.addStyleName(UiHelper.HAS_ERROR_STYLE);
			}
		} else {
			NavigationController.get().replaceNext();
		}

		ready();
	}
	@Override
	public void loginFailure (LoginRequest input, Throwable caught) {
		ready();
	}

	private boolean isValid () {
		// do client validation
		return true;
	}

	private void showErrors () {

	}
	@Override
	protected void reset () {
		frmLogin.reset();

		UiHelper.removeError(pnlUsername, pnlUsernameNote);
		UiHelper.removeError(pnlPassword, pnlPasswordNote);

		btnRegister.setHref(PageTypeHelper.REGISTER_PAGE_HREF);
	}
	@Override
	public String getTitle () {
		return UiHelper.pageTitle("Sign in");
	}
	@Override
	public boolean hasHeader () {
		return false;
	}
	@Override
	public Composite getHeader () {
		return LoginHeaderPart.get();
	}

}
