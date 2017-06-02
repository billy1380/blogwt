//
//  Vendor.java
//  quickinvoice
//
//  Created by William Shakour (billy1380) on 25 Dec 2014.
//  Copyright © 2014 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.client.page.invoice;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.invoice.VendorController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.utility.JsonUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
public class Vendor extends Page {

	private static VendorUiBinder uiBinder = GWT.create(VendorUiBinder.class);

	interface VendorUiBinder extends UiBinder<Widget, Vendor> {}

	@UiField TextArea txtVendorJson;
	@UiField Image imgLogo;
	@UiField HeadingElement h3VendorName;

	public Vendor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach()
	 */
	@Override
	protected void onAttach() {
		super.onAttach();

		com.willshex.blogwt.shared.api.datatype.invoice.Vendor vendor = VendorController.get().current();

		if (vendor != null) {
			txtVendorJson.setText(JsonUtils.beautifyJson(vendor.toString()));

			imgLogo.setAltText(vendor.logo == null ? vendor.description : vendor.logo.description);
			imgLogo.setUrl(vendor.logo == null ? vendor.code + "/img/logo.png" : vendor.logo.data);
			h3VendorName.setInnerText(vendor.name);
		}
	}
}
