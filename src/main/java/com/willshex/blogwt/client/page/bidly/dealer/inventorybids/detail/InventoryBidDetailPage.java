//
//  InventoryBidDetailPage.java
//  bidly
//
//  Created by William Shakour (billy1380) on 3 Jan 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dealer.inventorybids.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.Page;

/**
 * @author William Shakour (billy1380)
 *
 */
public class InventoryBidDetailPage extends Page {

	private static InventoryBidDetailPageUiBinder uiBinder = GWT
			.create(InventoryBidDetailPageUiBinder.class);

	interface InventoryBidDetailPageUiBinder
			extends UiBinder<Widget, InventoryBidDetailPage> {}

	public InventoryBidDetailPage () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
