//
//  BooleanPropertyPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 27 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.property;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class BooleanPropertyPart extends AbstractPropertyPart {

	private static BooleanPropertyPartUiBinder uiBinder = GWT
			.create(BooleanPropertyPartUiBinder.class);

	interface BooleanPropertyPartUiBinder extends
			UiBinder<Widget, BooleanPropertyPart> {}

	@UiField CheckBox cbxValue;
	@UiField Element elName;

	public BooleanPropertyPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("cbxValue")
	void onCheckBoxValueChanged (ValueChangeEvent<Boolean> vce) {
		setValue(vce.getValue().toString(), true);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object,
	 * boolean) */
	@Override
	public void setValue (String value, boolean fireEvents) {
		if (value == null) {
			value = "false";
		}

		String oldValue = getValue();

		cbxValue.setValue(Boolean.valueOf(value));
		this.value = value;

		if (value.equals(oldValue)) { return; }
		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasName#setName(java.lang.String) */
	@Override
	public void setName (String name) {
		elName.setInnerText(name);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasName#getName() */
	@Override
	public String getName () {
		return elName.getInnerText();
	}

	/**
	 * @param description
	 */
	public void setDescription (String description) {
		cbxValue.setText(description);
	}

}
