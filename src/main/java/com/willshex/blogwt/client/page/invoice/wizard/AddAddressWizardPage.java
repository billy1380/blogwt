//
//  AddAddressWizardPage.java
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
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.part.invoice.AddressDetails;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.shared.api.datatype.invoice.Address;

/**
 * @author William Shakour (billy1380)
 *
 */
public class AddAddressWizardPage extends Composite
		implements WizardPage<Address> {

	private static AddAddressWizardPageUiBinder uiBinder = GWT
			.create(AddAddressWizardPageUiBinder.class);

	interface AddAddressWizardPageUiBinder
			extends UiBinder<Widget, AddAddressWizardPage> {}

	@UiField AddressDetails addressFields;

	public AddAddressWizardPage () {
		initWidget(uiBinder.createAndBindUi(this));
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
	public Address getData () {
		Address address = new Address();

		address.nameOrNumber = addressFields.txtNameOrNumber.getText();
		address.firstLine = addressFields.txtFirstLine.getText();
		address.secondLine = addressFields.txtSecondLine.getText();
		address.locality = addressFields.txtLocality.getText();
		address.city = addressFields.txtCity.getText();
		address.state = addressFields.txtState.getText();
		address.country = addressFields.txtCountry.getText();
		address.postalCode = addressFields.txtPostalCode.getText();

		return address;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#getPageTitle() */
	@Override
	public String getPageTitle () {
		return "Address";
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
	 * 
	 * @see
	 * com.willshex.blogwt.client.wizard.WizardPage#setData(java.lang.Object) */
	@Override
	public void setData (Address data) {}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getPageDescription() */
	@Override
	public String getPageDescription () {
		return null;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getAutoFocusField() */
	@Override
	public Focusable getAutoFocusField () {
		return null;
	}

}
