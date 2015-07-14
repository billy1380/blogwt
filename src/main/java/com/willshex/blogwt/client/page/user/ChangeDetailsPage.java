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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.event.ChangeUserDetailsEventHandler;
import com.willshex.blogwt.shared.api.user.call.event.GetUserDetailsEventHandler;
import com.willshex.blogwt.shared.helper.DateTimeHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ChangeDetailsPage extends Page implements
		NavigationChangedEventHandler, GetUserDetailsEventHandler,
		ChangeUserDetailsEventHandler {

	private static ChangeDetailsPageUiBinder uiBinder = GWT
			.create(ChangeDetailsPageUiBinder.class);

	interface ChangeDetailsPageUiBinder extends
			UiBinder<Widget, ChangeDetailsPage> {}

	@UiField Image imgAvatar;
	@UiField HeadingElement h3Username;
	@UiField Element elDates;

	@UiField HTMLPanel pnlUsername;
	@UiField TextBox txtUsername;
	@UiField HTMLPanel pnlUsernameNote;

	@UiField HTMLPanel pnlForename;
	@UiField TextBox txtForename;
	@UiField HTMLPanel pnlForenameNote;

	@UiField HTMLPanel pnlSurname;
	@UiField TextBox txtSurname;
	@UiField HTMLPanel pnlSurnameNote;

	@UiField HTMLPanel pnlEmail;
	@UiField TextBox txtEmail;
	@UiField HTMLPanel pnlEmailNote;

	@UiField Button btnUpdate;
	@UiField InlineHyperlink lnkChangePassword;

	private User user;

	public ChangeDetailsPage () {
		super(PageType.ChangeDetailsPageType);
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtUsername, "Username");
		UiHelper.autoFocus(txtUsername);
		UiHelper.addPlaceholder(txtForename, "Forename");
		UiHelper.addPlaceholder(txtSurname, "Surname");
		UiHelper.addPlaceholder(txtEmail, "Email");
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
		register(DefaultEventBus.get().addHandlerToSource(
				GetUserDetailsEventHandler.TYPE, UserController.get(), this));

		register(DefaultEventBus.get().addHandlerToSource(
				ChangeUserDetailsEventHandler.TYPE, UserController.get(), this));

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.event.NavigationChangedEventHandler#
	 * navigationChanged
	 * (com.willshex.blogwt.client.controller.NavigationController.Stack,
	 * com.willshex.blogwt.client.controller.NavigationController.Stack) */
	@Override
	public void navigationChanged (Stack previous, Stack current) {
		if (current.getAction() == null) {
			showUserDetails(user = SessionController.get().user());
			lnkChangePassword
					.setTargetHistoryToken(PageType.ChangePasswordPageType
							.asTargetHistoryToken());
		} else if ("id".equals(current.getAction())
				&& current.getParameterCount() > 0) {
			Long id = Long.valueOf(current.getParameter(0));
			User user = new User();
			user.id(id);

			UserController.get().getUser(user);
		}
	}

	private void showUserDetails (User user) {
		if (user != null) {
			String username = "@" + user.username;
			imgAvatar.setAltText(username);
			imgAvatar.setUrl(user.avatar + "?s=160&default=retro");
			h3Username
					.setInnerHTML(username
							+ (UserHelper.isAdmin(user) ? " <span class=\"glyphicon glyphicon-sunglasses\"></span>"
									: ""));
			elDates.setInnerText("Added "
					+ DateTimeHelper.ago(user.created)
					+ " and last seen "
					+ (user.lastLoggedIn == null ? "never" : DateTimeHelper
							.ago(user.lastLoggedIn)));
			txtUsername.setText(user.username);
			txtForename.setText(user.forename);
			txtSurname.setText(user.surname);
			txtEmail.setText(user.email);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.GetUserDetailsEventHandler
	 * #getUserDetailsSuccess(com.willshex.blogwt.shared.api.user.call.
	 * GetUserDetailsRequest,
	 * com.willshex.blogwt.shared.api.user.call.GetUserDetailsResponse) */
	@Override
	public void getUserDetailsSuccess (GetUserDetailsRequest input,
			GetUserDetailsResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			showUserDetails(user = output.user);
			lnkChangePassword
					.setTargetHistoryToken(PageType.ChangePasswordPageType
							.asTargetHistoryToken("id", user.id.toString()));
		}

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.GetUserDetailsEventHandler
	 * #getUserDetailsFailure(com.willshex.blogwt.shared.api.user.call.
	 * GetUserDetailsRequest, java.lang.Throwable) */
	@Override
	public void getUserDetailsFailure (GetUserDetailsRequest input,
			Throwable caught) {
		ready();
	}

	@UiHandler("btnUpdate")
	void onUpdateClicked (ClickEvent ce) {
		if (isValid()) {
			loading();
			User updatedUser = new User();
			updatedUser.id(user.id);
			UserController.get().changeUserDetails(
					updatedUser.username(txtUsername.getText())
							.forename(txtForename.getText())
							.surname(txtSurname.getText())
							.email(txtEmail.getText()));
		} else {
			showErrors();
		}
	}

	private boolean isValid () {
		// do client validation
		return true;
	}

	private void showErrors () {

	}

	private void ready () {
		btnUpdate.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE
						.nextButton("Update"));

		btnUpdate.setEnabled(true);
		txtUsername.setEnabled(true);
		txtForename.setEnabled(true);
		txtSurname.setEnabled(true);
		txtEmail.setEnabled(true);
	}

	private void loading () {
		btnUpdate.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE.loadingButton(
						"Updating... ", Resources.RES.primaryLoader()
								.getSafeUri()));

		btnUpdate.setEnabled(false);
		txtUsername.setEnabled(false);
		txtForename.setEnabled(false);
		txtSurname.setEnabled(false);
		txtEmail.setEnabled(false);

		pnlUsername.removeStyleName("has-error");
		pnlUsernameNote.setVisible(false);
		pnlForename.removeStyleName("has-error");
		pnlForenameNote.setVisible(false);
		pnlSurname.removeStyleName("has-error");
		pnlSurnameNote.setVisible(false);
		pnlEmail.removeStyleName("has-error");
		pnlEmailNote.setVisible(false);
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
			showUserDetails(user = output.user);
		}

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.ChangeUserDetailsEventHandler
	 * #changeUserDetailsFailure(com.willshex.blogwt.shared.api.user.call.
	 * ChangeUserDetailsRequest, java.lang.Throwable) */
	@Override
	public void changeUserDetailsFailure (ChangeUserDetailsRequest input,
			Throwable caught) {
		ready();
	}

}
