//  
//  CustomerDetails.java
//  quickinvoice
//
//  Created by billy1380 on Sep 21, 2011.
//  Copyrights © 2011 Spacehopper Studios Ltd. All rights reserved.
//
package com.willshex.blogwt.client.part.invoice;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author billy1380
 * 
 */
public class CustomerDetails extends Composite {

	private static CustomerDetailsUiBinder uiBinder = GWT.create(CustomerDetailsUiBinder.class);

	interface CustomerDetailsUiBinder extends UiBinder<Widget, CustomerDetails> {}

	@UiField public TextBox txtName;
	@UiField public TextBox txtCode;
	@UiField public TextArea txtDescription;
	@UiField public TextBox txtEmail;
	@UiField public TextBox txtPhone;

	public CustomerDetails() {
		initWidget(uiBinder.createAndBindUi(this));

		txtName.getElement().setAttribute("placeholder", "Name");
		txtName.getElement().setAttribute("autofocus", "");
		txtCode.getElement().setAttribute("placeholder", "Short code");
		txtEmail.getElement().setAttribute("placeholder", "Email");
		txtPhone.getElement().setAttribute("placeholder", "Phone");
		txtDescription.getElement().setAttribute("placeholder", "Description");
	}

}
