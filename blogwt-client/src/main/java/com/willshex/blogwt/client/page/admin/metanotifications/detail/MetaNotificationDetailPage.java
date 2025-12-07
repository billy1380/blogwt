//
//  MetaNotificationDetailPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 16 Mar 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin.metanotifications.detail;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.api.notification.event.AddMetaNotificationEventHandler;
import com.willshex.blogwt.client.api.notification.event.GetMetaNotificationEventHandler;
import com.willshex.blogwt.client.api.notification.event.UpdateMetaNotificationEventHandler;
import com.willshex.blogwt.client.controller.MetaNotificationController;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.NotificationModeType;
import com.willshex.blogwt.shared.api.notification.call.AddMetaNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.AddMetaNotificationResponse;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationResponse;
import com.willshex.blogwt.shared.api.notification.call.UpdateMetaNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.UpdateMetaNotificationResponse;
import com.willshex.blogwt.shared.helper.DataTypeHelper;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class MetaNotificationDetailPage extends Page
		implements GetMetaNotificationEventHandler,
		AddMetaNotificationEventHandler, UpdateMetaNotificationEventHandler {

	private static MetaNotificationDetailPageUiBinder uiBinder = GWT
			.create(MetaNotificationDetailPageUiBinder.class);

	interface MetaNotificationDetailPageUiBinder
			extends UiBinder<Widget, MetaNotificationDetailPage> {}

	@UiField Element elTitle;

	@UiField FormPanel frmDetails;

	@UiField HTMLPanel pnlCode;
	@UiField TextBox txtCode;
	@UiField HTMLPanel pnlCodeNote;

	@UiField HTMLPanel pnlName;
	@UiField TextBox txtName;
	@UiField HTMLPanel pnlNameNote;

	@UiField HTMLPanel pnlContent;
	@UiField TextArea txtContent;
	@UiField HTMLPanel pnlContentNote;

	@UiField HTMLPanel pnlGroup;
	@UiField TextBox txtGroup;
	@UiField HTMLPanel pnlGroupNote;

	@UiField HTMLPanel pnlMode;
	@UiField CheckBox cboModeEmail;
	@UiField CheckBox cboModeSms;
	@UiField CheckBox cboModePush;
	@UiField HTMLPanel pnlModeNote;

	@UiField HTMLPanel pnlDefault;
	@UiField CheckBox cboDefaultEmail;
	@UiField CheckBox cboDefaultSms;
	@UiField CheckBox cboDefaultPush;
	@UiField HTMLPanel pnlDefaultNote;

	@UiField SubmitButton btnUpdate;

	private MetaNotification notification;

	public MetaNotificationDetailPage () {
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtCode, "3 letter code e.g. NPA");
		UiHelper.addPlaceholder(txtName, "e.g. New post");
		UiHelper.addPlaceholder(txtGroup, "e.g. Post notifications");
		UiHelper.addPlaceholder(txtContent,
				"e.g. A new post is now available by ${post.author}");

		changedMode(cboModeEmail, cboDefaultEmail);
		changedMode(cboModeSms, cboDefaultSms);
		changedMode(cboModePush, cboDefaultPush);

		updateDefaultVisiblity();
	}

	private void updateDefaultVisiblity () {
		pnlDefault.setVisible(cboDefaultEmail.isVisible()
				|| cboDefaultSms.isVisible() || cboDefaultPush.isVisible());
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this::navigationChanged));
		register(DefaultEventBus.get().addHandlerToSource(
				GetMetaNotificationEventHandler.TYPE,
				MetaNotificationController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				AddMetaNotificationEventHandler.TYPE,
				MetaNotificationController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				UpdateMetaNotificationEventHandler.TYPE,
				MetaNotificationController.get(), this));
	}

	void navigationChanged (Stack previous, Stack current) {
		reset();

		if (current.getAction() == null || "new".equals(current.getAction())) {
			elTitle.setInnerText("Add notification");
			ready();
		} else if ("id".equals(current.getAction())
				&& current.getParameterCount() > 0) {
			String param = current.getParameter(0);
			Long id = Long.valueOf(param);

			elTitle.setInnerText("Edit " + param);

			if (DataTypeHelper.<MetaNotification> same(notification, id)) {
				showMetaNotification(notification);
				ready();
			} else {
				MetaNotificationController.get().getMetaNotification(
						(MetaNotification) new MetaNotification().id(id));
			}
		}
	}

	private void showMetaNotification (MetaNotification metaNotification) {
		txtCode.setText(metaNotification.code);
		txtName.setText(metaNotification.name);
		txtContent.setText(metaNotification.content);
		txtGroup.setText(metaNotification.group);

		if (metaNotification.modes != null) {
			for (NotificationModeType mode : metaNotification.modes) {
				switch (mode) {
				case NotificationModeTypeEmail:
					cboModeEmail.setValue(Boolean.TRUE, true);
					break;
				case NotificationModeTypePush:
					cboModePush.setValue(Boolean.TRUE, true);
					break;
				case NotificationModeTypeSms:
					cboModeSms.setValue(Boolean.TRUE, true);
					break;
				}
			}
		}

		if (metaNotification.defaults != null) {
			for (NotificationModeType mode : metaNotification.defaults) {
				switch (mode) {
				case NotificationModeTypeEmail:
					cboDefaultEmail.setValue(Boolean.TRUE, true);
					break;
				case NotificationModeTypePush:
					cboDefaultPush.setValue(Boolean.TRUE, true);
					break;
				case NotificationModeTypeSms:
					cboDefaultSms.setValue(Boolean.TRUE, true);
					break;
				}
			}
		}
	}

	private void ready () {
		btnUpdate.getElement()
				.setInnerSafeHtml(WizardDialog.WizardDialogTemplates.INSTANCE
						.nextButton("Save notification"));

		enableInput(true);
		txtCode.setFocus(true);
	}

	private void loading () {
		btnUpdate.getElement()
				.setInnerSafeHtml(WizardDialog.WizardDialogTemplates.INSTANCE
						.loadingButton("Saving notification ",
								Resources.RES.primaryLoader().getSafeUri()));
		enableInput(false);
	}

	private void enableInput (boolean enabled) {
		txtCode.setEnabled(enabled);
		txtName.setEnabled(enabled);
		txtContent.setEnabled(enabled);
		txtGroup.setEnabled(enabled);
		cboModeEmail.setEnabled(enabled);
		cboModeSms.setEnabled(enabled);
		cboModePush.setEnabled(enabled);
		cboDefaultEmail.setEnabled(enabled);
		cboDefaultSms.setEnabled(enabled);
		cboDefaultPush.setEnabled(enabled);
		btnUpdate.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#reset() */
	@Override
	protected void reset () {
		super.reset();

		frmDetails.reset();
		elTitle.setInnerText("Add Notification");

		notification = null;
	}

	private boolean isValid () {
		boolean valid = true;

		return valid;
	}

	private void showErrors () {}

	@UiHandler("btnUpdate")
	void onUpdateClicked (ClickEvent ce) {
		if (isValid()) {
			loading();

			(notification == null ? notification = new MetaNotification()
					: notification).code(txtCode.getValue())
							.name(txtName.getValue())
							.content(txtContent.getValue())
							.group(txtGroup.getValue()).modes(modes())
							.defaults(defaults());

			if (notification.id == null) {
				MetaNotificationController.get()
						.addMetaNotification(notification);
			} else {
				MetaNotificationController.get()
						.updateMetaNotification(notification);
			}
		} else {
			showErrors();
		}
	}

	private List<NotificationModeType> defaults () {
		List<NotificationModeType> defaults = null;

		if (Boolean.TRUE.equals(cboDefaultEmail.getValue())
				|| Boolean.TRUE.equals(cboDefaultSms.getValue())
				|| Boolean.TRUE.equals(cboDefaultPush.getValue())) {
			defaults = new ArrayList<NotificationModeType>();

			if (Boolean.TRUE.equals(cboDefaultEmail.getValue())) {
				defaults.add(NotificationModeType.NotificationModeTypeEmail);
			}

			if (Boolean.TRUE.equals(cboDefaultSms.getValue())) {
				defaults.add(NotificationModeType.NotificationModeTypeSms);
			}

			if (Boolean.TRUE.equals(cboDefaultPush.getValue())) {
				defaults.add(NotificationModeType.NotificationModeTypePush);
			}
		}

		return defaults;
	}

	private List<NotificationModeType> modes () {
		List<NotificationModeType> modes = null;

		if (Boolean.TRUE.equals(cboModeEmail.getValue())
				|| Boolean.TRUE.equals(cboModeSms.getValue())
				|| Boolean.TRUE.equals(cboModePush.getValue())) {
			modes = new ArrayList<NotificationModeType>();

			if (Boolean.TRUE.equals(cboModeEmail.getValue())) {
				modes.add(NotificationModeType.NotificationModeTypeEmail);
			}

			if (Boolean.TRUE.equals(cboModeSms.getValue())) {
				modes.add(NotificationModeType.NotificationModeTypeSms);
			}

			if (Boolean.TRUE.equals(cboModePush.getValue())) {
				modes.add(NotificationModeType.NotificationModeTypePush);
			}
		}

		return modes;
	}

	@UiHandler({ "cboModeEmail", "cboModeSms", "cboModePush" })
	void onModeChanged (ValueChangeEvent<Boolean> vce) {
		if (vce.getSource() == cboModeEmail) {
			changedMode(cboModeEmail, cboDefaultEmail);
		} else if (vce.getSource() == cboModeSms) {
			changedMode(cboModeSms, cboDefaultSms);
		} else if (vce.getSource() == cboModePush) {
			changedMode(cboModePush, cboDefaultPush);
		}

		updateDefaultVisiblity();
	}

	private void changedMode (CheckBox cboMode, CheckBox cboDefault) {
		if (cboMode.getValue()) {
			cboDefault.setVisible(true);
		} else {
			cboDefault.setVisible(false);
		}

		cboDefault.setValue(Boolean.FALSE);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.api.notification.event.
	 * UpdateMetaNotificationEventHandler#updateMetaNotificationSuccess(com.
	 * willshex.blogwt.shared.api.notification.call.
	 * UpdateMetaNotificationRequest,
	 * com.willshex.blogwt.shared.api.notification.call.
	 * UpdateMetaNotificationResponse) */
	@Override
	public void updateMetaNotificationSuccess (
			UpdateMetaNotificationRequest input,
			UpdateMetaNotificationResponse output) {
		if (output.status == StatusType.StatusTypeSuccess
				&& output.meta != null) {
			showMetaNotification(notification = output.meta);
		}

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.api.notification.event.
	 * UpdateMetaNotificationEventHandler#updateMetaNotificationFailure(com.
	 * willshex.blogwt.shared.api.notification.call.
	 * UpdateMetaNotificationRequest, java.lang.Throwable) */
	@Override
	public void updateMetaNotificationFailure (
			UpdateMetaNotificationRequest input, Throwable caught) {
		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.api.notification.event.
	 * AddMetaNotificationEventHandler#addMetaNotificationSuccess(com.willshex.
	 * blogwt.shared.api.notification.call.AddMetaNotificationRequest,
	 * com.willshex.blogwt.shared.api.notification.call.
	 * AddMetaNotificationResponse) */
	@Override
	public void addMetaNotificationSuccess (AddMetaNotificationRequest input,
			AddMetaNotificationResponse output) {
		if (output.status == StatusType.StatusTypeSuccess
				&& output.meta != null) {
			showMetaNotification(notification = output.meta);
		}

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.api.notification.event.
	 * AddMetaNotificationEventHandler#addMetaNotificationFailure(com.willshex.
	 * blogwt.shared.api.notification.call.AddMetaNotificationRequest,
	 * java.lang.Throwable) */
	@Override
	public void addMetaNotificationFailure (AddMetaNotificationRequest input,
			Throwable caught) {
		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.api.notification.event.
	 * GetMetaNotificationEventHandler#getMetaNotificationSuccess(com.willshex.
	 * blogwt.shared.api.notification.call.GetMetaNotificationRequest,
	 * com.willshex.blogwt.shared.api.notification.call.
	 * GetMetaNotificationResponse) */
	@Override
	public void getMetaNotificationSuccess (GetMetaNotificationRequest input,
			GetMetaNotificationResponse output) {
		if (output.status == StatusType.StatusTypeSuccess
				&& output.meta != null) {
			showMetaNotification(notification = output.meta);
		}

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.api.notification.event.
	 * GetMetaNotificationEventHandler#getMetaNotificationFailure(com.willshex.
	 * blogwt.shared.api.notification.call.GetMetaNotificationRequest,
	 * java.lang.Throwable) */
	@Override
	public void getMetaNotificationFailure (GetMetaNotificationRequest input,
			Throwable caught) {
		ready();
	}

}
