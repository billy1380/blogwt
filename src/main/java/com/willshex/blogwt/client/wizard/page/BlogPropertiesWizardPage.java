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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.shared.api.datatype.Property;

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
		UiHelper.addPlaceholder(txtTitle, "Title");
		UiHelper.addPlaceholder(txtExtendedTitle, "Extended Title");
		UiHelper.addPlaceholder(txtCopyrightHolder, "Copyright Holder");
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#isRepeatable() */
	@Override
	public boolean isRepeatable () {
		return false;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getData() */
	@Override
	public List<Property> getData () {
		return new ArrayList<Property>();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getPageTitle() */
	@Override
	public String getPageTitle () {
		return "Properties";
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getBody() */
	@Override
	public Widget getBody () {
		return this;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#another() */
	@Override
	public WizardPage<?> another () {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#validate() */
	@Override
	public boolean validate () {
		return true;
	}

}
