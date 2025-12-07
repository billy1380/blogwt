//
//  AddUserWizardPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.wizard.page;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.RoleHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class AddUserWizardPage extends Composite implements WizardPage<User> {

	private static AddUserWizardPageUiBinder uiBinder = GWT
			.create(AddUserWizardPageUiBinder.class);

	interface AddUserWizardPageUiBinder extends
			UiBinder<Widget, AddUserWizardPage> {}

	@UiField public TextBox txtUsername;
	@UiField public TextBox txtEmail;
	@UiField public TextBox txtForename;
	@UiField public TextBox txtSurname;
	@UiField public PasswordTextBox txtPassword;
	@UiField public PasswordTextBox txtConfirmPassword;
	@UiField public CheckBox cbxIsAdmin;

	public AddUserWizardPage () {
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtUsername, "Username");
		UiHelper.autoFocus(txtUsername);

		UiHelper.addPlaceholder(txtEmail, "E-mail address");
		UiHelper.addPlaceholder(txtForename, "Forename");
		UiHelper.addPlaceholder(txtSurname, "Surname");
		UiHelper.addPlaceholder(txtPassword, "Password");
		UiHelper.addPlaceholder(txtConfirmPassword, "Confirm password");
	}
	@Override
	public boolean isRepeatable () {
		return true;
	}
	@Override
	public User getData () {
		List<Role> roles = null;
		if (cbxIsAdmin.getValue().booleanValue()) {
			(roles = new ArrayList<Role>()).add(RoleHelper.createAdmin());
		}

		return new User().username(txtUsername.getText())
				.email(txtEmail.getText())
				.forename(txtForename.getText()).surname(txtSurname.getText())
				.password(txtPassword.getText()).roles(roles);
	}
	@Override
	public String getPageTitle () {
		return "Add user";
	}
	@Override
	public Widget getBody () {
		return this;
	}
	@Override
	public WizardPage<?> another () {
		return new AddUserWizardPage();
	}
	@Override
	public boolean isValid () {
		return true;
	}
	@Override
	public void setData (User data) {
		
	}
	@Override
	public String getPageDescription () {
		return "Add a new user.";
	}
	@Override
	public Focusable getAutoFocusField () {
		return txtUsername;
	}

}
