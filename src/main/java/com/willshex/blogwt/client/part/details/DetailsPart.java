//
//  DetailsPart.java
//  bidly
//
//  Created by billy1380 on 16 Jul 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.details;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.user.event.GetEmailAvatarEventHandler;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.gwt.RegisteringComposite;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.part.details.event.AvatarChangedEventHandler;
import com.willshex.blogwt.client.part.details.event.AvatarChangedEventHandler.AvatarChanged;
import com.willshex.blogwt.client.part.details.event.UsernameChangedEventHandler;
import com.willshex.blogwt.client.part.details.event.UsernameChangedEventHandler.UsernameChanged;
import com.willshex.blogwt.client.part.password.PasswordPart;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarRequest;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarResponse;
import com.willshex.blogwt.shared.helper.DataTypeHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.blogwt.shared.util.SparseArray;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author billy1380
 *
 */
public class DetailsPart extends RegisteringComposite
		implements GetEmailAvatarEventHandler {

	private static DetailsPartUiBinder uiBinder = GWT
			.create(DetailsPartUiBinder.class);

	interface DetailsPartUiBinder extends UiBinder<Widget, DetailsPart> {}

	public interface Templates extends SafeHtmlTemplates {
		Templates INSTANCE = GWT.create(Templates.class);

		@Template("{0} (<a href=\"https://en.gravatar.com/\" target=\"_blank\">Gravatar</a>)")
		SafeHtml gravatarComment (String comment);
	}

	private static final int USERNAME_FIELD_ID = 0;
	private static final int FORENAME_FIELD_ID = USERNAME_FIELD_ID + 1;
	private static final int SURNAME_FIELD_ID = FORENAME_FIELD_ID + 1;
	private static final int EMAIL_FIELD_ID = SURNAME_FIELD_ID + 1;
	private static final int SUMMARY_FIELD_ID = EMAIL_FIELD_ID + 1;
	private static final int FIELD_COUNT = SUMMARY_FIELD_ID + 1;

	@UiField Element elGravatar;

	@UiField HTMLPanel pnlUsername;
	@UiField TextBox txtUsername;
	@UiField HTMLPanel pnlUsernameNote;

	@UiField PasswordPart pnlPassword;

	@UiField HTMLPanel pnlForename;
	@UiField TextBox txtForename;
	@UiField HTMLPanel pnlForenameNote;

	@UiField HTMLPanel pnlSurname;
	@UiField TextBox txtSurname;
	@UiField HTMLPanel pnlSurnameNote;

	@UiField HTMLPanel pnlEmail;
	@UiField TextBox txtEmail;
	@UiField HTMLPanel pnlEmailNote;

	@UiField Hyperlink lnkChangePassword;

	@UiField HTMLPanel pnlSummary;
	@UiField TextArea txtSummary;
	@UiField HTMLPanel pnlSummaryNote;

	private SparseArray<String> errors = new SparseArray<>();

	public DetailsPart () {
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtUsername, "e.g. jsmith");
		UiHelper.autoFocus(txtUsername);

		UiHelper.addPlaceholder(txtForename, "e.g. Johnny");
		UiHelper.addPlaceholder(txtSurname, "e.g. Smith");
		UiHelper.addPlaceholder(txtEmail, "e.g. johnny@smith.com");

		UiHelper.addPlaceholder(txtSummary, "e.g. Really enjoy all my cars.");
	}

	public void setGravatarComment (String comment) {
		elGravatar
				.setInnerSafeHtml(Templates.INSTANCE.gravatarComment(comment));
	}

	public void setShowPassword (boolean value) {
		pnlPassword.setVisible(value);
	}

	public void setShowChangePassword (boolean value) {
		lnkChangePassword.setVisible(value);
	}

	public void setUser (User user) {
		if (user == null
				&& DataTypeHelper.same(user, SessionController.get().user())) {
			lnkChangePassword.setTargetHistoryToken(
					PageType.ChangePasswordPageType.asTargetHistoryToken());
		} else {
			txtUsername.setText(user.username);
			txtForename.setText(user.forename);
			txtSurname.setText(user.surname);
			txtEmail.setText(user.email);
			txtSummary.setText(user.summary);

			lnkChangePassword
					.setTargetHistoryToken(PageType.ChangePasswordPageType
							.asTargetHistoryToken("id", user.id.toString()));
		}
	}

	@UiHandler("txtEmail")
	void onEmailChanged (ValueChangeEvent<String> vce) {
		String email = vce.getValue(), trimmed;

		if (email == null || (trimmed = email.trim()).isEmpty()) {
			fireEvent(new AvatarChanged(""));
		} else {
			UserController.get().getEmailAvatar(trimmed);
		}
	}

	@UiHandler("txtUsername")
	void onUsernameChanged (ValueChangeEvent<String> vce) {
		fireEvent(new UsernameChanged(vce.getValue()));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				GetEmailAvatarEventHandler.TYPE, UserController.get(), this));
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.GetEmailAvatarEventHandler
	 * #getEmailAvatarSuccess(com.willshex.blogwt.shared.api.user.call.
	 * GetEmailAvatarRequest,
	 * com.willshex.blogwt.shared.api.user.call.GetEmailAvatarResponse) */
	@Override
	public void getEmailAvatarSuccess (GetEmailAvatarRequest input,
			GetEmailAvatarResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			fireEvent(new AvatarChanged(output.avatar + "?s="
					+ UserHelper.AVATAR_LARGE_SIZE + "&default=retro"));
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.user.call.event.GetEmailAvatarEventHandler
	 * #getEmailAvatarFailure(com.willshex.blogwt.shared.api.user.call.
	 * GetEmailAvatarRequest, java.lang.Throwable) */
	@Override
	public void getEmailAvatarFailure (GetEmailAvatarRequest input,
			Throwable caught) {
		GWT.log("getEmailAvatarFailure", caught);
	}

	public String getUsername () {
		return txtUsername.getValue();
	}

	public String getForename () {
		return txtForename.getValue();
	}

	public String getSurname () {
		return txtSurname.getValue();
	}

	public String getEmail () {
		return txtEmail.getValue();
	}

	public String getSummary () {
		return txtSummary.getValue();
	}

	public String getPassword () {
		return pnlPassword.getPassword();
	}

	public void setEnabled (boolean enable) {
		txtUsername.setEnabled(enable);
		txtForename.setEnabled(enable);
		txtSurname.setEnabled(enable);
		txtEmail.setEnabled(enable);
		pnlPassword.setEnabled(enable);
	}

	public void focus () {
		txtUsername.setFocus(true);
	}

	public void showErrors () {
		for (int key = 0; key < FIELD_COUNT; key++) {
			if (errors.containsKey(key)) {
				switch (key) {
				case USERNAME_FIELD_ID:
					UiHelper.showError(pnlUsername, pnlUsernameNote,
							errors.get(key));
					break;
				case FORENAME_FIELD_ID:
					UiHelper.showError(pnlForename, pnlForenameNote,
							errors.get(key));
					break;
				case SURNAME_FIELD_ID:
					UiHelper.showError(pnlSurname, pnlSurnameNote,
							errors.get(key));
					break;
				case EMAIL_FIELD_ID:
					UiHelper.showError(pnlEmail, pnlEmailNote, errors.get(key));
					break;
				case SUMMARY_FIELD_ID:
					UiHelper.showError(pnlSummary, pnlSummaryNote,
							errors.get(key));
					break;
				}
			}
		}

		pnlPassword.showErrors();
	}

	public void clearErrors () {
		errors.clear();

		UiHelper.removeError(pnlUsername, pnlUsernameNote);
		UiHelper.removeError(pnlForename, pnlForenameNote);
		UiHelper.removeError(pnlSurname, pnlSurnameNote);
		UiHelper.removeError(pnlEmail, pnlEmailNote);

		pnlPassword.clearErrors();
	}

	public void addAvatarChanged (AvatarChangedEventHandler handler) {
		addHandler(handler, AvatarChangedEventHandler.TYPE);
	}

	public void addUsernameChanged (UsernameChangedEventHandler handler) {
		addHandler(handler, UsernameChangedEventHandler.TYPE);
	}

	public void setShowSummary (boolean value) {
		pnlSummary.setVisible(value);
	}

	public boolean isValid () {
		boolean valid = true;

		clearErrors();

		if (txtUsername.getValue().trim().isEmpty()) {
			valid = false;
			errors.put(USERNAME_FIELD_ID, "Username cannot be empty");
		}

		if (txtForename.getValue().trim().isEmpty()) {
			valid = false;
			errors.put(FORENAME_FIELD_ID, "Forename cannot be empty");
		}

		if (txtSurname.getValue().trim().isEmpty()) {
			valid = false;
			errors.put(SURNAME_FIELD_ID, "Surname cannot be empty");
		}

		// TODO: add email address validation
		if (txtEmail.getValue().trim().isEmpty()) {
			valid = false;
			errors.put(EMAIL_FIELD_ID, "Email address cannot be empty");
		}

		if (txtSummary.getValue().trim().length() > 2000) {
			valid = false;
			errors.put(SUMMARY_FIELD_ID,
					"Summary cannot be longer than 2000 characters");
		}

		if (!pnlPassword.isValid()) {
			valid = false;
		}

		return valid;
	}
}
