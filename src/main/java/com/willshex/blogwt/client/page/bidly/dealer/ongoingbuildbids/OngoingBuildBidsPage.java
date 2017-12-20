//
//  OngoingBuildBidsPage.java
//  bidly
//
//  Created by William Shakour (billy1380) on 20 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dealer.ongoingbuildbids;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.Page;

/**
 * @author William Shakour (billy1380)
 *
 */
public class OngoingBuildBidsPage extends Page {

	private static OngoingBuildBidsPageUiBinder uiBinder = GWT
			.create(OngoingBuildBidsPageUiBinder.class);

	interface OngoingBuildBidsPageUiBinder
			extends UiBinder<Widget, OngoingBuildBidsPage> {}

	
	public OngoingBuildBidsPage () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
