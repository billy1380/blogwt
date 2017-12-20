//
//  ChangeDealerDetailsPage.java
//  bidly
//
//  Created by William Shakour (billy1380) on 20 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin.bidly.changedealerdetails;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.Page;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ChangeDealerDetailsPage extends Page {

	private static ChangeDealerDetailsPageUiBinder uiBinder = GWT
			.create(ChangeDealerDetailsPageUiBinder.class);

	interface ChangeDealerDetailsPageUiBinder
			extends UiBinder<Widget, ChangeDealerDetailsPage> {}

	public ChangeDealerDetailsPage () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
