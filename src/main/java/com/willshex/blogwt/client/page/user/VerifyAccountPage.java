//
//  VerifyAccountPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 6 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.controller.PageController;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.shared.api.user.call.VerifyAccountRequest;
import com.willshex.blogwt.shared.api.user.call.VerifyAccountResponse;
import com.willshex.blogwt.shared.api.user.call.event.VerifyAccountEventHandler;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class VerifyAccountPage extends Page implements
		NavigationChangedEventHandler, VerifyAccountEventHandler {

	private static VerifyAccountPageUiBinder uiBinder = GWT
			.create(VerifyAccountPageUiBinder.class);

	interface VerifyAccountPageUiBinder extends
			UiBinder<Widget, VerifyAccountPage> {}

	@UiField Element elActionCode;
	private Timer goHomeTimer = new Timer() {

		@Override
		public void run () {
			PageTypeHelper.show(PageController.get()
					.homePageTargetHistoryToken());
		}

	};

	public VerifyAccountPage () {
		super(PageType.VerifyAccountPageType);
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));
		register(DefaultEventBus.get().addHandlerToSource(
				VerifyAccountEventHandler.TYPE, UserController.get(), this));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.event.NavigationChangedEventHandler#
	 * navigationChanged
	 * (com.willshex.blogwt.client.controller.NavigationController.Stack,
	 * com.willshex.blogwt.client.controller.NavigationController.Stack) */
	@Override
	public void navigationChanged (Stack previous, Stack current) {
		String action;
		if ((action = current.getAction()) != null && action.length() != 0) {
			elActionCode.setInnerText(action);

			UserController.get().verifyAccount(action);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.VerifyAccountEventHandler
	 * #verifyAccountSuccess
	 * (com.willshex.blogwt.shared.api.user.call.VerifyAccountRequest,
	 * com.willshex.blogwt.shared.api.user.call.VerifyAccountResponse) */
	@Override
	public void verifyAccountSuccess (VerifyAccountRequest input,
			VerifyAccountResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			goHomeTimer.cancel();
			goHomeTimer.schedule(2000);
		} else {
			GWT.log("Could not verify account with error ["
					+ (output.error == null ? "none" : output.error.toString())
					+ "]");
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.VerifyAccountEventHandler
	 * #verifyAccountFailure
	 * (com.willshex.blogwt.shared.api.user.call.VerifyAccountRequest,
	 * java.lang.Throwable) */
	@Override
	public void verifyAccountFailure (VerifyAccountRequest input,
			Throwable caught) {
		GWT.log("verifyAccount", caught);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#reset() */
	@Override
	protected void reset () {
		elActionCode.setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);

		super.reset();
	}

}
