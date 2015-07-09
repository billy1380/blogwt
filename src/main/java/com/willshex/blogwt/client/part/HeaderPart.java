//
//  HeaderPart.java
//  blogwt
//
//  Created by billy1380 on 31 Jul 2013.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.controller.PageController;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.shared.api.datatype.Page;
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
		LogoutEventHandler, NavigationChangedEventHandler {

	private static HeaderPartUiBinder uiBinder = GWT
			.create(HeaderPartUiBinder.class);

	interface HeaderPartUiBinder extends UiBinder<Widget, HeaderPart> {}

	interface HeaderTemplates extends SafeHtmlTemplates {
		HeaderTemplates INSTANCE = GWT.create(HeaderTemplates.class);

		@Template("<a href=\"{0}\">{1}</a>")
		SafeHtml item (SafeUri href, String title);

		@Template("<a href=\"{0}\"><span class=\"glyphicon glyphicon-{1}\"></span>{2}</a>")
		SafeHtml glyphItem (SafeUri href, String glyphName, String title);

		@Template("<a href=\"{0}\"><span>{1}</span><img class=\"img-circle\" src=\"{2}\" /></a>")
		SafeHtml imageItem (SafeUri href, String src, String title);
	}

	@UiField Element elName;
	@UiField Element elNavLeft;
	@UiField Element elNavRight;

	@UiField AnchorElement btnHome;
	private Map<String, Element> items;

	private Map<String, Element> ensureItems () {
		if (items == null) {
			items = new HashMap<String, Element>();
		}

		return items;
	}

	private List<HandlerRegistration> registration;

	public HeaderPart () {
		initWidget(uiBinder.createAndBindUi(this));

		Resources.RES.styles().ensureInjected();

		setupNavBarPages();
	}

	private void setupNavBarPages () {
		elName.setInnerText(PropertyController.get().title());

		boolean foundBrandPage = false;
		List<Page> pages;
		if ((pages = PageController.get().getHeaderPages()) != null) {
			for (Page page : pages) {
				if (page.parent != null) continue;

				if (page.priority != null && page.priority.floatValue() == 0.0f) {
					btnHome.setHref("#!" + page.slug);
					foundBrandPage = true;
				} else {
					addItem(elNavLeft, page.title,
							UriUtils.fromString("#!" + page.slug));
				}
			}
		}

		if (foundBrandPage) {
			addItem(elNavLeft, "Blog", PageType.PostsPageType.asHref());
		} else {
			btnHome.setHref(PageType.PostsPageType.asHref());
		}
	}

	private void addItem (Element parent, String title, SafeUri href) {
		Element element = Document.get().createLIElement();
		element.setInnerSafeHtml(HeaderTemplates.INSTANCE.item(href, title));
		parent.appendChild(element);
		ensureItems().put(href.asString().replaceFirst("#", ""), element);
	}

	private void activateItem (String page, boolean active) {
		Element el = ensureItems().get(page);

		if (el != null) {
			if (active && !el.hasClassName("active")) {
				el.addClassName("active");
			} else if (!active && el.hasClassName("active")) {
				el.removeClassName("active");
			}
		}
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
		//		imgAvatar.setAlt(user.username);
		//		imgAvatar.setSrc(user.avatar + "?s=" + UserHelper.AVATAR_HEADER_SIZE
		//				+ "&default=retro");
		//		spnUserName.setInnerText(user.forename + " " + user.surname);
		configure(true);
	}

	private void configure (boolean login) {
		//		btnAccount.setVisible(login);
		//		Display d = login ? Display.BLOCK : Display.NONE;
		//		btnPages.getStyle().setDisplay(d);
		//		btnProperties.getStyle().setDisplay(d);
		//		btnUsers.getStyle().setDisplay(d);
		//		btnSignInOut
		//				.getElement()
		//				.setInnerHTML(
		//						login ? "<span class=\"glyphicon glyphicon-log-out\"></span> Sign Out"
		//								: "<span class=\"glyphicon glyphicon-log-in\"></span> Sign In");
		//		btnSignInOut.setTargetHistoryToken(login ? PageType.LogoutPageType
		//				.asTargetHistoryToken() : PageType.LoginPageType
		//				.asTargetHistoryToken());
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
		if (previous != null) {
			activateItem(previous.getPage(), false);
		}

		activateItem(current.getPage(), true);
	}
}
