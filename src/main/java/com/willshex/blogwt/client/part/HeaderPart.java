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
import com.google.gwt.dom.client.ImageElement;
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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.controller.PageController;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.LoginRequest;
import com.willshex.blogwt.shared.api.user.call.LoginResponse;
import com.willshex.blogwt.shared.api.user.call.LogoutRequest;
import com.willshex.blogwt.shared.api.user.call.LogoutResponse;
import com.willshex.blogwt.shared.api.user.call.event.ChangeUserDetailsEventHandler;
import com.willshex.blogwt.shared.api.user.call.event.LoginEventHandler;
import com.willshex.blogwt.shared.api.user.call.event.LogoutEventHandler;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.json.service.shared.StatusType;

public class HeaderPart extends Composite implements LoginEventHandler,
		LogoutEventHandler, NavigationChangedEventHandler, ClickHandler,
		ChangeUserDetailsEventHandler {

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
	@UiField Element elAdmin;
	@UiField Anchor btnAdmin;
	@UiField Element elAdminDropdown;
	@UiField Element elPages;
	@UiField Element elProperties;
	@UiField Element elResources;
	@UiField Element elRoles;
	@UiField Element elPermissions;
	@UiField Element elUsers;
	@UiField Element elAccount;
	@UiField InlineHyperlink btnAccount;
	@UiField Element elUserName;
	@UiField ImageElement imgAvatar;
	@UiField Image imgLogo;
	@UiField CollapseButton btnNavExpand;
	@UiField HTMLPanel pnlNav;

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

		String logoImage;
		if ((logoImage = PropertyController.get().stringProperty(
				PropertyHelper.SMALL_LOGO_URL)) != null
				&& !PropertyHelper.NONE_VALUE.equalsIgnoreCase(logoImage)) {
			imgLogo.setUrl(logoImage);

			String title = PropertyController.get().title();

			imgLogo.setTitle(title);
			imgLogo.setAltText(title);
		}

		btnNavExpand.setTarget(pnlNav);
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
			href = PageTypeHelper.asHref(PageType.PostsPageType);
			addItem(elNavLeft, HeaderTemplates.INSTANCE.item(href, "Blog"),
					href);
		} else {
			btnHome.setHref(PageTypeHelper.asHref(PageType.PostsPageType));
		}

		ensureItems().put(PageType.PagesPageType.asTargetHistoryToken(),
				elPages);
		ensureItems().put(PageType.PropertiesPageType.asTargetHistoryToken(),
				elProperties);
		ensureItems().put(PageType.UsersPageType.asTargetHistoryToken(),
				elUsers);
		ensureItems().put(PageType.RolesPageType.asTargetHistoryToken(),
				elRoles);
		ensureItems().put(PageType.PermissionsPageType.asTargetHistoryToken(),
				elPermissions);
		ensureItems().put(PageType.ResourcesPageType.asTargetHistoryToken(),
				elResources);

		elAdmin.removeFromParent();
		elAccount.removeFromParent();
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
		registration
				.add(DefaultEventBus.get().addHandlerToSource(
						ChangeUserDetailsEventHandler.TYPE,
						UserController.get(), this));

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
		showUserDetails(user);

		configureNavBar(true);
	}

	private void configureNavBar (boolean login) {
		if (login) {
			elNavRight.appendChild(elAccount);
		} else {
			elAccount.removeFromParent();
		}

		removeItem(PageTypeHelper.asHref(login ? PageType.LoginPageType
				: PageType.LogoutPageType));
		SafeUri href = PageTypeHelper.asHref(login ? PageType.LogoutPageType
				: PageType.LoginPageType);

		if (login
				|| PropertyController.get().booleanProperty(
						PropertyHelper.SHOW_SIGN_IN, true)) {
			addItem(elNavRight, HeaderTemplates.INSTANCE.glyphItem(href,
					login ? "log-out" : "log-in", login ? "Sign Out"
							: "Sign In"), href);
		}

		addAdminNav(login);
	}

	private void addAdminNav (boolean login) {
		boolean addAdmin = false, isAdmin = false;

		if (login) {
			Permission managePages = PermissionHelper
					.create(PermissionHelper.MANAGE_PAGES);
			Permission managePermissions = PermissionHelper
					.create(PermissionHelper.MANAGE_PERMISSIONS);
			Permission manageRoles = PermissionHelper
					.create(PermissionHelper.MANAGE_ROLES);
			Permission manageUsers = PermissionHelper
					.create(PermissionHelper.MANAGE_USERS);
			Permission manageResources = PermissionHelper
					.create(PermissionHelper.MANAGE_RESOURCES);

			if (SessionController.get().isAdmin()) {
				addAdmin = true;
				isAdmin = true;
				elAdminDropdown.appendChild(elProperties);
			} else {
				elAdminDropdown.removeAllChildren();
			}

			if (isAdmin || SessionController.get().isAuthorised(managePages)) {
				addAdmin = true;
				elAdminDropdown.appendChild(elPages);
			} else {
				elPages.removeFromParent();
			}

			if (isAdmin || SessionController.get().isAuthorised(manageUsers)) {
				addAdmin = true;
				elAdminDropdown.appendChild(elUsers);
			} else {
				elUsers.removeFromParent();
			}

			if (isAdmin || SessionController.get().isAuthorised(manageRoles)) {
				addAdmin = true;
				elAdminDropdown.appendChild(elRoles);
			} else {
				elRoles.removeFromParent();
			}

			if (isAdmin
					|| SessionController.get().isAuthorised(managePermissions)) {
				addAdmin = true;
				elAdminDropdown.appendChild(elPermissions);
			} else {
				elPermissions.removeFromParent();
			}

			if (isAdmin
					|| SessionController.get().isAuthorised(manageResources)) {
				addAdmin = true;
				elAdminDropdown.appendChild(elResources);
			} else {
				elResources.removeFromParent();
			}
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

		btnNavExpand.hide();
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

	private void showUserDetails (User user) {
		imgAvatar.setAlt(user.username);
		imgAvatar.setSrc(user.avatar + "?s=" + UserHelper.AVATAR_HEADER_SIZE
				+ "&default=retro");
		elUserName.setInnerText(user.forename + " " + user.surname);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.ChangeUserDetailsEventHandler
	 * #changeUserDetailsSuccess(com.willshex.blogwt.shared.api.user.call.
	 * ChangeUserDetailsRequest,
	 * com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsResponse) */
	@Override
	public void changeUserDetailsSuccess (ChangeUserDetailsRequest input,
			ChangeUserDetailsResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			User user = SessionController.get().user();

			if (user != null && user.id.equals(output.user.id)) {
				showUserDetails(output.user);
			}
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.ChangeUserDetailsEventHandler
	 * #changeUserDetailsFailure(com.willshex.blogwt.shared.api.user.call.
	 * ChangeUserDetailsRequest, java.lang.Throwable) */
	@Override
	public void changeUserDetailsFailure (ChangeUserDetailsRequest input,
			Throwable caught) {}
}
