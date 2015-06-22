//
//  PermissionsPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PermissionsPage extends Page {

	private static PermissionsPageUiBinder uiBinder = GWT
			.create(PermissionsPageUiBinder.class);

	interface PermissionsPageUiBinder extends UiBinder<Widget, PermissionsPage> {}

	public PermissionsPage () {
		super(PageType.PermissionsPageType);
		initWidget(uiBinder.createAndBindUi(this));
	}

}
