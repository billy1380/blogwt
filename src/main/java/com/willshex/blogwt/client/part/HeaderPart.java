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
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
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
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.helper.PermissionHelper;
import com.willshex.blogwt.shared.api.user.call.LoginRequest;
import com.willshex.blogwt.shared.api.user.call.LoginResponse;
import com.willshex.blogwt.shared.api.user.call.LogoutRequest;
import com.willshex.blogwt.shared.api.user.call.LogoutResponse;
import com.willshex.blogwt.shared.api.user.call.event.LoginEventHandler;
import com.willshex.blogwt.shared.api.user.call.event.LogoutEventHandler;
import com.willshex.gson.json.service.shared.StatusType;

public class HeaderPart extends Composite implements LoginEventHandler,
		LogoutEventHandler, NavigationChangedEventHandler, ClickHandler {

	private static HeaderPartUiBinder uiBinder = GWT
			.create(HeaderPartUiBinder.class);

	interface HeaderPartUiBinder extends UiBinder<Widget, HeaderPart> {}

	interface HeaderTemplates extends SafeHtmlTemplates {
		HeaderTemplates INSTANCE = GWT.create(HeaderTemplates.class);

		@Template("<a href=\"{0}\">{1}</a>")
		SafeHtml item (SafeUri href, String title);

		@Template("<a href=\"{0}\"><span class=\"glyphicon glyphicon-{1}\"></span> {2}</a>")
		SafeHtml glyphItem (SafeUri href, String glyphName, String title);

		@Template("<a href=\"{0}\"><span>{1}</span><img class=\"img-circle\" src=\"{2}\" /></a>")
		SafeHtml imageItem (SafeUri href, String src, String title);
	}

	@UiField Element elName;
	@UiField Element elNavLeft;
	@UiField Element elNavRight;

	@UiField AnchorElement btnHome;
	private Map<String, Element> items;

	@UiField Element elAdmin;
	@UiField Anchor btnAdmin;
	@UiField Element elAdminDropdown;

	@UiField Element elPages;
	@UiField Element elProperties;
	@UiField Element elUsers;

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
		SafeUri href;
		if ((pages = PageController.get().getHeaderPages()) != null) {
			for (Page page : pages) {
				if (page.parent != null) continue;

				if (page.priority != null && page.priority.floatValue() == 0.0f) {
					btnHome.setHref("#!" + page.slug);
					foundBrandPage = true;
				} else {
					href = UriUtils.fromString("#!" + page.slug);
					addItem(elNavLeft,
							HeaderTemplates.INSTANCE.item(href, page.title),
							href);
				}
			}
		}

		if (foundBrandPage) {
			href = PageType.PostsPageType.asHref();
			addItem(elNavLeft, HeaderTemplates.INSTANCE.item(href, "Blog"),
					href);
		} else {
			btnHome.setHref(PageType.PostsPageType.asHref());
		}

		ensureItems().put(PageType.PagesPageType.asTargetHistoryToken(),
				elPages);
		ensureItems().put(PageType.PropertiesPageType.asTargetHistoryToken(),
				elProperties);
		ensureItems().put(PageType.UsersPageType.asTargetHistoryToken(),
				elUsers);

		elAdmin.removeFromParent();
	}

	private void addItem (Element parent, SafeHtml item, SafeUri href) {
		String key = href.asString().replaceFirst("#", "");
		Element element = ensureItems().get(key);

		if (element == null) {
			element = Document.get().createLIElement();
			element.setInnerSafeHtml(item);
			parent.appendChild(element);
			ensureItems().put(key, element);
		}
	}

	private void removeItem (SafeUri href) {
		String key = href.asString().replaceFirst("#", "");
		Element element = ensureItems().get(key);
		if (element != null) {
			element.removeFromParent();
			ensureItems().remove(key);
		}
	}

	private void activateItem (String page, boolean active) {
		Element element = ensureItems().get(page);

		if (element != null) {
			if (active && !element.hasClassName("active")) {
				element.addClassName("active");
			} else if (!active && element.hasClassName("active")) {
				element.removeClassName("active");
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
		registration.add(RootPanel.get().addDomHandler(this,
				ClickEvent.getType()));

		Session session = SessionController.get().session();
		if (session != null && session.user != null) {
			setLoggedInUser(session.user);
		} else {
			configureNavBar(false);
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

		configureNavBar(true);
	}

	private void configureNavBar (boolean login) {
		removeItem(login ? PageType.LoginPageType.asHref()
				: PageType.LogoutPageType.asHref());
		SafeUri href = login ? PageType.LogoutPageType.asHref()
				: PageType.LoginPageType.asHref();
		addItem(elNavRight, HeaderTemplates.INSTANCE.glyphItem(href,
				login ? "log-out" : "log-in", login ? "Sign Out" : "Sign In"),
				href);

		addAdminNav();
	}

	private void addAdminNav () {
		Permission managePages = PermissionHelper
				.create(PermissionHelper.MANAGE_PAGES);
		Permission manageProperties = PermissionHelper
				.create(PermissionHelper.MANAGE_PERMISSIONS);
		Permission manageUsers = PermissionHelper
				.create(PermissionHelper.MANAGE_POSTS);

		boolean addAdmin = false;

		if (SessionController.get().isAdmin()) {
			addAdmin = true;
		} else {
			elAdminDropdown.removeAllChildren();
		}

		if (addAdmin || SessionController.get().isAuthorised(managePages)) {
			addAdmin = true;
			elAdminDropdown.appendChild(elPages);
		} else {
			elPages.removeFromParent();
		}

		if (addAdmin || SessionController.get().isAuthorised(manageProperties)) {
			addAdmin = true;
			elAdminDropdown.appendChild(elProperties);
		} else {
			elProperties.removeFromParent();
		}

		if (addAdmin || SessionController.get().isAuthorised(manageUsers)) {
			addAdmin = true;
			elAdminDropdown.appendChild(elUsers);
		} else {
			elUsers.removeFromParent();
		}

		if (addAdmin) {
			elNavLeft.appendChild(elAdmin);
		} else {
			elAdmin.removeFromParent();
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.user.call.event.LogoutEventHandler#
	 * logoutSuccess(com.willshex.blogwt.shared.api.user.call.LogoutRequest,
	 * com.willshex.blogwt.shared.api.user.call.LogoutResponse) */
	@Override
	public void logoutSuccess (LogoutRequest input, LogoutResponse output) {
		configureNavBar(false);
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

	@UiHandler("btnAdmin")
	void onAdminClicked (ClickEvent ce) {
		boolean isOpen = elAdmin.hasClassName("open");

		if (isOpen) {
			elAdmin.removeClassName("open");
		} else {
			elAdmin.addClassName("open");
		}

		ce.getNativeEvent().stopPropagation();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event
	 * .dom.client.ClickEvent) */
	@Override
	public void onClick (ClickEvent event) {
		boolean isOpen = elAdmin.hasClassName("open");
		if (isOpen) {
			elAdmin.removeClassName("open");
		}
	}
}
