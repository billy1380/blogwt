//
//  EditResourcePage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.Page;

/**
 * @author billy1380
 *
 */
public class EditResourcePage extends Page {

	private static EditResourcePageUiBinder uiBinder = GWT
			.create(EditResourcePageUiBinder.class);

	interface EditResourcePageUiBinder extends
			UiBinder<Widget, EditResourcePage> {}

	public EditResourcePage () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
