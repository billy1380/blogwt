//
//  WizardPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 1 Aug 2013.
//  Copyright © 2013 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.wizard;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 * 
 */
public interface WizardPage<T> {

	boolean isRepeatable();

	T getData();

	void setData(T data);

	String getPageTitle();

	String getPageDescription();

	Widget getBody();

	WizardPage<?> another();

	boolean validate();

	Focusable getAutoFocusField();

}
