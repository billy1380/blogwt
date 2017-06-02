//  
//  AddCustomerWizardPage.java
//  quickinvoice
//
//  Created by billy1380 on Sep 21, 2011.
//  Copyrights © 2011 Spacehopper Studios Ltd. All rights reserved.
//
package com.willshex.blogwt.client.page.invoice.wizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.part.invoice.CustomerDetails;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.shared.api.datatype.invoice.Customer;

/**
 * @author billy1380
 * 
 */
public class AddCustomerWizardPage extends Composite
		implements WizardPage<Customer> {

	private static final AddCustomerWizardPageUiBinder binder = GWT
			.create(AddCustomerWizardPageUiBinder.class);

	@UiField CustomerDetails customerFields;

	interface AddCustomerWizardPageUiBinder
			extends UiBinder<Widget, AddCustomerWizardPage> {}

	public AddCustomerWizardPage () {
		initWidget(binder.createAndBindUi(this));
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
	public Customer getData () {
		Customer customer = new Customer();

		customer.name = customerFields.txtName.getText();
		customer.code = customerFields.txtCode.getText();
		customer.mainEmail = customerFields.txtEmail.getText();
		customer.phone = customerFields.txtPhone.getText();
		customer.description = customerFields.txtDescription.getText();

		return customer;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#getPageTitle() */
	@Override
	public String getPageTitle () {
		return "Details";
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
	public void setData (Customer data) {}

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
