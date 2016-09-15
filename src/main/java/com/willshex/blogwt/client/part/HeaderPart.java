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
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.api.user.event.ChangeUserDetailsEventHandler;
import com.willshex.blogwt.client.api.user.event.LoginEventHandler;
import com.willshex.blogwt.client.api.user.event.LogoutEventHandler;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.PageController;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PageTypeHelper;
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
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.gson.web.service.shared.StatusType;

public class HeaderPart extends Composite implements LoginEventHandler,
		LogoutEventHandler, NavigationChangedEventHandler, ClickHandler,
		ChangeUserDetailsEventHandler {

	private static HeaderPartUiBinder uiBinder = GWT
			.create(HeaderPartUiBinder.class);

	interface HeaderPartUiBinder extends UiBinder<Widget, HeaderPart> {}

	interface HeaderTemplates extends SafeHtmlTemplates {
		HeaderTemplates INSTANCE = GWT.create(HeaderTemplates.class);

		@Template("{0} <span class=\"caret\" />")
		SafeHtml openableTitle (String title);

		@Template("<span class=\"glyphicon glyphicon-{0}\"></span> {1}")
		SafeHtml glyphItem (String glyphName, String title);

		@Template("<span>{0}</span><img class=\"img-circle\" src=\"{1}\" />")
		SafeHtml imageItem (String src, String title);
	}

	@UiField Element elName;
	@UiField Element elNavLeft;
	@UiField Element elNavRight;
	@UiField AnchorElement btnHome;
	@UiField Element elAdmin;
	@UiField Anchor btnAdmin;
	@UiField Element elAdminDropdown;
	@UiField Element elPages;
	@UiField Element elPosts;
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
	private Element elOpen;
	private Map<String, Element> items;
	private Map<String, Element> openables;

	private List<HandlerRegistration> registration;

	private HeaderPart () {
		initWidget(uiBinder.createAndBindUi(this));
		Resources.RES.styles().ensureInjected();

		String titleInNavBar = PropertyController.get()
				.stringProperty(PropertyHelper.TITLE_IN_NAVBAR);

		if (titleInNavBar == null
				|| titleInNavBar.contains(PropertyHelper.LOGO_VALUE)) {
			String title = PropertyController.get().title();
			String logoImage = PropertyController.get()
					.stringProperty(PropertyHelper.SMALL_LOGO_URL);

			if (logoImage != null
					&& !PropertyHelper.NONE_VALUE.equalsIgnoreCase(logoImage)) {
				imgLogo.setUrl(logoImage);
			} else {
				imgLogo.setResource(Resources.RES.brand());
			}

			imgLogo.setTitle(title);
			imgLogo.setAltText(title);
		} else {
			imgLogo.getElement().getParentElement().removeFromParent();
		}

		btnNavExpand.setTarget(pnlNav);
		setupNavBarPages();
		btnAdmin.addClickHandler(this);
	}

	private void setupNavBarPages () {
		String titleInNavBar = PropertyController.get()
				.stringProperty(PropertyHelper.TITLE_IN_NAVBAR);

		boolean removedHome = false;
		if (titleInNavBar == null
				|| titleInNavBar.contains(PropertyHelper.TITLE_VALUE)) {
			elName.setInnerText(PropertyController.get().title());
		} else if (titleInNavBar.equals(PropertyHelper.NONE_VALUE)) {
			btnHome.removeFromParent();
			removedHome = true;
		} else {
			elName.removeFromParent();
		}

		boolean foundBrandPage = false, addedBlog = false;
		List<Page> pages;
		SafeUri href;
		Map<Long, Page> possibleParents = new HashMap<Long, Page>();
		if ((pages = PageController.get().getHeaderPages()) != null) {
			for (Page page : pages) {
				if (page.priority != null && page.priority.floatValue() == 0.0f
						&& !removedHome) {
					btnHome.setHref(PageTypeHelper.slugToHref(page.slug));
					foundBrandPage = true;
				} else {
					if (page.priority != null && page.priority.floatValue() > 2
							&& (foundBrandPage || removedHome) && !addedBlog) {
						href = PageTypeHelper.asHref(PageType.PostsPageType);
						addItem(elNavLeft,
								SafeHtmlUtils.fromSafeConstant("Blog"), href);
						addedBlog = true;
					}

					Element el;
					if (page.parent == null) {
						href = PageTypeHelper.slugToHref(page.slug);

						if (possibleParents.get(page.id) == null) {
							addItem(elNavLeft,
									SafeHtmlUtils.fromString(page.title), href);
							possibleParents.put(page.id, page);
						} else {
							possibleParents.put(page.id, page);
							swapTitle(
									PageTypeHelper.slugToTargetHistoryToken(
											"pageid_" + page.id),
									HeaderTemplates.INSTANCE
											.openableTitle(page.title),
									href);
						}
					} else {
						Page parent;
						if ((parent = possibleParents
								.get(page.parent.id)) != null) {
							el = getOpenable(PageTypeHelper
									.slugToTargetHistoryToken(parent.slug));

							if (el == null) {
								el = getOpenable(
										PageTypeHelper.slugToTargetHistoryToken(
												"pageid_" + parent.id));
							}

							if (el == null) {
								el = getItem(PageTypeHelper
										.slugToTargetHistoryToken(parent.slug));

								if (el != null) {
									el = convertItemToOpenable(
											PageTypeHelper
													.slugToTargetHistoryToken(
															parent.slug),
											HeaderTemplates.INSTANCE
													.openableTitle(
															parent.title));
								}
							}
						} else {
							String dummySlug = "pageid_" + page.parent.id;
							el = addOpenable(elNavLeft,
									HeaderTemplates.INSTANCE
											.openableTitle(dummySlug),
									PageTypeHelper.slugToHref(dummySlug));
							possibleParents.put(page.parent.id, page.parent);
						}

						if (el != null) {
							href = PageTypeHelper.slugToHref(page.slug);
							addItem(el.getFirstChildElement()
									.getNextSiblingElement(),
									SafeHtmlUtils.fromString(page.title), href);
						}
					}
				}
			}
		}

		if (foundBrandPage || removedHome) {
			if (!addedBlog) {
				href = PageTypeHelper.asHref(PageType.PostsPageType);
				addItem(elNavLeft, SafeHtmlUtils.fromSafeConstant("Blog"),
						href);
			}
		} else {
			btnHome.setHref(PageTypeHelper.asHref(PageType.PostsPageType));
		}

		ensureItems().put(PageType.AllPostsPageType.asTargetHistoryToken(),
				elPosts);
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

	private void addItem (Element parent, SafeHtml title, SafeUri href) {
		String key = href.asString().replaceFirst("#", "");
		Element element = getItem(key);

		if (element == null) {
			element = Document.get().createLIElement();
			final AnchorElement a = Document.get().createAnchorElement();
			a.setHref(href);
			element.appendChild(a);
			a.setInnerSafeHtml(title);
			parent.appendChild(element);
			ensureItems().put(key, element);
		}
	}

	private void removeItem (SafeUri href) {
		String key = href.asString().replaceFirst("#", "");
		Element element = getItem(key);
		if (element != null) {
			element.removeFromParent();
			ensureItems().remove(key);
		}
	}

	private void activateItem (String page, boolean active) {
		Element element = getItem(page);

		if (element != null) {
			if (active && !element.hasClassName("active")) {
				element.addClassName("active");
			} else if (!active && element.hasClassName("active")) {
				element.removeClassName("active");
			}
		}
	}

	private Element convertItemToOpenable (String key, SafeHtml title) {
		activateItem(key, false);
		Element element = getItem(key);
		ensureItems().remove(key);
		element.setClassName("dropdown");
		final AnchorElement a = AnchorElement
				.as(element.getFirstChildElement());
		a.removeAttribute("href");
		Event.sinkEvents(a, Event.ONCLICK);
		Event.setEventListener(a, new EventListener() {

			@Override
			public void onBrowserEvent (Event event) {
				openableClick(a);
				event.stopPropagation();
			}
		});
		a.addClassName("dropdown-toggle");
		a.setInnerSafeHtml(title);
		Element ul = Document.get().createULElement();
		ul.addClassName("dropdown-menu");
		element.appendChild(ul);
		ensureOpenables().put(key, element);

		return element;
	}

	private Element addOpenable (Element parent, SafeHtml title, SafeUri href) {
		String key = href.asString().replaceFirst("#", "");
		final Element got;
		final Element element = (got = getOpenable(key)) == null
				? Document.get().createLIElement() : got;

		element.setClassName("dropdown");
		parent.appendChild(element);

		final Element a = Document.get().createAnchorElement();
		element.appendChild(a);
		Event.sinkEvents(a, Event.ONCLICK);
		Event.setEventListener(a, new EventListener() {

			@Override
			public void onBrowserEvent (Event event) {
				openableClick(a);
				event.stopPropagation();
			}
		});

		a.addClassName("dropdown-toggle");
		a.setInnerSafeHtml(title);
		Element ul = Document.get().createULElement();
		ul.addClassName("dropdown-menu");
		element.appendChild(ul);
		ensureOpenables().put(key, element);

		return element;
	}

	private void swapTitle (String oldKey, SafeHtml title, SafeUri href) {
		String key = href.asString().replaceFirst("#", "");
		Element item = getOpenable(oldKey);
		ensureOpenables().remove(oldKey);
		ensureOpenables().put(key, item);
		item.getFirstChildElement().setInnerSafeHtml(title);
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
		registration
				.add(RootPanel.get().addDomHandler(this, ClickEvent.getType()));
		registration.add(DefaultEventBus.get().addHandlerToSource(
				ChangeUserDetailsEventHandler.TYPE, UserController.get(),
				this));

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

		SafeUri href;

		if (PropertyController.get().booleanProperty(
				PropertyHelper.ALLOW_USER_REGISTRATION, false)) {
			if (login) {
				removeItem(PageTypeHelper.asHref(PageType.RegisterPageType));
			} else {
				href = PageTypeHelper.asHref(PageType.RegisterPageType);
				addItem(elNavRight,
						HeaderTemplates.INSTANCE.glyphItem("user", "Sign Up"),
						href);
			}
		}

		removeItem(PageTypeHelper.asHref(
				login ? PageType.LoginPageType : PageType.LogoutPageType));

		if (login || PropertyController.get()
				.booleanProperty(PropertyHelper.SHOW_SIGN_IN, true)) {
			href = PageTypeHelper.asHref(
					login ? PageType.LogoutPageType : PageType.LoginPageType);
			addItem(elNavRight,
					HeaderTemplates.INSTANCE.glyphItem(
							login ? "log-out" : "log-in",
							login ? "Sign Out" : "Sign In"),
					href);
		}

		addAdminNav(login);
	}

	private void addAdminNav (boolean login) {
		boolean addAdmin = false, isAdmin = false;

		if (login) {
			Permission managePosts = PermissionHelper
					.create(PermissionHelper.MANAGE_POSTS);
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

			if (isAdmin || SessionController.get().isAuthorised(managePosts)) {
				addAdmin = true;
				elAdminDropdown.appendChild(elPosts);
			} else {
				elPosts.removeFromParent();
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

			if (isAdmin || SessionController.get()
					.isAuthorised(managePermissions)) {
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
			ensureOpenables().put("blogwt_administration_menu_reserved_slug",
					elAdmin);
		} else {
			elAdmin.removeFromParent();
			ensureOpenables()
					.remove("blogwt_administration_menu_reserved_slug");
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
	 * @see com.willshex.blogwt.shared.api.user.call.event.LoginEventHandler#
	 * loginFailure (com.willshex.blogwt.shared.api.user.call.LoginRequest,
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

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event
	 * .dom.client.ClickEvent) */
	@Override
	public void onClick (ClickEvent ce) {
		openableClick(((Widget) ce.getSource()).getElement());
		ce.getNativeEvent().stopPropagation();
	}

	private void openableClick (Element source) {
		boolean isOpen = (elOpen != null);

		if (isOpen) {
			elOpen.removeClassName("open");
		}

		if (source != elOpen && openables != null
				&& openables.values().contains(source.getParentElement())) {
			elOpen = source.getParentElement();
			elOpen.addClassName("open");
		} else {
			elOpen = null;
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
	 * @see com.willshex.blogwt.shared.api.user.call.event.
	 * ChangeUserDetailsEventHandler
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
	 * @see com.willshex.blogwt.shared.api.user.call.event.
	 * ChangeUserDetailsEventHandler
	 * #changeUserDetailsFailure(com.willshex.blogwt.shared.api.user.call.
	 * ChangeUserDetailsRequest, java.lang.Throwable) */
	@Override
	public void changeUserDetailsFailure (ChangeUserDetailsRequest input,
			Throwable caught) {}

	private Map<String, Element> ensureItems () {
		if (items == null) {
			items = new HashMap<String, Element>();
		}

		return items;
	}

	private Element getItem (String key) {
		return items == null ? null : items.get(key);
	}

	private Map<String, Element> ensureOpenables () {
		if (openables == null) {
			openables = new HashMap<String, Element>();
		}

		return openables;
	}

	private Element getOpenable (String key) {
		return openables == null ? null : openables.get(key);
	}

	private static HeaderPart one;

	/**
	 * @return
	 */
	public static HeaderPart get () {
		if (one == null) {
			one = new HeaderPart();
		}
		
		return one;
	}
}
