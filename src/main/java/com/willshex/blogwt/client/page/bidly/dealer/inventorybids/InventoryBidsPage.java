//
//  InventoryBidsPage.java
//  bidly
//
//  Created by William Shakour (billy1380) on 20 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dealer.inventorybids;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.Page;

/**
 * @author William Shakour (billy1380)
 *
 */
public class InventoryBidsPage extends Page {

	private static InventoryBidsPageUiBinder uiBinder = GWT
			.create(InventoryBidsPageUiBinder.class);

	interface InventoryBidsPageUiBinder
			extends UiBinder<Widget, InventoryBidsPage> {}

	public InventoryBidsPage () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
