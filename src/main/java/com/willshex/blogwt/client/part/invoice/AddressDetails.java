//  
//  AddressDetails.java
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
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.oracle.invoice.AddressSuggestOracle;
import com.willshex.blogwt.client.part.BootstrapGwtSuggestBox;

/**
 * @author billy1380
 * 
 */
public class AddressDetails extends Composite {

	private static AddressDetailsUiBinder uiBinder = GWT
			.create(AddressDetailsUiBinder.class);

	@UiField public TextBox txtNameOrNumber;
	@UiField public TextBox txtFirstLine;
	@UiField public TextBox txtSecondLine;
	@UiField public TextBox txtLocality;
	@UiField public TextBox txtCity;
	@UiField public TextBox txtState;
	@UiField public TextBox txtCountry;
	@UiField(provided = true) public SuggestBox txtPostalCode = new SuggestBox(
			new AddressSuggestOracle());

	interface AddressDetailsUiBinder extends UiBinder<Widget, AddressDetails> {}

	public AddressDetails () {
		initWidget(uiBinder.createAndBindUi(this));

		BootstrapGwtSuggestBox.INSTANCE.styles().ensureInjected();

		txtNameOrNumber.getElement().setAttribute("placeholder",
				"Name or number");
		txtNameOrNumber.getElement().setAttribute("autofocus", "");
		txtFirstLine.getElement().setAttribute("placeholder", "First line");
		txtSecondLine.getElement().setAttribute("placeholder", "Second line");
		txtLocality.getElement().setAttribute("placeholder", "Locality");
		txtCity.getElement().setAttribute("placeholder", "City");
		txtState.getElement().setAttribute("placeholder", "State/County");
		txtCountry.getElement().setAttribute("placeholder", "Country");
		txtPostalCode.getElement().setAttribute("placeholder",
				"Zip/Postal code");
	}

}
