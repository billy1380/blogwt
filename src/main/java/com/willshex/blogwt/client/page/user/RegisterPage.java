//
//  RegisterPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarRequest;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarResponse;
import com.willshex.blogwt.shared.api.user.call.RegisterUserRequest;
import com.willshex.blogwt.shared.api.user.call.RegisterUserResponse;
import com.willshex.blogwt.shared.api.user.call.event.GetEmailAvatarEventHandler;
import com.willshex.blogwt.shared.api.user.call.event.RegisterUserEventHandler;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RegisterPage extends Page implements
		NavigationChangedEventHandler, GetEmailAvatarEventHandler,
		RegisterUserEventHandler {

	private static RegisterPageUiBinder uiBinder = GWT
			.create(RegisterPageUiBinder.class);

	interface RegisterPageUiBinder extends UiBinder<Widget, RegisterPage> {}

	@UiField HTMLPanel pnlAlert;
	@UiField HTMLPanel pnlRegister;

	@UiField FormPanel frmDetails;

	@UiField Image imgAvatar;
	@UiField HeadingElement h3Username;

	@UiField HTMLPanel pnlUsername;
	@UiField TextBox txtUsername;
	@UiField HTMLPanel pnlUsernameNote;

	@UiField HTMLPanel pnlPassword;
	@UiField PasswordTextBox txtPassword;
	@UiField PasswordTextBox txtConfirmPassword;
	@UiField HTMLPanel pnlPasswordNote;

	@UiField HTMLPanel pnlForename;
	@UiField TextBox txtForename;
	@UiField HTMLPanel pnlForenameNote;

	@UiField HTMLPanel pnlSurname;
	@UiField TextBox txtSurname;
	@UiField HTMLPanel pnlSurnameNote;

	@UiField HTMLPanel pnlEmail;
	@UiField TextBox txtEmail;
	@UiField HTMLPanel pnlEmailNote;

	@UiField Button btnRegister;

	public RegisterPage () {
		super(PageType.RegisterPageType);
		initWidget(uiBinder.createAndBindUi(this));

		boolean allowed;
		pnlAlert.setVisible(!(allowed = PropertyController.get()
				.booleanProperty(PropertyHelper.ALLOW_USER_REGISTRATION, false)));
		pnlRegister.setVisible(allowed);

		if (allowed) {
			UiHelper.addPlaceholder(txtUsername, "Username");
			UiHelper.autoFocus(txtUsername);

			UiHelper.addPlaceholder(txtPassword, "Password");
			UiHelper.addPlaceholder(txtConfirmPassword, "Confirm password");

			UiHelper.addPlaceholder(txtForename, "Forename");
			UiHelper.addPlaceholder(txtSurname, "Surname");
			UiHelper.addPlaceholder(txtEmail, "Email");
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));
		register(DefaultEventBus.get().addHandlerToSource(
				GetEmailAvatarEventHandler.TYPE, UserController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				RegisterUserEventHandler.TYPE, UserController.get(), this));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.event.NavigationChangedEventHandler#
	 * navigationChanged
	 * (com.willshex.blogwt.client.controller.NavigationController.Stack,
	 * com.willshex.blogwt.client.controller.NavigationController.Stack) */
	@Override
	public void navigationChanged (Stack previous, Stack current) {
		reset();
		ready();
	}

	@UiHandler("txtUsername")
	void onUsernameChanged (ValueChangeEvent<String> vce) {
		String username = "@" + vce.getValue();
		imgAvatar.setAltText(username);
		h3Username.setInnerHTML(username);
	}

	@UiHandler("txtEmail")
	void onEmailChanged (ValueChangeEvent<String> vce) {
		UserController.get().getEmailAvatar(vce.getValue());
	}

	@UiHandler("btnRegister")
	void onRegisterClicked (ClickEvent ce) {
		if (isValid()) {
			loading();
			User user = new User().username(txtUsername.getText())
					.forename(txtForename.getText())
					.surname(txtSurname.getText()).email(txtEmail.getText());

			user.password(txtPassword.getText());
			UserController.get().registerUser(user);
		} else {
			showErrors();
		}
	}

	private boolean isValid () {
		// do client validation
		return true;
	}

	private void showErrors () {

	}

	private void ready () {
		btnRegister.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE
						.nextButton("Register"));

		btnRegister.setEnabled(true);
		txtUsername.setEnabled(true);
		txtForename.setEnabled(true);
		txtSurname.setEnabled(true);
		txtEmail.setEnabled(true);
		txtPassword.setEnabled(true);
		txtConfirmPassword.setEnabled(true);

		txtUsername.setFocus(true);
	}

	private void loading () {
		btnRegister.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE.loadingButton(
						"Registering...", Resources.RES.primaryLoader()
								.getSafeUri()));

		btnRegister.setEnabled(false);
		txtUsername.setEnabled(false);
		txtForename.setEnabled(false);
		txtSurname.setEnabled(false);
		txtEmail.setEnabled(false);
		txtPassword.setEnabled(false);
		txtConfirmPassword.setEnabled(false);

		pnlUsername.removeStyleName("has-error");
		pnlUsernameNote.setVisible(false);
		pnlForename.removeStyleName("has-error");
		pnlForenameNote.setVisible(false);
		pnlSurname.removeStyleName("has-error");
		pnlSurnameNote.setVisible(false);
		pnlEmail.removeStyleName("has-error");
		pnlEmailNote.setVisible(false);
		pnlPassword.removeStyleName("has-error");
		pnlPasswordNote.setVisible(false);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#reset() */
	@Override
	protected void reset () {
		frmDetails.reset();
		imgAvatar.setAltText("");
		imgAvatar.setUrl("");
		h3Username.setInnerText("-");

		super.reset();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.GetEmailAvatarEventHandler
	 * #getEmailAvatarSuccess(com.willshex.blogwt.shared.api.user.call.
	 * GetEmailAvatarRequest,
	 * com.willshex.blogwt.shared.api.user.call.GetEmailAvatarResponse) */
	@Override
	public void getEmailAvatarSuccess (GetEmailAvatarRequest input,
			GetEmailAvatarResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			imgAvatar.setUrl(output.avatar + "?s=160&default=retro");
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.GetEmailAvatarEventHandler
	 * #getEmailAvatarFailure(com.willshex.blogwt.shared.api.user.call.
	 * GetEmailAvatarRequest, java.lang.Throwable) */
	@Override
	public void getEmailAvatarFailure (GetEmailAvatarRequest input,
			Throwable caught) {
		GWT.log("getEmailAvatarFailure", caught);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.RegisterUserEventHandler
	 * #registerUserSuccess
	 * (com.willshex.blogwt.shared.api.user.call.RegisterUserRequest,
	 * com.willshex.blogwt.shared.api.user.call.RegisterUserResponse) */
	@Override
	public void registerUserSuccess (RegisterUserRequest input,
			RegisterUserResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			if (SessionController.get().isAdmin()) {
				PageTypeHelper.show(PageType.ChangeDetailsPageType, "id",
						output.user.id.toString());
			} else {
				PageTypeHelper.show(PageType.LoginPageType, "username="
						+ output.user.username.toString());
			}
		}

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.RegisterUserEventHandler
	 * #registerUserFailure
	 * (com.willshex.blogwt.shared.api.user.call.RegisterUserRequest,
	 * java.lang.Throwable) */
	@Override
	public void registerUserFailure (RegisterUserRequest input, Throwable caught) {
		GWT.log("registerUserFailure", caught);

		ready();
	}
}
