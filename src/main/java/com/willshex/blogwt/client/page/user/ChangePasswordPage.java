//
//  ChangePasswordPage.java
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
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordResponse;
import com.willshex.blogwt.shared.api.user.call.event.ChangePasswordEventHandler;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ChangePasswordPage extends Page implements
		ChangePasswordEventHandler, NavigationChangedEventHandler {

	private static ChangePasswordPageUiBinder uiBinder = GWT
			.create(ChangePasswordPageUiBinder.class);

	interface ChangePasswordPageUiBinder extends
			UiBinder<Widget, ChangePasswordPage> {}

	@UiField FormPanel frmPasswords;

	@UiField HTMLPanel pnlPassword;
	@UiField PasswordTextBox txtPassword;
	@UiField HTMLPanel pnlPasswordNote;

	@UiField HTMLPanel pnlNewPassword;
	@UiField PasswordTextBox txtNewPassword;
	@UiField PasswordTextBox txtConfirmPassword;
	@UiField HTMLPanel pnlNewPasswordNote;

	@UiField Button btnChange;

	private Long userId;

	public ChangePasswordPage () {
		super(PageType.ChangePasswordPageType);
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtPassword, "Password");
		UiHelper.autoFocus(txtPassword);
		UiHelper.addPlaceholder(txtNewPassword, "New password");
		UiHelper.addPlaceholder(txtConfirmPassword, "Confirm new password");
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				ChangePasswordEventHandler.TYPE, UserController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));

		ready();
	}

	@UiHandler("btnChange")
	void onChangeClicked (ClickEvent ce) {
		if (isValid()) {
			loading();

			UserController.get().changeUserPassword(userId,
					txtPassword.getText(), txtNewPassword.getText());
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
		btnChange.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE
						.nextButton("Change"));

		btnChange.setEnabled(true);
		txtPassword.setEnabled(true);
		txtNewPassword.setEnabled(true);
		txtConfirmPassword.setEnabled(true);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.ChangePasswordEventHandler
	 * #changePasswordSuccess(com.willshex.blogwt.shared.api.user.call.
	 * ChangePasswordRequest,
	 * com.willshex.blogwt.shared.api.user.call.ChangePasswordResponse) */
	@Override
	public void changePasswordSuccess (ChangePasswordRequest input,
			ChangePasswordResponse output) {
		if (output.status == StatusType.StatusTypeFailure) {
			GWT.log("Could not change password with error ["
					+ (output.error == null ? "none" : output.error.toString())
					+ "]");
		} else {
			if (userId == null) {
				PageType.ChangeDetailsPageType.show();
			} else {
				PageType.ChangeDetailsPageType.show("id", userId.toString());
			}
		}

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.ChangePasswordEventHandler
	 * #changePasswordFailure(com.willshex.blogwt.shared.api.user.call.
	 * ChangePasswordRequest, java.lang.Throwable) */
	@Override
	public void changePasswordFailure (ChangePasswordRequest input,
			Throwable caught) {
		GWT.log("Could not change password", caught);

		txtPassword.setText("");
		ready();
	}

	private void loading () {
		btnChange.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE.loadingButton(
						"Changing... ", Resources.RES.primaryLoader()
								.getSafeUri()));

		btnChange.setEnabled(false);
		txtPassword.setEnabled(false);
		txtNewPassword.setEnabled(false);
		txtConfirmPassword.setEnabled(false);

		pnlPassword.removeStyleName("has-error");
		pnlPasswordNote.setVisible(false);
		pnlNewPassword.removeStyleName("has-error");
		pnlNewPasswordNote.setVisible(false);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.event.NavigationChangedEventHandler#
	 * navigationChanged
	 * (com.willshex.blogwt.client.controller.NavigationController.Stack,
	 * com.willshex.blogwt.client.controller.NavigationController.Stack) */
	@Override
	public void navigationChanged (Stack previous, Stack current) {
		if ("id".equals(current.getAction())) {
			if (current.getParameterCount() > 0) {
				userId = Long.valueOf(current.getParameter(0));
			}
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#reset() */
	@Override
	protected void reset () {
		frmPasswords.reset();
		userId = null;
		super.reset();
	}
}
