//
//  BuildBidDetailPage.java
//  bidly
//
//  Created by William Shakour (billy1380) on 28 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dealer.buildbids.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.Page;

/**
 * @author William Shakour (billy1380)
 *
 */
public class BuildBidDetailPage extends Page {

	private static BuildBidDetailPageUiBinder uiBinder = GWT
			.create(BuildBidDetailPageUiBinder.class);

	interface BuildBidDetailPageUiBinder
			extends UiBinder<Widget, BuildBidDetailPage> {}

	
	public BuildBidDetailPage () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
