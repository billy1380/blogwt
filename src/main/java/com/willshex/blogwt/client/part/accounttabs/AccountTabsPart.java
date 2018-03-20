//
//  AccountTabsPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Mar 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.accounttabs;

import static com.willshex.blogwt.client.helper.UiHelper.activateItem;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.gwt.RegisteringComposite;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author William Shakour (billy1380)
 *
 */
public class AccountTabsPart extends RegisteringComposite
		implements NavigationChangedEventHandler {

	private static AccountTabsPartUiBinder uiBinder = GWT
			.create(AccountTabsPartUiBinder.class);

	interface AccountTabsPartUiBinder
			extends UiBinder<Widget, AccountTabsPart> {}

	private static AccountTabsPart one = null;

	public static AccountTabsPart get () {
		if (one == null) {
			one = new AccountTabsPart();
		}

		return one;
	}

	@UiField Element elDetailsPage;
	@UiField Element elPasswordPage;
	@UiField Element elAccessPage;
	@UiField Element elNotificationSettingsPage;
	@UiField Element elNotificationsPage;

	private Map<String, Element> items = new HashMap<>();

	public AccountTabsPart () {
		initWidget(uiBinder.createAndBindUi(this));

		items.put(PageType.ChangeDetailsPageType.asTargetHistoryToken(),
				elDetailsPage);
		items.put(PageType.ChangePasswordPageType.asTargetHistoryToken(),
				elPasswordPage);
		items.put(PageType.ChangeAccessPageType.asTargetHistoryToken(),
				elAccessPage);
		//		items.put(PageType.NottificationSettingsPage.asTargetHistoryToken(),
		//				elNotificationSettingsPage);
		//		items.put(
		//				PageType.NotificationsPage.asTargetHistoryToken(),
		//				elNotificationsPage);

	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.event.NavigationChangedEventHandler#
	 * navigationChanged(com.willshex.blogwt.shared.page.Stack,
	 * com.willshex.blogwt.shared.page.Stack) */
	@Override
	public void navigationChanged (Stack previous, Stack current) {
		for (Element e : items.values()) {
			activateItem(e, false);
		}

		activateItem(current.getPage(), true, this::getItem);
	}

	private Element getItem (String key) {
		return items == null ? null : items.get(key);
	}

}
