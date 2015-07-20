//
//  PropertiesPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.part.property.StringPropertyPart;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.helper.PropertyHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PropertiesPage extends Page {

	private static PropertiesPageUiBinder uiBinder = GWT
			.create(PropertiesPageUiBinder.class);

	interface PropertiesPageUiBinder extends UiBinder<Widget, PropertiesPage> {}

	@UiField HTMLPanel pnlProperties;
	@UiField SubmitButton btnUpdate;

	public PropertiesPage () {
		super(PageType.PropertiesPageType);
		initWidget(uiBinder.createAndBindUi(this));

		btnUpdate.removeFromParent();
		addProperties();
		pnlProperties.add(btnUpdate);

	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		ready();
	}

	@UiHandler("btnUpdate")
	void onUpdateClicked (ClickEvent ce) {
		if (isValid()) {
			loading();
			// update properties
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

	/**
	 * 
	 */
	private void addProperties () {
		StringPropertyPart formPart = null;
		boolean first = true;

		for (Property property : PropertyHelper.properties()) {
			formPart = new StringPropertyPart();

			if (first) {
				formPart.setAutofocus();
				first = false;
			}

			formPart.setDescription(property.description);
			formPart.setName(property.name);
			formPart.setValue(PropertyController.get().stringProperty(
					property.name));

			pnlProperties.add(formPart);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#reset() */
	@Override
	protected void reset () {
		super.reset();
	}

	private void ready () {
		btnUpdate.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE
						.nextButton("Update"));

		btnUpdate.setEnabled(true);

		// enable properties
	}

	private void loading () {
		btnUpdate.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE.loadingButton(
						"Updating... ", Resources.RES.primaryLoader()
								.getSafeUri()));

		btnUpdate.setEnabled(false);

		// disable properties
	}

}
