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

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#isRepeatable() */
	@Override
	public boolean isRepeatable () {
		return false;
	}


	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getData()
	 */
	@Override
	public T getData () {
		return null;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.gwt.wizard.WizardPage#getPageTitle() */
	@Override
	public String getPageTitle () {
		return null;
	}

	
	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getPageDescription()
	 */
	@Override
	public String getPageDescription () {
		return null;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.gwt.wizard.WizardPage#getBody() */
	@Override
	public Widget getBody () {
		return null;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.gwt.wizard.WizardPage#another() */
	@Override
	public WizardPage<?> another () {
		return null;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.gwt.wizard.WizardPage#validate() */
	@Override
	public boolean validate () {
		return false;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.gwt.wizard.WizardPage#getAutoFocusField() */
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

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.gwt.wizard.WizardPage#setData(java.lang.Object) */
	@Override
	public void setData (Object data) {}

}
