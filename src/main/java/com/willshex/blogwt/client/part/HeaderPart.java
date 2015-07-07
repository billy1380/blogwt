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
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
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
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.helper.UserHelper;
import com.willshex.blogwt.shared.api.user.call.LoginRequest;
import com.willshex.blogwt.shared.api.user.call.LoginResponse;
import com.willshex.blogwt.shared.api.user.call.LogoutRequest;
import com.willshex.blogwt.shared.api.user.call.LogoutResponse;
import com.willshex.blogwt.shared.api.user.call.event.LoginEventHandler;
import com.willshex.blogwt.shared.api.user.call.event.LogoutEventHandler;
import com.willshex.gson.json.service.shared.StatusType;

public class HeaderPart extends Composite implements LoginEventHandler,
		LogoutEventHandler, NavigationChangedEventHandler {

	private List<HandlerRegistration> registration;

	@UiField Element elName;
	@UiField InlineHyperlink btnAccount;
	@UiField InlineHyperlink btnSignInOut;
	@UiField AnchorElement btnPosts;
	@UiField AnchorElement btnPages;
	@UiField AnchorElement btnProperties;
	@UiField AnchorElement btnUsers;
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

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
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
		registration.add(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));

		Session session = SessionController.get().session();
		if (session != null && session.user != null) {
			setLoggedInUser(session.user);
		} else {
			configure(false);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onDetach() */
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
		imgAvatar.setSrc(user.avatar + "?s=" + UserHelper.AVATAR_HEADER_SIZE
				+ "&default=retro");
		spnUserName.setInnerText(user.forename + " " + user.surname);
		configure(true);
	}

	private void configure (boolean login) {
		btnAccount.setVisible(login);
		Display d = login ? Display.BLOCK : Display.NONE;
		btnPages.getStyle().setDisplay(d);
		btnProperties.getStyle().setDisplay(d);
		btnUsers.getStyle().setDisplay(d);
		btnSignInOut
				.getElement()
				.setInnerHTML(
						login ? "<span class=\"glyphicon glyphicon-log-out\"></span> Sign Out"
								: "<span class=\"glyphicon glyphicon-log-in\"></span> Sign In");
		btnSignInOut.setTargetHistoryToken(login ? PageType.LogoutPageType
				.asTargetHistoryToken() : PageType.LoginPageType
				.asTargetHistoryToken());
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.user.call.event.LogoutEventHandler#
	 * logoutSuccess(com.willshex.blogwt.shared.api.user.call.LogoutRequest,
	 * com.willshex.blogwt.shared.api.user.call.LogoutResponse) */
	@Override
	public void logoutSuccess (LogoutRequest input, LogoutResponse output) {
		configure(false);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.user.call.event.LogoutEventHandler#
	 * logoutFailure(com.willshex.blogwt.shared.api.user.call.LogoutRequest,
	 * java.lang.Throwable) */
	@Override
	public void logoutFailure (LogoutRequest input, Throwable caught) {
		GWT.log("logoutFailure - input:"
				+ (input == null ? null : input.toString()), caught);
	}

	@Override
	public void loginSuccess (LoginRequest input, LoginResponse output) {
		if (output.status == StatusType.StatusTypeSuccess
				&& output.session != null && output.session.user != null) {
			setLoggedInUser(output.session.user);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.LoginEventHandler#loginFailure
	 * (com.willshex.blogwt.shared.api.user.call.LoginRequest,
	 * java.lang.Throwable) */
	@Override
	public void loginFailure (LoginRequest input, Throwable caught) {
		GWT.log("loginFailure - input:"
				+ (input == null ? null : input.toString()), caught);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.event.NavigationChangedEventHandler#
	 * navigationChanged
	 * (com.willshex.blogwt.client.controller.NavigationController.Stack,
	 * com.willshex.blogwt.client.controller.NavigationController.Stack) */
	@Override
	public void navigationChanged (Stack previous, Stack current) {
		btnAccount.getElement().getParentElement().removeClassName("active");
		btnPosts.getParentElement().removeClassName("active");
		btnPages.getParentElement().removeClassName("active");
		btnProperties.getParentElement().removeClassName("active");
		btnUsers.getParentElement().removeClassName("active");
		btnSignInOut.getElement().getParentElement().removeClassName("active");

		PageType p = PageType.fromString(current.getPage());

		// this causes the blog heading to be selected by default
		if (p == null) {
			p = PageType.PostsPageType;
		}

		switch (p) {
		case ChangeDetailsPageType:
			btnAccount.getElement().getParentElement().addClassName("active");
			break;
		case PagesPageType:
			btnPages.getParentElement().addClassName("active");
			break;
		case UsersPageType:
			btnUsers.getParentElement().addClassName("active");
			break;
		case LoginPageType:
			btnSignInOut.getElement().getParentElement().addClassName("active");
			break;
		case PostsPageType:
			btnPosts.getParentElement().addClassName("active");
			break;
		case PropertiesPageType:
			btnProperties.getParentElement().addClassName("active");
			break;
		default:
			break;
		}
	}
}
