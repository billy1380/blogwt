//
//  ResetPasswordPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.wizard.WizardDialog;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ResetPasswordPage extends Page {

	private static ResetPasswordPageUiBinder uiBinder = GWT
			.create(ResetPasswordPageUiBinder.class);

	interface ResetPasswordPageUiBinder extends
			UiBinder<Widget, ResetPasswordPage> {}

	@UiField FormPanel frmReset;
	@UiField TextBox txtEmail;
	@UiField HTMLPanel pnlEmailNote;
	@UiField HTMLPanel pnlEmail;

	@UiField Button btnReset;

	public ResetPasswordPage () {
		super(PageType.ResetPasswordPageType);
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtEmail, "E-mail");
		UiHelper.autoFocus(txtEmail);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		ready();
	}

	@UiHandler("btnReset")
	void onResetClicked (ClickEvent ce) {
		if (isValid()) {
			loading();

			UserController.resetPassword(txtEmail.getText());
		} else {
			showErrors();
		}
	}

	/**
	 * @return
	 */
	private boolean isValid () {
		// do client validation
		return true;
	}

	/**
	 * 
	 */
	private void showErrors () {

	}

	private void ready () {
		btnReset.getElement()
				.setInnerSafeHtml(
						WizardDialog.WizardDialogTemplates.INSTANCE
								.nextButton("Reset"));

		btnReset.setEnabled(true);
		txtEmail.setEnabled(true);
	}

	private void loading () {
		btnReset.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE.loadingButton(
						"Resetting... ", Resources.RES.primaryLoader()
								.getSafeUri()));

		btnReset.setEnabled(false);
		txtEmail.setEnabled(false);

		pnlEmail.removeStyleName("has-error");
		pnlEmailNote.setVisible(false);
	}
}
