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
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.property.AbstractPropertyPart;
import com.willshex.blogwt.client.part.property.BooleanPropertyPart;
import com.willshex.blogwt.client.part.property.CommentPropertyPart;
import com.willshex.blogwt.client.part.property.EmojiPropertyPart;
import com.willshex.blogwt.client.part.property.ImagePropertyPart;
import com.willshex.blogwt.client.part.property.StringPropertyPart;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesResponse;
import com.willshex.blogwt.shared.api.blog.call.event.UpdatePropertiesEventHandler;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.page.PageType;

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
	private Map<String, Property> propertyLookup;

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

			if (!PropertyController.get().updateProperties(
					propertyLookup.values())) {
				ready();
			}
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
		List<Property> values;
		propertyLookup = PropertyHelper.toLookup(values = PropertyHelper
				.properties());

		for (Property property : values) {
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
		AbstractPropertyPart propertyWidget = null;

		if (PropertyHelper.POST_COMMENTS_ENABLED.equals(property.name)) {
			propertyWidget = new CommentPropertyPart();
		} else if (PropertyHelper.POST_ENABLE_EMOJI.equals(property.name)) {
			propertyWidget = new EmojiPropertyPart();
		} else if (PropertyHelper.SMALL_LOGO_URL.equals(property.name)
				|| PropertyHelper.LARGE_LOGO_URL.equals(property.name)) {
			propertyWidget = new ImagePropertyPart();
		} else if (PropertyHelper.FAVICON_URL.equals(property.name)) {
			propertyWidget = new ImagePropertyPart();
			((ImagePropertyPart) propertyWidget).setValidExtensions("ico");
		} else if (property.type.equals("boolean")) {
			propertyWidget = new BooleanPropertyPart();
		} else if (PropertyHelper.SHORT_DESCRIPTION.equals(property.name)) {
			propertyWidget = new StringPropertyPart(true);
		} else {
			propertyWidget = new StringPropertyPart();

			if (first) {
				((StringPropertyPart) propertyWidget).setAutofocus();
			}
		}

		propertyWidget.setDescription(property.description);
		propertyWidget.setName(property.name);
		propertyWidget.setValue(PropertyController.get().stringProperty(
				property.name));
		register(propertyWidget.addValueChangeHandler(this));

		return propertyWidget;
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

		// enable properties
		btnUpdate.setEnabled(true);

		if (propertyWidgets != null) {
			for (Widget widget : propertyWidgets) {
				if (widget instanceof Focusable) {
					((Focusable) widget).setFocus(true);
					break;
				}
			}
		}
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

		// FIXME: this is a work around for refreshing properties and tags - can probably do better
		Window.Location.replace("/");
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
		String name = null;
		if (event.getSource() instanceof HasName) {
			name = ((HasName) event.getSource()).getName();
		}

		if (name != null) {
			propertyLookup.get(name).value = event.getValue();
		}
	}

}
