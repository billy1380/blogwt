//
//  SeparatorWizardPage.java
//  codegen
//
//  Created by William Shakour (billy1380) on 30 Dec 2014.
//  Copyright © 2014 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.wizard;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public final class SeparatorWizardPage implements WizardPage<Void> {

	public static final SeparatorWizardPage SEPARATOR = new SeparatorWizardPage();

	private SeparatorWizardPage() {}

	@Override
	public boolean isRepeatable() {
		return false;
	}

	@Override
	public Void getData() {
		return null;
	}

	@Override
	public void setData(Void data) {}

	@Override
	public String getPageTitle() {
		return null;
	}

	@Override
	public String getPageDescription() {
		return null;
	}

	@Override
	public Widget getBody() {
		return null;
	}

	@Override
	public WizardPage<?> another() {
		return null;
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public Focusable getAutoFocusField() {
		return null;
	}
}
