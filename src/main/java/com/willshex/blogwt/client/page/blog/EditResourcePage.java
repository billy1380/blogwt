//
//  EditResourcePage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author billy1380
 *
 */
public class EditResourcePage extends Page implements
		NavigationChangedEventHandler {

	private static EditResourcePageUiBinder uiBinder = GWT
			.create(EditResourcePageUiBinder.class);

	interface EditResourcePageUiBinder extends
			UiBinder<Widget, EditResourcePage> {}

	@UiField Element elHeading;
	@UiField FormPanel frmDetails;
	@UiField HTMLPanel pnlResource;
	@UiField HTMLPanel pnlName;
	@UiField TextBox txtName;
	@UiField HTMLPanel pnlNameNote;
	@UiField HTMLPanel pnlData;
	@UiField TextBox txtData;
	@UiField HTMLPanel pnlDescription;
	@UiField TextArea txtDescription;
	@UiField HTMLPanel pnlDescriptionNote;
	@UiField HTMLPanel pnlType;
	@UiField TextBox txtType;
	@UiField HTMLPanel pnlTypeNote;
	@UiField HTMLPanel pnlProperties;
	@UiField TextArea txtProperties;
	@UiField HTMLPanel pnlPropertiesNote;

	@UiField Button btnUpdate;

	private Resource resource;
	private String actionText;

	private static final String UPDATE_ACTION_TEXT = "Update";
	private static final String CREATE_ACTION_TEXT = "Create";

	public EditResourcePage () {
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtName, "Name");
		UiHelper.addPlaceholder(txtData, "Data");
		UiHelper.addPlaceholder(txtDescription, "Description");
		UiHelper.addPlaceholder(txtType, "Type");
		UiHelper.addPlaceholder(txtProperties, "Properties");
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));
	}

	@Override
	public void navigationChanged (Stack previous, Stack current) {
		reset();

		// load and show

		elHeading.setInnerText(getHeadingText());
		
		ready();
	}

	@Override
	protected void reset () {
		frmDetails.reset();

		actionText = UPDATE_ACTION_TEXT;
		resource = null;

		super.reset();
	}

	@UiHandler("btnUpdate")
	void onUpdateClicked (ClickEvent ce) {
		if (isValid()) {
			loading();

		} else {
			showErrors();
		}
	}

	private boolean isValid () {
		// do client validation
		return true;
	}

	private void ready () {
		btnUpdate.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE
						.nextButton(actionText));

		btnUpdate.setEnabled(true);

		txtName.setFocus(true);
	}

	private void showErrors () {

	}

	private void loading () {
		btnUpdate.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE.loadingButton(
						getLoadingText(), Resources.RES.primaryLoader()
								.getSafeUri()));

		btnUpdate.setEnabled(false);

		pnlName.removeStyleName("has-error");
		pnlNameNote.setVisible(false);
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
		}

		return loadingText;
	}

	private String getHeadingText () {
		String headingText = null;
		switch (actionText) {
		case UPDATE_ACTION_TEXT:
			headingText = "Edit Resource";
			break;
		case CREATE_ACTION_TEXT:
			headingText = "Add Resource";
			break;
		}

		return headingText;
	}

}
