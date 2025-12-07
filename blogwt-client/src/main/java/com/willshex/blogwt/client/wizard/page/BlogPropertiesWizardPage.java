//
//  BlogPropertiesWizardPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.wizard.page;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.helper.PropertyHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class BlogPropertiesWizardPage extends Composite implements
		WizardPage<List<Property>> {

	private static BlogPropertiesWizardPageUiBinder uiBinder = GWT
			.create(BlogPropertiesWizardPageUiBinder.class);

	interface BlogPropertiesWizardPageUiBinder extends
			UiBinder<Widget, BlogPropertiesWizardPage> {}

	@UiField TextBox txtTitle;
	@UiField TextArea txtExtendedTitle;
	@UiField TextBox txtCopyrightHolder;

	public BlogPropertiesWizardPage () {
		initWidget(uiBinder.createAndBindUi(this));
		UiHelper.addPlaceholder(txtTitle, "Blog Title");
		UiHelper.addPlaceholder(txtExtendedTitle,
				"Extended Title (Short Description)");
		UiHelper.addPlaceholder(txtCopyrightHolder, "Copyright Holder Name");
	}
	@Override
	public boolean isRepeatable () {
		return false;
	}
	@Override
	public List<Property> getData () {
		List<Property> properties = new ArrayList<Property>();

		properties.add(PropertyHelper.createTitle(txtTitle.getText()));
		properties.add(PropertyHelper.createExtendedTitle(txtExtendedTitle
				.getText()));
		properties.add(PropertyHelper.createCopyrightHolder(txtCopyrightHolder
				.getText()));
		properties.add(PropertyHelper.createCopyrightUrl(PropertyController
				.get().copyrightHolderUrl().asString()));

		return properties;
	}
	@Override
	public String getPageTitle () {
		return "Properties";
	}
	@Override
	public Widget getBody () {
		return this;
	}
	@Override
	public WizardPage<?> another () {
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean isValid () {
		return true;
	}
	@Override
	public void setData (List<Property> data) {
		
	}
	@Override
	public String getPageDescription () {
		return "Enter some blog details to start. You can change these properties and others later from the admin pages.";
	}
	@Override
	public Focusable getAutoFocusField () {
		return txtTitle;
	}

}
