//
//  AbstractPropertyPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.property;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasValue;

/**
 * @author William Shakour (billy1380)
 *
 */
public abstract class AbstractPropertyPart extends Composite implements
		HasValue<String>, HasName {

	protected String value;
	@Override
	public HandlerRegistration addValueChangeHandler (
			ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
	@Override
	public void setValue (String value) {
		setValue(value, false);
	}
	@Override
	public String getValue () {
		return value;
	}
	
	public abstract void setDescription(String description);

}
