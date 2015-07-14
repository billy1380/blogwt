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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordResponse;
import com.willshex.blogwt.shared.api.user.call.event.ChangePasswordEventHandler;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ChangePasswordPage extends Page implements
		ChangePasswordEventHandler {

	private static ChangePasswordPageUiBinder uiBinder = GWT
			.create(ChangePasswordPageUiBinder.class);

	interface ChangePasswordPageUiBinder extends
			UiBinder<Widget, ChangePasswordPage> {}

	@UiField HTMLPanel pnlPassword;
	@UiField PasswordTextBox txtPassword;
	@UiField HTMLPanel pnlPasswordNote;

	@UiField HTMLPanel pnlNewPassword;
	@UiField PasswordTextBox txtNewPassword;
	@UiField PasswordTextBox txtConfirmPassword;
	@UiField HTMLPanel pnlNewPasswordNote;

	@UiField Button btnChange;

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

		ready();
	}

	@UiHandler("btnChange")
	void onChangeClicked (ClickEvent ce) {
		if (isValid()) {
			loading();
			// change password
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
			ChangePasswordResponse output) {}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.ChangePasswordEventHandler
	 * #changePasswordFailure(com.willshex.blogwt.shared.api.user.call.
	 * ChangePasswordRequest, java.lang.Throwable) */
	@Override
	public void changePasswordFailure (ChangePasswordRequest input,
			Throwable caught) {}

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

}
