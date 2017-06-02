//
//  AddInvoiceWizardPage.java
//  quickinvoice
//
//  Created by billy1380 on 1 Aug 2013.
//  Copyright © 2013 Spacehopper Studios Ltd. All rights reserved.
//

package com.willshex.blogwt.client.page.invoice.wizard;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.willshex.blogwt.client.helper.invoice.UiHelper;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.shared.api.datatype.invoice.Currency;
import com.willshex.blogwt.shared.api.datatype.invoice.Customer;
import com.willshex.blogwt.shared.api.datatype.invoice.Invoice;

/**
 * @author billy1380
 * 
 */
public class AddInvoiceWizardPage extends Composite
		implements WizardPage<Invoice> {

	private static AddInvoiceWizardPageUiBinder uiBinder = GWT
			.create(AddInvoiceWizardPageUiBinder.class);

	@UiField public ListBox cboCustomer;
	@UiField public ListBox cboCurrency;
	@UiField public DateBox dbxDate;
	@UiField public TextBox txtTax;
	@UiField public TextBox txtDeposit;

	interface AddInvoiceWizardPageUiBinder
			extends UiBinder<Widget, AddInvoiceWizardPage> {}

	public AddInvoiceWizardPage () {
		initWidget(uiBinder.createAndBindUi(this));

		dbxDate.getElement().setAttribute("placeholder", "Date");
		dbxDate.setFormat(
				new DefaultFormat(DateTimeFormat.getFormat("dd MMMM yyyy")));
		dbxDate.setValue(new Date());

		txtTax.getElement().setAttribute("placeholder",
				"% Tax (added to invoice total)");
		txtDeposit.getElement().setAttribute("placeholder", "Deposit");

		UiHelper.addCurrenciesFromCache(cboCurrency);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		UiHelper.addCustomersFromCache(cboCustomer);

		super.onAttach();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#getData() */
	@Override
	public Invoice getData () {
		Invoice invoice = new Invoice();

		invoice.customer = new Customer();
		invoice.customer.code = cboCustomer
				.getValue(cboCustomer.getSelectedIndex());

		invoice.date = dbxDate.getValue();

		invoice.currency = new Currency();
		invoice.currency.code = cboCurrency
				.getValue(cboCurrency.getSelectedIndex());

		invoice.tax = Float.valueOf(txtTax.getText());
		invoice.deposit = new Integer(
				(int) (Float.parseFloat(txtDeposit.getText()) * 100.0f));

		return invoice;
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
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#hasChildren() */
	@Override
	public boolean isRepeatable () {
		return false;
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
	public void setData (Invoice data) {}

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
