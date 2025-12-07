//
//  DeferredWizardPage.java
//  codegen
//
//  Created by William Shakour (billy1380) on 14 Aug 2013.
//  Copyright © 2013 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.client.wizard;

import java.util.List;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 * 
 */
public final class DeferredWizardPage<T> implements WizardPage<T> {

	private DeferredWizardPageDelegate delegate;

	public DeferredWizardPage (DeferredWizardPageDelegate delegate) {
		this.delegate = delegate;
	}
	@Override
	public boolean isRepeatable () {
		return false;
	}
	@Override
	public T getData () {
		return null;
	}
	@Override
	public String getPageTitle () {
		return null;
	}
	@Override
	public String getPageDescription () {
		return null;
	}
	@Override
	public Widget getBody () {
		return null;
	}
	@Override
	public WizardPage<?> another () {
		return null;
	}
	@Override
	public boolean isValid () {
		return false;
	}
	@Override
	public Focusable getAutoFocusField () {
		return null;
	}

	/**
	 * @param pages
	 * @param index
	 * @return
	 */
	public List<WizardPage<?>> getPages (List<WizardPage<?>> pages, int current) {
		return delegate == null ? null : delegate.getPages(pages, current);
	}
	@Override
	public void setData (Object data) {}

}
