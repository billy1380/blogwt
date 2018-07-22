//
//  PasswordPart.java
//  blogwt
//
//  Created by billy1380 on 16 Jul 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.password;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.shared.util.SparseArray;

/**
 * @author billy1380
 *
 */
public class PasswordPart extends Composite {

	private static PasswordPartUiBinder uiBinder = GWT
			.create(PasswordPartUiBinder.class);

	interface PasswordPartUiBinder extends UiBinder<Widget, PasswordPart> {}

	private static final int PASSWORD_FIELD_ID = 0;
	private static final int FIELD_COUNT = PASSWORD_FIELD_ID + 1;

	@UiField PasswordTextBox txtPassword;
	@UiField PasswordTextBox txtConfirmPassword;
	@UiField HTMLPanel pnlPasswordNote;

	private SparseArray<String> errors = new SparseArray<>();

	public PasswordPart () {
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtPassword, "Password");
		UiHelper.addPlaceholder(txtConfirmPassword, "Confirm password");
	}

	public String getPassword () {
		return txtPassword.getValue();
	}

	public void setEnabled (boolean enabled) {
		txtPassword.setEnabled(enabled);
		txtConfirmPassword.setEnabled(enabled);
	}

	public void clearErrors () {
		errors.clear();
		
		UiHelper.removeError((HTMLPanel) this.getWidget(), pnlPasswordNote);
	}

	public boolean isValid () {
		boolean valid = true;

		clearErrors();

		if (txtPassword.getValue().trim().isEmpty()) {
			valid = false;
			errors.put(PASSWORD_FIELD_ID, "Password cannot be empty");
		}

		if (txtConfirmPassword.getValue().trim().isEmpty()) {
			valid = false;
			errors.put(PASSWORD_FIELD_ID, "Confirm password cannot be empty");
		}

		if (!txtPassword.getValue().trim()
				.equals(txtConfirmPassword.getValue().trim())) {
			valid = false;
			errors.put(PASSWORD_FIELD_ID,
					"Password and confirmation should match");
		}

		return valid;
	}

	public void showErrors () {
		for (int key = 0; key < FIELD_COUNT; key++) {
			if (errors.containsKey(key)) {
				switch (key) {
				case PASSWORD_FIELD_ID:
					UiHelper.showError((HTMLPanel) this.getWidget(),
							pnlPasswordNote, errors.get(key));
					break;
				}
			}
		}
	}

}
