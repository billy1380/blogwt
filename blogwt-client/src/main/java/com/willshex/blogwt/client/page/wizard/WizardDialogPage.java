//
//  WizardDialogPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.wizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.wizard.PagePlan;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public abstract class WizardDialogPage extends Page {

	private static WizardDialogPageUiBinder uiBinder = GWT
			.create(WizardDialogPageUiBinder.class);

	interface WizardDialogPageUiBinder extends
			UiBinder<Widget, WizardDialogPage> {}

	static class LatePlanWizardDialog extends WizardDialog {
		public void lateSetPlan (PagePlan plan) {
			setPlan(plan);
		}
	};

	protected @UiField(provided = true) WizardDialog wizard = new LatePlanWizardDialog();

	/**
	 * @param pageType
	 */
	public WizardDialogPage (PageType pageType) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	protected void setPlan (PagePlan plan) {
		((LatePlanWizardDialog) wizard).lateSetPlan(plan);
	}
}
