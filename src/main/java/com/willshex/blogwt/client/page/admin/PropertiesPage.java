//
//  PropertiesPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.part.property.CommentPart;
import com.willshex.blogwt.client.part.property.StringPropertyPart;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesResponse;
import com.willshex.blogwt.shared.api.blog.call.event.UpdatePropertiesEventHandler;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.helper.PropertyHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PropertiesPage extends Page implements
		UpdatePropertiesEventHandler, ValueChangeHandler<String> {

	private static PropertiesPageUiBinder uiBinder = GWT
			.create(PropertiesPageUiBinder.class);

	interface PropertiesPageUiBinder extends UiBinder<Widget, PropertiesPage> {}

	@UiField HTMLPanel pnlProperties;
	@UiField SubmitButton btnUpdate;

	private List<Widget> propertyWidgets;

	public PropertiesPage () {
		super(PageType.PropertiesPageType);
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				UpdatePropertiesEventHandler.TYPE, PropertyController.get(),
				this));

		btnUpdate.removeFromParent();
		addProperties();
		pnlProperties.add(btnUpdate);

		ready();
	}

	@UiHandler("btnUpdate")
	void onUpdateClicked (ClickEvent ce) {
		if (isValid()) {
			loading();

			PropertyController.get().updateProperties(
					PropertyHelper.properties());
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
		boolean first = true;
		Widget w;
		for (Property property : PropertyHelper.properties()) {
			pnlProperties.add(w = widget(property, first));

			if (propertyWidgets == null) {
				propertyWidgets = new ArrayList<Widget>();
			}

			propertyWidgets.add(w);
			first = false;
		}
	}

	/**
	 * @param property
	 * @param first
	 * @return
	 */
	private Widget widget (Property property, boolean first) {
		Widget w = null;

		if (PropertyHelper.POST_COMMENTS_ENABLED.equals(property.name)) {
			CommentPart formPart = new CommentPart();

			formPart.setDescription(property.description);
			formPart.setName(property.name);
			formPart.setValue(PropertyController.get().stringProperty(
					property.name));
			register(formPart.addValueChangeHandler(this));

			w = formPart;
		} else {
			StringPropertyPart formPart = new StringPropertyPart();

			if (first) {
				formPart.setAutofocus();
			}

			formPart.setDescription(property.description);
			formPart.setName(property.name);
			formPart.setValue(PropertyController.get().stringProperty(
					property.name));
			register(formPart.addValueChangeHandler(this));

			w = formPart;
		}

		return w;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#reset() */
	@Override
	protected void reset () {
		super.reset();

		if (propertyWidgets != null) {
			for (Widget widget : propertyWidgets) {
				widget.removeFromParent();
			}

			propertyWidgets.clear();
		}
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

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.UpdatePropertiesEventHandler
	 * #updatePropertiesSuccess(com.willshex.blogwt.shared.api.blog.call.
	 * UpdatePropertiesRequest,
	 * com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesResponse) */
	@Override
	public void updatePropertiesSuccess (UpdatePropertiesRequest input,
			UpdatePropertiesResponse output) {
		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.UpdatePropertiesEventHandler
	 * #updatePropertiesFailure(com.willshex.blogwt.shared.api.blog.call.
	 * UpdatePropertiesRequest, java.lang.Throwable) */
	@Override
	public void updatePropertiesFailure (UpdatePropertiesRequest input,
			Throwable caught) {
		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange
	 * (com.google.gwt.event.logical.shared.ValueChangeEvent) */
	@Override
	public void onValueChange (ValueChangeEvent<String> event) {
		Window.alert(event.getValue());
	}

}
