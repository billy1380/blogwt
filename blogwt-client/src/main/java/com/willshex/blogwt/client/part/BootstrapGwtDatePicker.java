//
//  BootstrapGwtDatePicker.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Dec 2014.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author William Shakour (billy1380)
 *
 */
public interface BootstrapGwtDatePicker extends ClientBundle {
	public interface BootstrapGwtDatePickerStyles extends CssResource {}

	public static final BootstrapGwtDatePicker INSTANCE = GWT.create(BootstrapGwtDatePicker.class);

	@Source("res/bootstrap-gwt-datepicker.gss")
	BootstrapGwtDatePickerStyles styles();
}