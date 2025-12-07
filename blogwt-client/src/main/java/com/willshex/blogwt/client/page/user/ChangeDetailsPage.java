//
//  ChangeDetailsPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.api.user.event.ChangeUserDetailsEventHandler;
import com.willshex.blogwt.client.api.user.event.GetUserDetailsEventHandler;
import com.willshex.blogwt.client.api.user.event.RegisterUserEventHandler;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.accounttabs.AccountTabsPart;
import com.willshex.blogwt.client.part.details.DetailsPart;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.RegisterUserRequest;
import com.willshex.blogwt.shared.api.user.call.RegisterUserResponse;
import com.willshex.blogwt.shared.helper.DateTimeHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ChangeDetailsPage extends Page
		implements GetUserDetailsEventHandler, ChangeUserDetailsEventHandler,
		RegisterUserEventHandler {

	private static ChangeDetailsPageUiBinder uiBinder = GWT
			.create(ChangeDetailsPageUiBinder.class);

	interface ChangeDetailsPageUiBinder
			extends UiBinder<Widget, ChangeDetailsPage> {}

	public interface ChangeDetailsTemplates extends SafeHtmlTemplates {
		ChangeDetailsTemplates INSTANCE = GWT
				.create(ChangeDetailsTemplates.class);

		@Template("{0} <a href=\"{1}\"><span class=\"glyphicon glyphicon-user\"></span></a>")
		SafeHtml rolesAndPermissions (String username, String accessHref);

		@Template("{0} <a href=\"{1}\"><span class=\"glyphicon glyphicon-certificate\"></span></a>")
		SafeHtml adminRolesAndPermissions (String username, String accessHref);
	}

	private static final String UPDATE_ACTION_TEXT = "Update";
	private static final String CREATE_ACTION_TEXT = "Create";
	private static final String REGISTER_ACTION_TEXT = "Register";

	@UiField Element elHeading;
	@UiField DetailsPart pnlDetails;
	@UiField FormPanel frmDetails;

	@UiField Image imgAvatar;
	@UiField HeadingElement h3Username;
	@UiField Element elDates;

	@UiField Button btnUpdate;

	@UiField HTMLPanel pnlTabs;
	@UiField HTMLPanel pnlAlternative;

	private User user;
	private String actionText;

	public ChangeDetailsPage () {
		initWidget(uiBinder.createAndBindUi(this));

		actionText = UPDATE_ACTION_TEXT;

		pnlDetails.addAvatarChanged(a -> imgAvatar.setUrl(a));
		pnlDetails.addUsernameChanged(u -> {
			String username = "@" + u;

			imgAvatar.setAltText(username);
			h3Username.setInnerHTML(username);
		});
	}
	@Override
	protected void onAttach () {
		super.onAttach();

		AccountTabsPart.get().removeFromParent();

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this::navigationChanged));
		register(DefaultEventBus.get().addHandlerToSource(
				GetUserDetailsEventHandler.TYPE, UserController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				ChangeUserDetailsEventHandler.TYPE, UserController.get(),
				this));

		register(DefaultEventBus.get().addHandlerToSource(
				RegisterUserEventHandler.TYPE, UserController.get(), this));
	}

	void navigationChanged (Stack p, Stack c) {
		reset();

		boolean addTabs = true, showAlternative = false;

		if (PageType.ChangeDetailsPageType.equals(c.getPage())) {
			if (c.getAction() == null) {
				show(user = SessionController.get().user());
				AccountTabsPart.get().setUser(null);

				pnlDetails.setShowChangePassword(true);
				pnlDetails.setShowPassword(false);
			} else if ("id".equals(c.getAction())
					&& c.getParameterCount() > 0) {
				Long id = Long.valueOf(c.getParameter(0));
				User user = new User();
				user.id(id);

				AccountTabsPart.get().setUser(user);
				UserController.get().getUser(user);

				pnlDetails.setShowChangePassword(false);
				pnlDetails.setShowPassword(false);
			} else if ("new".equals(c.getAction())) {
				elDates.setInnerText("Enter user details");
				pnlDetails.setShowChangePassword(false);
				pnlDetails.setShowPassword(true);

				if (SessionController.get().isAdmin()) {
					actionText = CREATE_ACTION_TEXT;
				}

				addTabs = false;
			}
		} else if (PageType.RegisterPageType.equals(c.getPage())) {
			elDates.setInnerText("Enter user details");
			pnlDetails.setShowChangePassword(false);
			pnlDetails.setShowPassword(true);

			actionText = REGISTER_ACTION_TEXT;

			showAlternative = true;
			addTabs = false;
		}

		elHeading.setInnerText(getHeadingText());
		setGravatarSafeHtml();

		if (addTabs) {
			pnlTabs.add(AccountTabsPart.get());
			AccountTabsPart.get().navigationChanged(p, c);
		} else {
			AccountTabsPart.get().removeFromParent();
		}

		pnlAlternative.setVisible(showAlternative);
		ready();
		refreshTitle();
	}

	private void show (User user) {
		if (user != null) {
			String username = "@" + user.username;
			imgAvatar.setAltText(username);
			imgAvatar.setUrl(user.avatar + "?s=" + UserHelper.AVATAR_LARGE_SIZE
					+ "&default=retro");
			String url = PageTypeHelper.asHref(PageType.ChangeAccessPageType,
					"id", user.id.toString()).asString();
			h3Username.setInnerSafeHtml(UserHelper.isAdmin(user)
					? ChangeDetailsTemplates.INSTANCE
							.adminRolesAndPermissions(username, url)
					: ChangeDetailsTemplates.INSTANCE
							.rolesAndPermissions(username, url));
			elDates.setInnerText("Added " + DateTimeHelper.ago(user.created)
					+ " and last seen " + (user.lastLoggedIn == null ? "never"
							: DateTimeHelper.ago(user.lastLoggedIn)));
			pnlDetails.setUser(user);
		}
	}
	@Override
	public void getUserDetailsSuccess (GetUserDetailsRequest input,
			GetUserDetailsResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			show(user = output.user);
			pnlDetails.setUser(user);
		}

		ready();
	}
	@Override
	public void getUserDetailsFailure (GetUserDetailsRequest input,
			Throwable caught) {
		GWT.log("getUserDetailsFailure", caught);

		ready();
	}

	@UiHandler("btnUpdate")
	void onUpdateClicked (ClickEvent ce) {
		if (isValid()) {
			loading();
			User user = new User().username(pnlDetails.getUsername())
					.forename(pnlDetails.getForename())
					.surname(pnlDetails.getSurname())
					.email(pnlDetails.getEmail())
					.summary(pnlDetails.getSummary());

			if (this.user == null) {
				user.password(pnlDetails.getPassword());
				UserController.get().registerUser(user);
			} else {
				user.id(this.user.id);
				UserController.get().changeUserDetails(user);
			}
		} else {
			showErrors();
		}
	}

	private boolean isValid () {
		// do client validation - including property check for user registration
		return true;
	}

	private void showErrors () {

	}

	private void ready () {
		btnUpdate.getElement()
				.setInnerSafeHtml(WizardDialog.WizardDialogTemplates.INSTANCE
						.nextButton(actionText));

		pnlDetails.setEnabled(true);
		pnlDetails.focus();
		btnUpdate.setEnabled(true);
	}

	private void loading () {
		btnUpdate.getElement()
				.setInnerSafeHtml(WizardDialog.WizardDialogTemplates.INSTANCE
						.loadingButton(getLoadingText(),
								Resources.RES.primaryLoader().getSafeUri()));

		btnUpdate.setEnabled(false);
		pnlDetails.setEnabled(false);

		pnlDetails.clearErrors();
	}

	private String getLoadingText () {
		String loadingText = null;
		switch (actionText) {
		case UPDATE_ACTION_TEXT:
			loadingText = "Updating... ";
			break;
		case CREATE_ACTION_TEXT:
			loadingText = "Creating... ";
			break;
		case REGISTER_ACTION_TEXT:
			loadingText = "Registering... ";
			break;
		}

		return loadingText;
	}

	private String getHeadingText () {
		String headingText = null;
		switch (actionText) {
		case UPDATE_ACTION_TEXT:
			headingText = "Account";
			break;
		case CREATE_ACTION_TEXT:
			headingText = "Add User";
			break;
		case REGISTER_ACTION_TEXT:
			headingText = "Sign Up";
			break;
		}

		return headingText;
	}

	private void setGravatarSafeHtml () {
		switch (actionText) {
		case UPDATE_ACTION_TEXT:
			pnlDetails.setGravatarComment(
					"Changing e-mail address will change your avatar");
			break;
		case CREATE_ACTION_TEXT:
			pnlDetails.setGravatarComment(
					"E-mail address will determine user's avatar");
			break;
		case REGISTER_ACTION_TEXT:
			pnlDetails.setGravatarComment(
					"Your e-mail address will determine your avatar");
			break;
		}
	}
	@Override
	public void changeUserDetailsSuccess (ChangeUserDetailsRequest input,
			ChangeUserDetailsResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			show(user = output.user);
		}

		ready();
	}
	@Override
	public void changeUserDetailsFailure (ChangeUserDetailsRequest input,
			Throwable caught) {
		GWT.log("changeUserDetailsFailure", caught);

		ready();
	}
	@Override
	protected void reset () {
		frmDetails.reset();
		imgAvatar.setAltText("");
		imgAvatar.setUrl("");
		elDates.setInnerText("");
		h3Username.setInnerText("");
		actionText = UPDATE_ACTION_TEXT;
		user = null;

		super.reset();
	}
	@Override
	public void registerUserSuccess (RegisterUserRequest input,
			RegisterUserResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			if (SessionController.get().isAdmin()) {
				PageTypeHelper.show(PageType.ChangeDetailsPageType, "id",
						output.user.id.toString());
			} else {
				PageTypeHelper.show(PageType.LoginPageType,
						"username=" + output.user.username.toString());
			}
		}

		ready();
	}
	@Override
	public void registerUserFailure (RegisterUserRequest input,
			Throwable caught) {
		GWT.log("registerUserFailure", caught);

		ready();
	}
	@Override
	public String getTitle () {
		return UiHelper.pageTitle(getHeadingText());
	}

}
