//
//  ChangeAccessPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 1 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author billy1380
 *
 */
public class ChangeAccessPage extends Page implements
		NavigationChangedEventHandler {

	private static ChangeAccessPageUiBinder uiBinder = GWT
			.create(ChangeAccessPageUiBinder.class);

	interface ChangeAccessPageUiBinder extends
			UiBinder<Widget, ChangeAccessPage> {}

	public ChangeAccessPage () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));
	}

	@Override
	public void navigationChanged (Stack previous, Stack current) {
		// TODO Auto-generated method stub

	}

}
