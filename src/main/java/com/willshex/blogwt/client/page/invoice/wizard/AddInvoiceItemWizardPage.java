//
//  AddInvoiceItemWizardPage.java
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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.willshex.blogwt.client.helper.invoice.UiHelper;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.shared.api.datatype.invoice.Item;

/**
 * @author billy1380
 * 
 */
public class AddInvoiceItemWizardPage extends Composite
		implements WizardPage<Item> {

	@UiField TextBox txtName;
	@UiField DateBox dbxDate;
	@UiField TextArea txtDescription;
	@UiField TextBox txtPrice;
	@UiField TextBox txtQuantity;
	@UiField ListBox cboUnit;

	private static AddInvoiceItemWizardPageUiBinder uiBinder = GWT
			.create(AddInvoiceItemWizardPageUiBinder.class);

	interface AddInvoiceItemWizardPageUiBinder
			extends UiBinder<Widget, AddInvoiceItemWizardPage> {}

	public AddInvoiceItemWizardPage () {
		initWidget(uiBinder.createAndBindUi(this));

		txtName.getElement().setAttribute("placeholder", "Name");
		txtName.getElement().setAttribute("autofocus", "");

		dbxDate.getElement().setAttribute("placeholder", "Date");
		dbxDate.setFormat(
				new DefaultFormat(DateTimeFormat.getFormat("dd MMMM yyyy")));
		dbxDate.setValue(new Date());

		txtDescription.getElement().setAttribute("placeholder", "Description");
		txtPrice.getElement().setAttribute("placeholder", "Price");
		txtQuantity.getElement().setAttribute("placeholder", "Quantity");

		UiHelper.addUnits(cboUnit);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#getData() */
	@Override
	public Item getData () {
		Item item = new Item();

		item.name = (String) txtName.getValue();
		item.date = dbxDate.getValue();
		item.description = txtDescription.getText();
		item.quantity = Float.parseFloat(txtQuantity.getText());
		item.unit = cboUnit.getSelectedValue();
		item.price = Integer.valueOf(
				(int) (Float.parseFloat(txtPrice.getValue()) * 100.0f));

		return item;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#getPageTitle() */
	@Override
	public String getPageTitle () {
		return "Items";
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#isRepeatable() */
	@Override
	public boolean isRepeatable () {
		return true;
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
		return new AddInvoiceItemWizardPage();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.wizard.WizardPage#setData(java.lang.Object) */
	@Override
	public void setData (Item data) {}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getPageDescription() */
	@Override
	public String getPageDescription () {
		return null;
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
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getAutoFocusField() */
	@Override
	public Focusable getAutoFocusField () {
		return null;
	}
}
