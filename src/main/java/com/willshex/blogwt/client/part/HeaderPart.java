//
//  HeaderPart.java
//  blogwt
//
//  Created by billy1380 on 31 Jul 2013.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.LoginRequest;
import com.willshex.blogwt.shared.api.user.call.LoginResponse;
import com.willshex.blogwt.shared.api.user.call.LogoutRequest;
import com.willshex.blogwt.shared.api.user.call.LogoutResponse;
import com.willshex.blogwt.shared.api.user.call.event.LoginEventHandler;
import com.willshex.blogwt.shared.api.user.call.event.LogoutEventHandler;
import com.willshex.gson.json.service.shared.StatusType;

public class HeaderPart extends Composite implements LoginEventHandler,
		LogoutEventHandler {

	private List<HandlerRegistration> registration;
	
	@UiField Element elName;
	@UiField InlineHyperlink btnAccount;
	@UiField InlineHyperlink btnSignInOut;
	@UiField SpanElement spnUserName;

	@UiField ImageElement imgAvatar;

	private static HeaderPartUiBinder uiBinder = GWT
			.create(HeaderPartUiBinder.class);

	interface HeaderPartUiBinder extends UiBinder<Widget, HeaderPart> {}

	public HeaderPart () {
		initWidget(uiBinder.createAndBindUi(this));

		Resources.RES.styles().ensureInjected();

		elName.setInnerText(PropertyController.get().title());
	}

	@UiHandler("btnExpand")
	void onBtnExpandClicked (ClickEvent event) {}

	@Override
	protected void onAttach () {
		super.onAttach();

		if (registration == null) {
			registration = new ArrayList<HandlerRegistration>();
		}

		registration.add(DefaultEventBus.get().addHandlerToSource(
				LoginEventHandler.TYPE, SessionController.get(), this));
		registration.add(DefaultEventBus.get().addHandlerToSource(
				LogoutEventHandler.TYPE, SessionController.get(), this));

		Session session = SessionController.get().session();
		if (session != null && session.user != null) {
			setLoggedInUser(session.user);
		}
	}

	@Override
	protected void onDetach () {
		for (HandlerRegistration handlerRegistration : registration) {
			handlerRegistration.removeHandler();
		}

		super.onDetach();
	}

	/**
	 * @param user
	 */
	private void setLoggedInUser (User user) {
		imgAvatar.setAlt(user.username);
		imgAvatar.setSrc(user.avatar + "?s=20&default=retro");
		spnUserName.setInnerText(user.forename + " " + user.surname);
		btnAccount.setVisible(true);
		btnSignInOut.setText("Sign Out");
		btnSignInOut.setTargetHistoryToken(PageType.LogoutPageType
				.asTargetHistoryToken());
	}

	@Override
	public void logoutSuccess (LogoutRequest input, LogoutResponse output) {
		btnAccount.setVisible(false);
		btnSignInOut.setText("Sign In");
		btnSignInOut.setTargetHistoryToken(PageType.LoginPageType
				.asTargetHistoryToken());
	}

	@Override
	public void logoutFailure (LogoutRequest input, Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loginSuccess (LoginRequest input, LoginResponse output) {
		if (output.status == StatusType.StatusTypeSuccess
				&& output.session != null && output.session.user != null) {
			setLoggedInUser(output.session.user);
		}

	}

	@Override
	public void loginFailure (LoginRequest input, Throwable caught) {
		// TODO Auto-generated method stub

	}

}
