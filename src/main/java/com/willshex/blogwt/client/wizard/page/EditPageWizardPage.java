//
//  EditPageWizardPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.wizard.page;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.shared.api.datatype.Page;

/**
 * @author William Shakour (billy1380)
 *
 */
public class EditPageWizardPage extends Composite implements WizardPage<Page> {

	private static EditPageWizardPageUiBinder uiBinder = GWT
			.create(EditPageWizardPageUiBinder.class);

	interface EditPageWizardPageUiBinder extends
			UiBinder<Widget, EditPageWizardPage> {}

	@UiField TextBox txtTitle;
	@UiField Element elSlug;
	@UiField CheckBox cbxHasParent;
	@UiField SuggestBox txtParentPage;
	@UiField CheckBox cbxHasPriority;
	@UiField TextBox txtPriority;

	private Page page;
	private Page suggestParent;

	public EditPageWizardPage () {
		initWidget(uiBinder.createAndBindUi(this));
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
	public Page getData () {
		if (page == null) {
			page = new Page();
		}

		if (page.owner == null) {
			page.owner = SessionController.get().user();
		}

		if (cbxHasParent.getValue().booleanValue()) {
			page.parent = suggestParent;
		} else {
			page.parent = null;
		}

		if (cbxHasPriority.getValue().booleanValue()) {
			page.priority = Float.valueOf(txtPriority.getValue());
		} else {
			page.priority = null;
		}

		page.title = txtTitle.getValue();
		page.slug = PostHelper.slugify(page.title);

		return page;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.wizard.WizardPage#setData(java.lang.Object) */
	@Override
	public void setData (Page data) {
		page = data;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getPageTitle() */
	@Override
	public String getPageTitle () {
		return "Page Details";
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getPageDescription() */
	@Override
	public String getPageDescription () {
		return PostHelper
				.makeMarkup("Enter the page details then press next to select the content to add to the page.\n\nParentless pages will appear in the header, but only if they are assigned a priority.\n\nParentless and priorityless pages are effectively invisible.");
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

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getAutoFocusField() */
	@Override
	public Focusable getAutoFocusField () {
		return txtTitle;
	}

	@UiHandler("txtTitle")
	void onTitleValueChanged (ValueChangeEvent<String> e) {
		elSlug.setInnerText(PostHelper.slugify(txtTitle.getValue()));
	}

	@UiHandler({ "cbxHasParent", "cbxHasPriority" })
	void onHasParent (ValueChangeEvent<Boolean> e) {
		if (e.getSource() == cbxHasParent) {
			txtParentPage.setEnabled(e.getValue());

			if (!e.getValue().booleanValue()) {
				txtParentPage.setText("");
			}
		} else if (e.getSource() == cbxHasPriority) {
			txtPriority.setEnabled(e.getValue());

			if (e.getValue().booleanValue()) {
				txtPriority.setText("1");
			} else {
				txtPriority.setText("none");
			}
		}
	}

}
