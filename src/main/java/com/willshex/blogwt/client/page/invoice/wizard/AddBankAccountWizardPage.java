//
//  AddBankAccountWizardPage.java
//  reckoning
//
//  Created by William Shakour (billy1380) on 26 Apr 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.page.invoice.wizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.shared.api.datatype.invoice.BankAccount;

/**
 * @author William Shakour (billy1380)
 *
 */
public class AddBankAccountWizardPage extends Composite
		implements WizardPage<BankAccount> {

	private static AddBankAccountWizardPageUiBinder uiBinder = GWT
			.create(AddBankAccountWizardPageUiBinder.class);

	interface AddBankAccountWizardPageUiBinder
			extends UiBinder<Widget, AddBankAccountWizardPage> {}

	@UiField public TextBox txtName;
	@UiField public TextBox txtBankName;
	@UiField public TextBox txtAccountNumber;
	@UiField public TextBox txtSortCode;

	public AddBankAccountWizardPage () {
		initWidget(uiBinder.createAndBindUi(this));

		txtName.getElement().setAttribute("placeholder", "Name");
		txtName.getElement().setAttribute("autofocus", "");
		txtBankName.getElement().setAttribute("placeholder", "Bank name");
		txtAccountNumber.getElement().setAttribute("placeholder",
				"Account number");
		txtSortCode.getElement().setAttribute("placeholder", "Sort code");
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#isRepeatable() */
	@Override
	public boolean isRepeatable () {
		return false;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#getData() */
	@Override
	public BankAccount getData () {
		BankAccount bankAccount = new BankAccount();

		bankAccount.name = txtName.getValue();
		bankAccount.bankName = txtBankName.getValue();
		bankAccount.accountNumber = txtAccountNumber.getValue();
		bankAccount.sortCode = txtSortCode.getValue();

		return bankAccount;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#getPageTitle() */
	@Override
	public String getPageTitle () {
		return "Bank Account";
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#getBody() */
	@Override
	public Widget getBody () {
		return this;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#another() */
	@Override
	public WizardPage<?> another () {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#isValid() */
	@Override
	public boolean isValid () {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.wizard.WizardPage#setData(java.lang.Object)
	 */
	@Override
	public void setData (BankAccount data) {}

	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getPageDescription()
	 */
	@Override
	public String getPageDescription () {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getAutoFocusField()
	 */
	@Override
	public Focusable getAutoFocusField () {
		return null;
	}

}
