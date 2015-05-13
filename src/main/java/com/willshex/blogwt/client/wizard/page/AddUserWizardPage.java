//
//  AddUserWizardPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.wizard.page;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.shared.api.datatype.User;

/**
 * @author William Shakour (billy1380)
 *
 */
public class AddUserWizardPage extends Composite implements WizardPage<User> {

	private static AddUserWizardPageUiBinder uiBinder = GWT
			.create(AddUserWizardPageUiBinder.class);

	interface AddUserWizardPageUiBinder extends
			UiBinder<Widget, AddUserWizardPage> {}

	public AddUserWizardPage () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.wizard.WizardPage#isRepeatable()
	 */
	@Override
	public boolean isRepeatable () {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getData()
	 */
	@Override
	public User getData () {
		return new User();
	}

	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getPageTitle()
	 */
	@Override
	public String getPageTitle () {
		return "Add user";
	}

	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getBody()
	 */
	@Override
	public Widget getBody () {
		return this;
	}

	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.wizard.WizardPage#another()
	 */
	@Override
	public WizardPage<?> another () {
		return new AddUserWizardPage();
	}

	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.wizard.WizardPage#validate()
	 */
	@Override
	public boolean validate () {
		return true;
	}

}
