//
//  ChangeDetailsPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarRequest;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarResponse;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.RegisterUserRequest;
import com.willshex.blogwt.shared.api.user.call.RegisterUserResponse;
import com.willshex.blogwt.shared.api.user.call.event.ChangeUserDetailsEventHandler;
import com.willshex.blogwt.shared.api.user.call.event.GetEmailAvatarEventHandler;
import com.willshex.blogwt.shared.api.user.call.event.GetUserDetailsEventHandler;
import com.willshex.blogwt.shared.api.user.call.event.RegisterUserEventHandler;
import com.willshex.blogwt.shared.helper.DateTimeHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ChangeDetailsPage extends Page implements
		NavigationChangedEventHandler, GetUserDetailsEventHandler,
		ChangeUserDetailsEventHandler, GetEmailAvatarEventHandler,
		RegisterUserEventHandler {

	private static ChangeDetailsPageUiBinder uiBinder = GWT
			.create(ChangeDetailsPageUiBinder.class);

	interface ChangeDetailsPageUiBinder extends
			UiBinder<Widget, ChangeDetailsPage> {}

	public interface ChangeDetailsTemplates extends SafeHtmlTemplates {
		ChangeDetailsTemplates INSTANCE = GWT
				.create(ChangeDetailsTemplates.class);

		@Template("{0} (<a href=\"https://en.gravatar.com/\" target=\"_blank\">Gravatar</a>)")
		SafeHtml gravatarComment (String comment);

		@Template("{0} <small>(<a href=\"{1}\">access</a>)</small>")
		SafeHtml rolesAndPermissions (String username, String accessHref);

		@Template("{0} <a href=\"{1}\"><span class=\"glyphicon glyphicon-sunglasses\"></span></a>")
		SafeHtml adminRolesAndPermissions (String username, String accessHref);
	}

	private static final String UPDATE_ACTION_TEXT = "Update";
	private static final String CREATE_ACTION_TEXT = "Create";
	private static final String REGISTER_ACTION_TEXT = "Register";

	@UiField Element elHeading;
	@UiField Element elGravatar;
	@UiField FormPanel frmDetails;

	@UiField Image imgAvatar;
	@UiField HeadingElement h3Username;
	@UiField Element elDates;

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

	@UiField Button btnUpdate;
	@UiField Hyperlink lnkChangePassword;

	private User user;
	private String actionText;

	public ChangeDetailsPage () {
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtUsername, "Username");
		UiHelper.autoFocus(txtUsername);

		UiHelper.addPlaceholder(txtPassword, "Password");
		UiHelper.addPlaceholder(txtConfirmPassword, "Confirm password");

		UiHelper.addPlaceholder(txtForename, "Forename");
		UiHelper.addPlaceholder(txtSurname, "Surname");
		UiHelper.addPlaceholder(txtEmail, "Email");

		actionText = UPDATE_ACTION_TEXT;
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
				GetUserDetailsEventHandler.TYPE, UserController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				ChangeUserDetailsEventHandler.TYPE, UserController.get(), this));
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

		if (PageType.ChangeDetailsPageType.equals(current.getPage())) {
			if (current.getAction() == null) {
				show(user = SessionController.get().user());
				lnkChangePassword
						.setTargetHistoryToken(PageType.ChangePasswordPageType
								.asTargetHistoryToken());
			} else if ("id".equals(current.getAction())
					&& current.getParameterCount() > 0) {
				Long id = Long.valueOf(current.getParameter(0));
				User user = new User();
				user.id(id);

				UserController.get().getUser(user);

				lnkChangePassword.setVisible(true);
				pnlPassword.setVisible(false);
			} else if ("new".equals(current.getAction())) {
				elDates.setInnerText("Enter user details");
				lnkChangePassword.setVisible(false);
				pnlPassword.setVisible(true);

				if (SessionController.get().isAdmin()) {
					actionText = CREATE_ACTION_TEXT;
				}
			}
		} else if (PageType.RegisterPageType.equals(current.getPage())) {
			elDates.setInnerText("Enter user details");
			lnkChangePassword.setVisible(false);
			pnlPassword.setVisible(true);

			actionText = REGISTER_ACTION_TEXT;
		}

		elHeading.setInnerText(getHeadingText());
		elGravatar.setInnerSafeHtml(getGravatarSafeHtml());

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

	private void show (User user) {
		if (user != null) {
			String username = "@" + user.username;
			imgAvatar.setAltText(username);
			imgAvatar.setUrl(user.avatar + "?s=160&default=retro");
			h3Username
					.setInnerSafeHtml(UserHelper.isAdmin(user) ? ChangeDetailsTemplates.INSTANCE
							.adminRolesAndPermissions(
									username,
									PageTypeHelper.asHref(
											PageType.ChangeAccessPageType,
											user.id.toString()).asString())
							: ChangeDetailsTemplates.INSTANCE
									.rolesAndPermissions(
											username,
											PageTypeHelper
													.asHref(PageType.ChangeAccessPageType,
															user.id.toString())
													.asString()));
			elDates.setInnerText("Added "
					+ DateTimeHelper.ago(user.created)
					+ " and last seen "
					+ (user.lastLoggedIn == null ? "never" : DateTimeHelper
							.ago(user.lastLoggedIn)));
			txtUsername.setText(user.username);
			txtForename.setText(user.forename);
			txtSurname.setText(user.surname);
			txtEmail.setText(user.email);

			lnkChangePassword.setVisible(true);
			pnlPassword.setVisible(false);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.GetUserDetailsEventHandler
	 * #getUserDetailsSuccess(com.willshex.blogwt.shared.api.user.call.
	 * GetUserDetailsRequest,
	 * com.willshex.blogwt.shared.api.user.call.GetUserDetailsResponse) */
	@Override
	public void getUserDetailsSuccess (GetUserDetailsRequest input,
			GetUserDetailsResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			show(user = output.user);
			lnkChangePassword
					.setTargetHistoryToken(PageType.ChangePasswordPageType
							.asTargetHistoryToken("id", user.id.toString()));
		}

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.GetUserDetailsEventHandler
	 * #getUserDetailsFailure(com.willshex.blogwt.shared.api.user.call.
	 * GetUserDetailsRequest, java.lang.Throwable) */
	@Override
	public void getUserDetailsFailure (GetUserDetailsRequest input,
			Throwable caught) {
		GWT.log("getUserDetailsFailure", caught);

		ready();
	}

	@UiHandler("btnUpdate")
	void onUpdateClicked (ClickEvent ce) {
		if (isValid()) {
			loading();
			User user = new User().username(txtUsername.getText())
					.forename(txtForename.getText())
					.surname(txtSurname.getText()).email(txtEmail.getText());

			if (this.user == null) {
				user.password(txtPassword.getText());
				UserController.get().registerUser(user);
			} else {
				user.id(this.user.id);
				UserController.get().changeUserDetails(user);
			}
		} else {
			showErrors();
		}
	}

	private boolean isValid () {
		// do client validation - including property check for user registration
		return true;
	}

	private void showErrors () {

	}

	private void ready () {
		btnUpdate.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE
						.nextButton(actionText));

		btnUpdate.setEnabled(true);
		txtUsername.setEnabled(true);
		txtForename.setEnabled(true);
		txtSurname.setEnabled(true);
		txtEmail.setEnabled(true);
		txtPassword.setEnabled(true);
		txtConfirmPassword.setEnabled(true);

		txtUsername.setFocus(true);
	}

	private void loading () {
		btnUpdate.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE.loadingButton(
						getLoadingText(), Resources.RES.primaryLoader()
								.getSafeUri()));

		btnUpdate.setEnabled(false);
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

	private String getLoadingText () {
		String loadingText = null;
		switch (actionText) {
		case UPDATE_ACTION_TEXT:
			loadingText = "Updating... ";
			break;
		case CREATE_ACTION_TEXT:
			loadingText = "Creating... ";
			break;
		case REGISTER_ACTION_TEXT:
			loadingText = "Registering... ";
			break;
		}

		return loadingText;
	}

	private String getHeadingText () {
		String headingText = null;
		switch (actionText) {
		case UPDATE_ACTION_TEXT:
			headingText = "Account";
			break;
		case CREATE_ACTION_TEXT:
			headingText = "Add User";
			break;
		case REGISTER_ACTION_TEXT:
			headingText = "Sign Up";
			break;
		}

		return headingText;
	}

	private SafeHtml getGravatarSafeHtml () {
		SafeHtml gravatarSafeHtml = null;
		switch (actionText) {
		case UPDATE_ACTION_TEXT:
			gravatarSafeHtml = ChangeDetailsTemplates.INSTANCE
					.gravatarComment("Changing e-mail address will change your avatar");
			break;
		case CREATE_ACTION_TEXT:
			gravatarSafeHtml = ChangeDetailsTemplates.INSTANCE
					.gravatarComment("E-mail address will determine user's avatar");
			break;
		case REGISTER_ACTION_TEXT:
			gravatarSafeHtml = ChangeDetailsTemplates.INSTANCE
					.gravatarComment("Your e-mail address will determine your avatar");
			break;
		}

		return gravatarSafeHtml;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.ChangeUserDetailsEventHandler
	 * #changeUserDetailsSuccess(com.willshex.blogwt.shared.api.user.call.
	 * ChangeUserDetailsRequest,
	 * com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsResponse) */
	@Override
	public void changeUserDetailsSuccess (ChangeUserDetailsRequest input,
			ChangeUserDetailsResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			show(user = output.user);
		}

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.ChangeUserDetailsEventHandler
	 * #changeUserDetailsFailure(com.willshex.blogwt.shared.api.user.call.
	 * ChangeUserDetailsRequest, java.lang.Throwable) */
	@Override
	public void changeUserDetailsFailure (ChangeUserDetailsRequest input,
			Throwable caught) {
		GWT.log("changeUserDetailsFailure", caught);

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#reset() */
	@Override
	protected void reset () {
		frmDetails.reset();
		imgAvatar.setAltText("");
		imgAvatar.setUrl("");
		elDates.setInnerText("");
		h3Username.setInnerText("");
		actionText = "Update";
		user = null;

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
