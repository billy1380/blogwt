//
//  ChangePasswordPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
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
import com.willshex.blogwt.client.api.user.event.ChangePasswordEventHandler;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.accounttabs.AccountTabsPart;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordResponse;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ChangePasswordPage extends Page
		implements ChangePasswordEventHandler {

	private static ChangePasswordPageUiBinder uiBinder = GWT
			.create(ChangePasswordPageUiBinder.class);

	interface ChangePasswordPageUiBinder
			extends UiBinder<Widget, ChangePasswordPage> {}

	@UiField FormPanel frmPasswords;

	@UiField HTMLPanel pnlPassword;
	@UiField PasswordTextBox txtPassword;
	@UiField HTMLPanel pnlPasswordNote;

	@UiField HTMLPanel pnlNewPassword;
	@UiField PasswordTextBox txtNewPassword;
	@UiField PasswordTextBox txtConfirmPassword;
	@UiField HTMLPanel pnlNewPasswordNote;

	@UiField Button btnChange;
	@UiField Element elReset;
	@UiField Element elActionCode;

	@UiField HTMLPanel pnlTabs;

	private Long userId;
	private String actionCode;

	public ChangePasswordPage () {
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtPassword, "Password");
		UiHelper.autoFocus(txtPassword);
		UiHelper.addPlaceholder(txtNewPassword, "New password");
		UiHelper.addPlaceholder(txtConfirmPassword, "Confirm new password");

		elReset.removeFromParent();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		pnlTabs.add(AccountTabsPart.get());

		register(DefaultEventBus.get().addHandlerToSource(
				ChangePasswordEventHandler.TYPE, UserController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				(p, c) -> {
					reset();

					boolean reset = false;
					if ("id".equals(c.getAction())
							|| (reset = "reset".equals(c.getAction()))) {
						if (c.getParameterCount() > 0) {
							String value = c.getParameter(0);

							pnlPassword.setVisible(false);

							if (reset) {
								actionCode = value;
								elActionCode.setInnerText(actionCode);
								pnlPassword.getElement().getParentElement()
										.insertBefore(elReset,
												pnlPassword.getElement());
							} else {
								userId = Long.valueOf(value);
							}
						}
					}

					ready();
				}));

		ready();
	}

	@UiHandler("btnChange")
	void onChangeClicked (ClickEvent ce) {
		if (isValid()) {
			loading();

			if (actionCode == null) {
				UserController.get().changeUserPassword(userId,
						txtPassword.getText(), txtNewPassword.getText());
			} else {
				UserController.get().changeUserPassword(actionCode,
						txtNewPassword.getText());
			}
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
		btnChange.getElement()
				.setInnerSafeHtml(WizardDialog.WizardDialogTemplates.INSTANCE
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
			if (actionCode == null) {
				if (userId == null) {
					PageTypeHelper.show(PageType.ChangeDetailsPageType);
				} else {
					PageTypeHelper.show(PageType.ChangeDetailsPageType, "id",
							userId.toString());
				}
			} else {
				PageTypeHelper.show(PageType.LoginPageType);
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
		btnChange.getElement()
				.setInnerSafeHtml(WizardDialog.WizardDialogTemplates.INSTANCE
						.loadingButton("Changing... ",
								Resources.RES.primaryLoader().getSafeUri()));

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
	 * @see com.willshex.blogwt.client.page.Page#reset() */
	@Override
	protected void reset () {
		frmPasswords.reset();
		userId = null;
		actionCode = null;
		elActionCode.setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
		elReset.removeFromParent();
		pnlPassword.setVisible(true);
		super.reset();
	}
}
